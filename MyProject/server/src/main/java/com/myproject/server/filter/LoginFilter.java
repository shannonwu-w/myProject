package com.myproject.server.filter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = {"/api/admin/*","/api/profile"})
public class LoginFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String requestURI = request.getRequestURI();

        // 1. 定義白名單：登入 API 不檢查
        if (requestURI.equals("/api/login")) {
            chain.doFilter(request, response);
            return;
        }

        // 檢查 session 中是否有 userCert
        HttpSession session = request.getSession();
        if(session.getAttribute("userCert") == null) { // 沒有憑證
            response.sendRedirect("/login"); // 重導到登入頁面
            return;
        }
        // 放行
        chain.doFilter(request, response);
    }

}
