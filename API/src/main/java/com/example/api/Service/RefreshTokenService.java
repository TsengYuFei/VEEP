package com.example.api.Service;

import com.example.api.Entity.RefreshToken;
import com.example.api.Entity.User;
import com.example.api.Exception.NotFoundException;
import com.example.api.Other.JwtUtil;
import com.example.api.Repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    @Autowired
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private final SingleUserService singleUserService;



    public RefreshToken createOrUpdateToken(String account) {
        System.out.println("RefreshTokenService: createOrUpdateToken >> "+account);
        User user = singleUserService.getUserByAccount(account);

        String newTokenValue = UUID.randomUUID().toString();
        LocalDateTime deadline = LocalDateTime.now().plusMinutes(JwtUtil.REFRESH_TOKEN_VALIDITY_MINUTES);
        RefreshToken token = refreshTokenRepository.findByUser(user).orElse(null);

        if (token != null) {
            token.setToken(newTokenValue);
            token.setDeadline(deadline);
            return refreshTokenRepository.save(token);
        } else {
            RefreshToken newToken = new RefreshToken();
            newToken.setUser(user);
            newToken.setToken(newTokenValue);
            newToken.setDeadline(deadline);
            return refreshTokenRepository.save(newToken);
        }
    }


    public RefreshToken getTokenByUserAccount(String userAccount){
        System.out.println("RefreshTokenService: getTokenByUserAccount >> "+userAccount);
        User user = singleUserService.getUserByAccount(userAccount);
        return refreshTokenRepository.findByUser(user)
                .orElseThrow(() -> new NotFoundException("找不到使用者帳號為 < "+userAccount+" > 的RefreshToken"));
    }


    public RefreshToken getTokenByToken(String token) {
        System.out.println("RefreshTokenService: getTokenByToken >> "+token);
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException("找不到token為 < "+token+" > 的RefreshToken"));
    }


    public boolean isTokenExpired(RefreshToken token){
        System.out.println("RefreshTokenService: isTokenExpired >> "+token);
        return token.getDeadline().isBefore(LocalDateTime.now());
    }


    public void saveToken(RefreshToken token){
        System.out.println("RefreshTokenService: saveToken >> "+token);
        refreshTokenRepository.save(token);
    }


    @Transactional
    public void deleteTokenByToken(String token){
        System.out.println("RefreshTokenService: deleteTokenByToken >> "+token);
        refreshTokenRepository.deleteByToken(token);
    }
}
