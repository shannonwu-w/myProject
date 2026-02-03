package com.myproject.server.controller;

import com.myproject.server.domain.dto.UserCert;
import com.myproject.server.domain.dto.UserProfileDto;
import com.myproject.server.domain.dto.UsersDto;
import com.myproject.server.service.UsersService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class UsersController {

    private final UsersService usersService;

    @PostMapping("/register")
    public Map<String, Object> registerUsers(@RequestBody UsersDto usersDto) {
        Map<String, Object> result = new HashMap<>();
        try {
            usersService.addUser(usersDto);

            result.put("success", true);
            result.put("message", "註冊成功");

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "註冊失敗：" + e.getMessage());

        }
        return  result;
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

    @PostMapping("/reset-password")
    public UsersDto updateUser(@RequestBody UsersDto dto, HttpSession session) {
        UserCert userCert = (UserCert) session.getAttribute("userCert");
        if (userCert == null) throw new RuntimeException("未登入");

        Long userId = userCert.getUserId(); // 從 session 拿登入使用者 id
        return usersService.updateUserPassword(userId, dto);
    }

    @GetMapping("/admin/all-users")
    public List<UsersDto> allUserList(){
      return usersService.findAllUsers();
    }







}
