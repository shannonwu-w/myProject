package com.myproject.server.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/test")
    public Map<String, String> testJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, String> result = new HashMap<>();

        if (authentication == null || authentication.getPrincipal() == null) {
            result.put("status", "未登入或 JWT 不存在");
        } else {
            result.put("status", "JWT 收到！");
            result.put("principal", authentication.getPrincipal().toString());
            result.put("authorities", authentication.getAuthorities().toString());
        }
        return result;
    }
}
