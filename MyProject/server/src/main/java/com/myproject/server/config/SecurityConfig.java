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

                .formLogin().disable()
                .httpBasic().disable()

                // 設定無狀態 Session（JWT 不需要 server session）
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                // 權限設定
                .authorizeRequests()
                // OPTIONS 放行 (CORS)
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // 登入、驗證碼、註冊 API 放行
                .antMatchers("/api/login", "/api/authcode", "/api/register").permitAll()

                // 管理員專屬 API
                .antMatchers("/api/admin/**").hasRole("ADMIN")

                // 其他 API 需要登入
                .anyRequest().authenticated()
                .and()

                // 加入 JWT Filter
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}