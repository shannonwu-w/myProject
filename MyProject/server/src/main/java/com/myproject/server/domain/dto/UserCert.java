package com.myproject.server.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserCert {
    private String email;    // 使用 email 當唯一標識
    private String username;
    private String role;
}
