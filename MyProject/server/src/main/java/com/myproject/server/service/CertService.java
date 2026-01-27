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

@Service
public class CertService {

    @Autowired
    private UsersRepository usersRepository;

    public UserCert getCert(String username, String password) throws CertException {
        // 1. 是否有此人
        Users user = usersRepository.findByUsername(username);
        if(user == null) {
            throw new UserNotFoundException("查無此人");
        }
        // 2. 密碼 hash 比對
        String passwordHash = Hash.getHash(password, user.getSalt());
        if(!passwordHash.equals(user.getPasswordHash())) {
            throw new PasswordInvalidException("密碼錯誤");
        }
        // 3. 簽發憑證
        UserCert userCert = new UserCert(user.getUserId(), user.getUsername(), user.getRole());
        return userCert;
    }
}



