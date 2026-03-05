package com.myproject.server.controller;

import com.myproject.server.domain.dto.LoginDto;
import com.myproject.server.domain.dto.UserCert;
import com.myproject.server.exception.CertException;
import com.myproject.server.service.CertService;
import javax.servlet.http.HttpSession;

import com.myproject.server.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private CertService certService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        try {
            // 帳號密碼驗證
            UserCert userCert = certService.getCert(
                    loginDto.getEmail(),
                    loginDto.getPassword()
            );

            // 產生 JWT（放 email + role）
            String token = jwtUtil.generateToken(
                    userCert.getEmail(),
                    userCert.getRole()
            );

            // 回傳給前端（JWT + 顯示用資料）
            return ResponseEntity.ok(
                    Map.of(
                            "token", token,
                            "email", userCert.getEmail(),
                            "username", userCert.getUsername(),
                            "role", userCert.getRole()
                    )
            );

        } catch (CertException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        }
    }

//    @GetMapping("/status")
//    public ResponseEntity<?> getStatus(HttpSession session){
//        UserCert cert = (UserCert) session.getAttribute("userCert");
//        if (cert != null) {
//            // 有登入，直接回傳憑證物件
//            return ResponseEntity.ok(cert);
//        } else {
//            // 沒登入，回傳 401
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("未登入");
//        }
//    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();  // 清空 session 資料
        return "logout";  // 回到登出畫面
    }
}