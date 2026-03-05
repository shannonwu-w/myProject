package com.myproject.server.filter;

import com.myproject.server.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // 如果沒有 Authentication 且 Header 有 Bearer token
        if (SecurityContextHolder.getContext().getAuthentication() == null
                && authHeader != null
                && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7);

            try {
                // 驗證 token
                if (jwtUtil.validateToken(token)) {

                    String email = jwtUtil.extractEmail(token);
                    String role = jwtUtil.extractRole(token);

                    // 組成 Spring Security Authentication，注意 ROLE_ 前綴
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    email,
                                    null,
                                    List.of(new SimpleGrantedAuthority("ROLE_" + role))
                            );

                    // 放入 SecurityContext
                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);
                }
            } catch (Exception e) {
                SecurityContextHolder.clearContext(); // token 無效 → 清空
            }
        }

        filterChain.doFilter(request, response);
    }
}