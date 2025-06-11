package com.example.api.Controller;

import com.example.api.DTO.Request.LoginRequest;
import com.example.api.DTO.Response.LoginResponse;
import com.example.api.Service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "登入")
@RequestMapping("/login")
@RestController
@RequiredArgsConstructor
public class LoginController {
    @Autowired
    private final LoginService loginService;



    @Operation(
            summary = "登入"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "成功登入",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "密碼錯誤"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "找不到使用者"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @PostMapping
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest){
        System.out.println("LoginController: login");
        LoginResponse jwt = loginService.login(loginRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(jwt);
    }
}
