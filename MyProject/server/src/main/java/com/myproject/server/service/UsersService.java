package com.myproject.server.service;

import com.myproject.server.domain.dto.UserProfileDto;
import com.myproject.server.domain.entity.Users;
import com.myproject.server.domain.dto.UsersDto;
import com.myproject.server.mapper.UsersMapper;
import com.myproject.server.repository.UsersRepository;
import com.myproject.server.util.Hash;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;

    /**
     * 查詢所有使用者
     */
    public List<UsersDto> findAllUsers() {
        List<Users> usersList = usersRepository.findAll();

        // 使用 Stream 批量轉換 Entity -> DTO
        return usersList.stream()
                .map(usersMapper::toDto)
                .toList();
    }

    @Transactional
    public void addUser(UsersDto dto) {

        String salt = Hash.getSalt();
        String passwordHash = Hash.getHash(dto.getPassword(), salt);

        Users user = usersMapper.toEntity(dto);
        user.setPasswordHash(passwordHash);
        user.setSalt(salt);

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

    public Users changeUserPassword(Users user){
        return usersRepository.save(user);
    }

}


