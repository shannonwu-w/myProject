package com.myproject.server.domain.dto;

import lombok.Data;

@Data
public class UsersDto {
    private Long userId; // 使用者 Id
    private String username; // 使用者名稱
    private String password; // 接收前端傳來的明文密碼
    private String email; // 使用者Email
    private String role; // 角色權限

}
