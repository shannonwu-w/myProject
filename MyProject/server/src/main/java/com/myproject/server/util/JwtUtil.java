package com.myproject.server.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    //  長度至少 32 bytes（你現在是 OK 的）
    private static final String SECRET_KEY = "mySuperSecretJwtKey1234567890123456";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 小時

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    /* =======================
         產生 Token（放 email + role）
       ======================= */
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)              // 身分識別
                .claim("role", role)            // 授權用
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /* =======================
         解析 Claims（統一入口）
       ======================= */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /* =======================
         取得 email
       ======================= */
    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    /* =======================
         取得 role
       ======================= */
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    /* =======================
         驗證 Token（是否有效）
       ======================= */
    public boolean validateToken(String token) {
        try {
            extractAllClaims(token); // 會自動檢查簽章 + 過期
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}