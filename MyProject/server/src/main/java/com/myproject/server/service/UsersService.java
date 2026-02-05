package com.myproject.server.service;

import com.myproject.server.domain.dto.UserProfileDto;
import com.myproject.server.domain.entity.Users;
import com.myproject.server.domain.dto.UsersDto;
import com.myproject.server.mapper.UsersMapper;
import com.myproject.server.repository.UsersRepository;
import com.myproject.server.util.Hash;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;


@AllArgsConstructor
@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;

    /**
     * 查詢所有使用者
     */
    public Page<UsersDto> findAllUsersByKeyword(String keyword, Pageable pageable) {
        Page<Users> usersPage = usersRepository.findByAllFields(keyword, pageable);
        // 確保 Mapper 能夠將 Users 實體轉為 UsersDto
        return usersPage.map(usersMapper::toDto);
    }

    @Transactional
    public void addUser(UsersDto dto) {
        Users user;

        // 1. 判斷是更新還是新增
        if (dto.getUserId() != null && dto.getUserId() > 0) {
            // 【修改模式】一定要先從 DB 撈出持久化狀態的物件
            user = usersRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("找不到使用者 ID: " + dto.getUserId()));
        } else {
            // 【新增模式】
            user = new Users();
        }

        // 2. 只有新使用者或 Email 改變時才設定 Email
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setPhone(dto.getPhone());
        user.setRole(dto.getRole());

        // 3. 處理密碼：前端沒傳密碼就不動它 (保留原密碼)
        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            String salt = Hash.getSalt();
            String passwordHash = Hash.getHash(dto.getPassword(), salt);
            user.setSalt(salt);
            user.setPasswordHash(passwordHash);
        }

        // 4. 儲存。JPA 看到 user 身上有 ID，就會自動發送 UPDATE 而非 INSERT
        usersRepository.save(user);
    }

    public UserProfileDto getUserProfileByEmail(String email) throws Exception {
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("使用者不存在"));

        UserProfileDto dto = new UserProfileDto();
        dto.setUsername(user.getUsername());
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        return dto;
    }
    @Transactional
    public UsersDto updateUserPassword(Long userId, UsersDto dto) {

        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("使用者不存在"));

        // 可修改的欄位
//        user.setUsername(dto.getUsername());
//        user.setPhone(dto.getPhone());

        // 如果有輸入新密碼，才更新
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            String salt = Hash.getSalt();
            String passwordHash = Hash.getHash(dto.getPassword(), salt);
            user.setSalt(salt);
            user.setPasswordHash(passwordHash);
        }

        usersRepository.save(user); // JPA 會自動做 UPDATE
        return dto;
    }

    public void deleteUser(@RequestParam Long userId){
        usersRepository.deleteById(userId);
    }



}


