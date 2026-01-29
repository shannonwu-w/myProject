package com.myproject.server.controller;

import com.myproject.server.domain.dto.LoginDto;
import com.myproject.server.domain.dto.UserCert;
import com.myproject.server.exception.CertException;
import com.myproject.server.service.CertService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // 確保回傳資料(JSON)，而不是找 HTML 頁面
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private CertService certService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto, HttpSession session) {

        // 1. 驗證碼比對
        String sessionAuthcode = (String) session.getAttribute("authcode");
        if (sessionAuthcode == null || !sessionAuthcode.equalsIgnoreCase(loginDto.getAuthcode())) {
            return ResponseEntity.badRequest().body("驗證碼錯誤或已過期");
        }

        try {
            // 2. 執行驗證邏輯 (使用 email)
            UserCert userCert = certService.getCert(loginDto.getEmail(), loginDto.getPassword());

            // 3. 登入成功：存入 Session 供 Filter 檢查
            session.setAttribute("userCert", userCert);

            // 4. 回傳 UserCert (前端會收到包含 email, username, role 的 JSON)
            return ResponseEntity.ok(userCert);

        } catch (CertException e) {
            // 5. 驗證失敗：回傳 401 並附帶錯誤訊息
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    @GetMapping("/status")
    public ResponseEntity<?> getStatus(HttpSession session){
        UserCert cert = (UserCert) session.getAttribute("userCert");
        if (cert != null) {
            // 有登入，直接回傳憑證物件
            return ResponseEntity.ok(cert);
        } else {
            // 沒登入，回傳 401
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("未登入");
        }
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();  // 清空 session 資料
        return "logout";  // 回到登出畫面
    }
}