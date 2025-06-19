package com.example.api.Service;

import com.example.api.DTO.Request.LoginRequest;
import com.example.api.DTO.Response.LoginResponse;
import com.example.api.Entity.RefreshToken;
import com.example.api.Entity.User;
import com.example.api.Exception.NotFoundException;
import com.example.api.Exception.UnauthorizedException;
import com.example.api.Other.JwtUtil;
import com.example.api.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final JwtUtil jwtUtil;

    @Autowired
    private final RefreshTokenService refreshTokenService;



    public LoginResponse login(LoginRequest request) {
        System.out.println("LoginService: login");

        User user = userRepository.findByMail(request.getUserAccountOrMail());
        if(user == null) user = userRepository.findByUserAccount(request.getUserAccountOrMail());

        if(user == null) throw new NotFoundException("Can't find the user with email or user account < "+request.getUserAccountOrMail()+" >");
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) throw new UnauthorizedException("XX Wrong password XX");

        String accessToken = jwtUtil.generateToken(user.getUserAccount());
        RefreshToken refreshToken = refreshTokenService.createOrUpdateToken(user.getUserAccount());

        return new LoginResponse(user.getUserAccount(), accessToken, refreshToken.getToken());
    }
}
