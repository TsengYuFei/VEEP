package com.example.api.Controller;

import com.example.api.DTO.Request.RefreshTokenRequest;
import com.example.api.DTO.Response.RefreshTokenResponse;
import com.example.api.Entity.RefreshToken;
import com.example.api.Entity.User;
import com.example.api.Exception.ForibiddenException;
import com.example.api.Other.JwtUtil;
import com.example.api.Service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Tag(name = "更新token")
@RequestMapping("/token")
@RestController
@RequiredArgsConstructor
public class RefreshTokenController {
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;



    @Operation(
            summary = "更新token",
            description = "更新db中的refresh token(有效7 days)，並給一組新的access token(有效30 mins)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "成功更新token",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RefreshTokenResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "找不到token"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshAccessToken(@Valid @RequestBody RefreshTokenRequest request){
        System.out.println("RefreshTokenController: refreshAccessToken");
        String tokenValue = request.getRefreshToken();
        RefreshToken token = refreshTokenService.getTokenByToken(tokenValue);
        if(refreshTokenService.isTokenExpired(token)) throw new ForibiddenException("Refresh Token 逾期，請重新登入");

        User user = token.getUser();
        String newAccessToken = jwtUtil.generateToken(user.getUserAccount());
        String newRefreshToken = jwtUtil.generateRefreshToken(user.getUserAccount());
        token.setToken(newRefreshToken);
        token.setDeadline(LocalDateTime.now().plusMinutes(JwtUtil.REFRESH_TOKEN_VALIDITY_MINUTES));
        refreshTokenService.saveToken(token);

        RefreshTokenResponse response = new RefreshTokenResponse(newAccessToken, newRefreshToken);
        return ResponseEntity.ok(response);
    }

}
