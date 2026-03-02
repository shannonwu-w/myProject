package com.myproject.server.domain.dto;

import lombok.Data;

@Data
public class UsersDto {
    private Long userId;
    private String email;
    private String password;
    private String phone;
    private String username;
    private String role;
}
