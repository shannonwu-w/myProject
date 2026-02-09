package com.myproject.server.service;

import com.myproject.server.domain.dto.UserCert;
import com.myproject.server.domain.entity.Users;
import com.myproject.server.exception.CertException;
import com.myproject.server.exception.PasswordInvalidException;
import com.myproject.server.exception.UserNotFoundException;
import com.myproject.server.repository.UsersRepository;
import com.myproject.server.util.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CertService {

    @Autowired
    private UsersRepository usersRepository;

    public UserCert getCert(String email, String password) throws CertException {
        // 1. 查詢使用者(用email去查)
        Optional<Users> userOpt = usersRepository.findByEmail(email);
        Users user = userOpt.orElseThrow(() -> new UserNotFoundException("查無此人"));

        // 2. 密碼 hash 比對
        String passwordHash = Hash.getHash(password, user.getSalt());
        if (!passwordHash.equals(user.getPasswordHash())) {
            throw new PasswordInvalidException("密碼錯誤");
        }

        // 3. 簽發憑證
        return new UserCert(user.getEmail(), user.getUsername(), user.getRole(),user.getUserId());
    }
}
