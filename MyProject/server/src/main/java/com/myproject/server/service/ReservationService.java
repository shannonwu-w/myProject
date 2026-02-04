package com.myproject.server.service;

import com.myproject.server.domain.dto.ReservationsDto;
import com.myproject.server.domain.dto.UserCert;
import com.myproject.server.domain.entity.Reservations;
import com.myproject.server.domain.entity.TableList;
import com.myproject.server.domain.entity.Users;
import com.myproject.server.mapper.ReservationsMapper;
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
    private final ReservationsMapper reservationsMapper;

    @Transactional
    public void makeReservation(ReservationsDto dto, UserCert userCert) throws Exception {

        // 1. 查找該日期與時段已被預訂的資料
        List<Reservations> existing = reservationRepository.findByResvDateAndTimeSlot(dto.getResvDate(), dto.getTimeSlot());

        // 2. 取得已被佔用的桌號
        Set<Long> reservedTableIds = existing.stream()
                .map(Reservations::getTableId)
                .collect(Collectors.toSet());

        // 3. 根據人數篩選可用桌位
        List<TableList> candidates = findAvailableTables(dto.getPeople(), reservedTableIds);
        if (candidates.isEmpty()) {
            throw new Exception("抱歉，該時段已無適合人數的空位");
        }

        // 4. DTO -> Entity
        Reservations entity = reservationsMapper.toEntity(dto);
        entity.setResvDate(dto.getResvDate());
        entity.setTimeSlot(dto.getTimeSlot());

        // 5. 分配桌位
        entity.setTableId(candidates.get(0).getTableId());

        // 6. 設定使用者
        Users user = usersRepository.findByUserId(userCert.getUserId())
                .orElseThrow(() -> new Exception("使用者不存在"));
        entity.setUserId(user.getUserId());

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

    public List<ReservationsDto> allReservations(){
        List<Reservations> list = reservationRepository.findAll();
        return list.stream()
                .map(reservationsMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<ReservationsDto> getMyReservations(Long userId) {
        List<Reservations> list = reservationRepository.findByUserId(userId);

        // Entity -> DTO
        return list.stream()
                .map(reservationsMapper::toDto)
                .collect(Collectors.toList());
    }



    public void deleteResv(Long reservationId){

        this.reservationRepository.deleteById(reservationId);


    }

    public List<ReservationsDto> getResvEditData(Long reservationId){
        List<Reservations> list = reservationRepository.findByReservationId(reservationId);
        return list.stream()
                .map(reservationsMapper::toDto)
                .collect(Collectors.toList());
    }

}
