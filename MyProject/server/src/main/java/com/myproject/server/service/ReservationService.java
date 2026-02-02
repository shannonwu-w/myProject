package com.myproject.server.service;

import com.myproject.server.domain.dto.ReservationsDto;
import com.myproject.server.domain.dto.UserCert;
import com.myproject.server.domain.entity.Reservations;
import com.myproject.server.domain.entity.TableList;
import com.myproject.server.domain.entity.Users;
import com.myproject.server.repository.ReservationRepository;
import com.myproject.server.repository.TableListRepository;
import com.myproject.server.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TableListRepository tableListRepository;
    private final UsersRepository usersRepository;

    @Transactional
    public void makeReservation(ReservationsDto dto, UserCert userCert) throws Exception {

        // 1. 查找該日期與時段已被預訂的資料
        List<Reservations> existing = reservationRepository.findByResvDateAndTimeSlot(dto.getDate(), dto.getTimeSlot());

        // 2. 取得已被佔用的桌號
        Set<Long> reservedTableIds = existing.stream()
                .map(r -> r.getTableList().getTableId())
                .collect(Collectors.toSet());

        // 3. 根據人數篩選可用桌位 (0: 小桌, 1: 大桌)
        List<TableList> candidates = findAvailableTables(dto.getPeople(), reservedTableIds);
        if (candidates.isEmpty()) {
            throw new Exception("抱歉，該時段已無適合人數的空位");
        }

        // 4. 將 DTO 轉換為 Entity
        Reservations entity = new Reservations();
        entity.setName(dto.getName());
        entity.setPeople(dto.getPeople());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setResvDate(dto.getDate());
        entity.setTimeSlot(dto.getTimeSlot());
        entity.setMessage(dto.getMessage());

        // 5. 分配桌位
        entity.setTableList(candidates.get(0));

        // 6. 設定使用者
        Users user = usersRepository.findByEmail(userCert.getEmail())
                .orElseThrow(() -> new Exception("使用者不存在"));
        entity.setUsers(user);

        // 7. 儲存
        reservationRepository.save(entity);
    }

    private List<TableList> findAvailableTables(Integer people, Set<Long> reservedTableIds) throws Exception {
        int requiredCategory = (people <= 4) ? 0 : 1; // 0:小桌, 1:大桌
        if (people > 6) {
            throw new Exception("目前僅提供 1-6 人訂位");
        }

        return tableListRepository.findAll().stream()
                .filter(t -> t.getTableSize() == requiredCategory)
                .filter(t -> !reservedTableIds.contains(t.getTableId()))
                .collect(Collectors.toList());
    }
}
