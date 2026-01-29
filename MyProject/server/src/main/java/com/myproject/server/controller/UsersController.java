package com.myproject.server.controller;

import com.myproject.server.domain.dto.UserCert;
import com.myproject.server.domain.dto.UserProfileDto;
import com.myproject.server.domain.dto.UsersDto;
import com.myproject.server.domain.entity.Users;
import com.myproject.server.service.UsersService;
import jakarta.servlet.http.HttpSession;
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

    // 查詢所有使用者
    @GetMapping("/users")
    public ResponseEntity<Object> findUsers() {
        try {
            return ResponseEntity.ok(usersService.findAllUsers());
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "查詢使用者失敗：" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUsers(@RequestBody UsersDto usersDto) {
        Map<String, Object> result = new HashMap<>();
        try {
            usersService.addUser(usersDto);

            result.put("success", true);
            result.put("message", "註冊成功");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "註冊失敗：" + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }


    @GetMapping("/profile")
    public UserProfileDto getUserProfile(HttpSession session) throws Exception {

        UserCert cert = (UserCert) session.getAttribute("userCert");
        if (cert == null) {
            throw new Exception("使用者未登入");
        }

        String email = cert.getEmail();
        return usersService.getUserProfileByEmail(email);
    }


}
