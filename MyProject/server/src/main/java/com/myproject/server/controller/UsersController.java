package com.myproject.server.controller;

import com.myproject.server.domain.dto.UsersDto;
import com.myproject.server.service.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class UsersController {
    private final UsersService usersService;

    @GetMapping("/users")
    public Object findUsers(){

        return this.usersService.findAllUsers();

    }
    @PostMapping("/register")
    public  ResponseEntity<Map<String, Object>> registerUsers(@RequestBody UsersDto usersDto) {
        Map<String, Object> result = new HashMap<>();
        try {
            usersService.addUser(
                    usersDto.getUsername(),
                    usersDto.getPassword(),
                    usersDto.getEmail(),
                    usersDto.getRole()
            );
            result.put("success", true);
            result.put("message", "註冊成功");
            return ResponseEntity.ok(result); // 回傳 200 OK
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "註冊失敗：" + e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    }


