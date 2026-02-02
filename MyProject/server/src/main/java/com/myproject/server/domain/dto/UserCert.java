package com.myproject.server.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserCert {
    private String email;
    private String username;
    private String role;
    private Long userId;
}
