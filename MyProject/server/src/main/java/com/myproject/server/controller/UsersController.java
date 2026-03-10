package com.myproject.server.controller;

import com.myproject.server.domain.dto.UserProfileDto;
import com.myproject.server.domain.dto.UsersDto;
import com.myproject.server.service.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class UsersController {

    private final UsersService usersService;

    /** 註冊 */
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
        return result;
    }

    /** 取得使用者個人資料 (JWT) */
    @GetMapping("/profile")
    public UserProfileDto getUserProfile() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new Exception("使用者未登入");
        }

        String email = authentication.getPrincipal().toString();
        return usersService.getUserProfileByEmail(email);
    }

    /** 使用者自行重置密碼 */
    @PostMapping("/reset-password")
    public UsersDto updateUserPassword(@RequestBody UsersDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new RuntimeException("未登入");
        }

        String email = authentication.getPrincipal().toString();
        Long userId = usersService.getUserIdByEmail(email);
        return usersService.updateUserPassword(userId, dto);
    }

    /** 管理員查詢使用者列表 */
    @GetMapping("/admin/find-users")
    public Page<UsersDto> allUserList(
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return usersService.findAllUsersByKeyword(keyword, pageable);
    }

    /** 管理員新增或編輯使用者 */
    @PostMapping("/admin/update-user")
    public Map<String, Object> adminUpdateUser(@RequestBody UsersDto usersDto){
        Map<String, Object> result = new HashMap<>();
        try {
            usersService.addUser(usersDto);
            result.put("success", true);
            result.put("message", "新增/更新成功");
        } catch (Exception e){
            result.put("success", false);
            result.put("message", "新增/更新失敗：" + e.getMessage());
        }
        return result;
    }

    /** 管理員刪除使用者 */
    @Transactional
    @PostMapping("/admin/deleteUser")
    public Map<String, Object> adminDeleteUser(@RequestParam Long userId){
        Map<String, Object> result = new HashMap<>();
        try {
            usersService.deleteUser(userId);
            result.put("success", true);
            result.put("message", "刪除成功");
        } catch (Exception e){
            result.put("success", false);
            result.put("message", "刪除失敗：" + e.getMessage());
        }
        return result;
    }

}