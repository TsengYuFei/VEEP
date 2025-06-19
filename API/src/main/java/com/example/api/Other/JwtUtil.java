package com.example.api.Other;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey key;
    // 30 mins
    public static final long ACCESS_TOKEN_VALIDITY_MINUTES = 30;

    // 7 days
    public static final long REFRESH_TOKEN_VALIDITY_MINUTES = 60 * 24 * 7;

    public JwtUtil(@Value("${jwt.secret}") String jwtSecret) {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken(String account) {
        return Jwts.builder()
                .setSubject(account)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_MINUTES * 60 * 1000))
                .signWith(key)
                .compact();
    }


    public String generateRefreshToken(String account) {
        return Jwts.builder()
                .setSubject(account)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY_MINUTES * 60 * 1000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    public String getUserAccountFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody();
//            System.out.println("JWT Debug: Claims 內容：" + claims);
            return claims.getSubject(); // userAccount
        } catch (ExpiredJwtException e) {
            System.out.println("JWT Debug: Token 過期");
            throw e;
        } catch (SignatureException e) {
            System.out.println("JWT Debug: 簽名錯誤");
            throw e;
        } catch (MalformedJwtException e) {
            System.out.println("JWT Debug: Token 格式錯誤");
            throw e;
        } catch (Exception e) {
            System.out.println("JWT Debug: 其他錯誤：" + e.getMessage());
            throw e;
        }
    }



    public boolean legalToken(String token, String account) {
        String userAccount = getUserAccountFromToken(token);
        return userAccount.equals(account) && !legalTokenDate(token);
    }


    public boolean legalTokenDate(String token) {
        Date date = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return date.before(new Date(System.currentTimeMillis()));
    }
}
