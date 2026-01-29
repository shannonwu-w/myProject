package com.myproject.server.domain.dto;

import lombok.Data;

@Data
public class UsersDto {
    private String email;      // 使用者 Email (主鍵)
    private String password;   // 接收前端傳來的明文密碼
    private String phone;      // 電話
    private String username;   // 使用者名稱
    private String role;       // 角色權限
}
