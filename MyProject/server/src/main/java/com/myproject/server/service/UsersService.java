package com.myproject.server.service;

import com.myproject.server.domain.entity.Users;
import com.myproject.server.domain.dto.UsersDto;
import com.myproject.server.mapper.UsersMapper;
import com.myproject.server.repository.UsersRepository;
import com.myproject.server.util.Hash;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@AllArgsConstructor
@Service
public class UsersService{
    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;

    public List<UsersDto> findAllUsers() {
        List<Users> usersList = usersRepository.findAll();

        // 2. 使用 Stream 進行批量轉換
        return usersList.stream()
                    .map(usersMapper::toDto)
                    .toList();
    }
    @Transactional // 建議加上事務管理，確保資料完整性
    public void addUser(String username, String password, String email, String role) {
        // 1. 產生隨機鹽
        String salt = Hash.getSalt();

        // 2. 產生加鹽後的雜湊值
        String passwordHash = Hash.getHash(password, salt);

        // 3. 建立 Entity 並填入資料
        Users user = new Users();
        user.setUsername(username);
        user.setPasswordHash(passwordHash); // 存入雜湊後的密碼
        user.setSalt(salt);                 // 存入這筆資料專用的鹽
        user.setEmail(email);
        user.setRole(role);
        user.setCreatedAt(java.time.LocalDateTime.now());
        // 4. 存入資料庫
        usersRepository.save(user);
    }

}