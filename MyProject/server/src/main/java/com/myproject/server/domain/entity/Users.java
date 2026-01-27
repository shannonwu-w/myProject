package com.myproject.server.domain.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "USERS")
@Data
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator ="USERS_ID_SEQ") // 自動生成 id
    @SequenceGenerator(
            sequenceName = "USERS_ID_SEQ", // 必須對應 SQL 中的 "USERS_ID_SEQ"
            allocationSize = 1             // 必須對應 SQL 中的 INCREMENT BY 1
    )
    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "USERNAME", unique = true, nullable = false, length = 50)
    private String username;

    @Column(name = "PASSWORD_HASH", nullable = false)
    private String passwordHash;

    @Column(name = "SALT")
    private String salt;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "ROLE")
    private String role;

    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;




}

