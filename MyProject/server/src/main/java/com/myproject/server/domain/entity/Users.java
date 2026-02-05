package com.myproject.server.domain.entity;

import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "USERS")
@Data
public class Users {

    // 主鍵，數字自增
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USERS_ID_SEQ")
    @SequenceGenerator(
            name = "USERS_ID_SEQ",
            sequenceName = "USERS_ID_SEQ",
            allocationSize = 1
    )
    @Column(name = "USER_ID")
    private Long userId;

    // Email 保留唯一約束，用來登入
    @Column(name = "EMAIL", nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "PASSWORD_HASH", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "SALT", length = 255)
    private String salt;

    @Column(name = "PHONE", length = 20)
    private String phone;

    @Column(name = "USERNAME", nullable = false, length = 50)
    private String username;

    @Column(name = "ROLE", length = 20)
    private String role;

    @CreationTimestamp
    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
}
