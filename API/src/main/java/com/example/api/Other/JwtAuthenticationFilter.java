package com.example.api.Other;

import com.example.api.Service.MyUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private final JwtUtil jwtUtil;

    @Autowired
    private final MyUserDetailService userDetailService;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if(header == null || !header.startsWith("Bearer ")) {
            System.out.println("JWT Debug: 沒有 Authorization 或格式錯誤");
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.replace("Bearer ", "");
        String userAccount;

        try {
            userAccount = jwtUtil.getUserAccountFromToken(token);
        } catch (Exception e) {
            System.out.println("JWT Debug: 解析Token失敗");
            filterChain.doFilter(request, response);
            return;
        }

        // SecurityContextHolder放驗證結果
        if(userAccount != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailService.loadUserByUsername(userAccount);

            if(jwtUtil.legalToken(token, userDetails.getUsername())){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("JWT Debug: 成功驗證，用戶：" + userAccount + " 角色：" + userDetails.getAuthorities());
            } else {
                System.out.println("JWT Debug: Token非法");
            }
        } else {
            System.out.println("JWT Debug: userAccount為空或已認證");
        }

        filterChain.doFilter(request, response);
    }
}
