package com.myproject.server.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserCert {
        private Long userId;
        private String username;
        private String role;



}
