package com.myproject.server.config;

import com.myproject.server.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors().and()
                .csrf().disable()

                // 1️⃣ 無狀態 Session
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 2️⃣ 權限配置
                .authorizeHttpRequests(auth -> auth
                        // OPTIONS 放行 (CORS)
                        .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 登入與驗證碼放行
                        .antMatchers("/api/login", "/api/authcode").permitAll()

                        // 管理員 API
                        .antMatchers("/api/reservation/**").hasRole("ADMIN")

                        // 其他 API 必須登入
                        .anyRequest().authenticated()
                )

                // 3️⃣ 加入 JWT Filter
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}