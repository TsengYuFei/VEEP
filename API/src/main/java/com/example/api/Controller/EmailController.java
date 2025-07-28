package com.example.api.Controller;

import com.example.api.Service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "寄信相關")
@RequestMapping("/mail")
@RestController
@RequiredArgsConstructor
public class EmailController {
    @Autowired
    private final EmailService emailService;



    @Operation(summary = "重新寄送驗證碼")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "成功寄送驗證信"
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
    @PostMapping("/resend/verification/{userAccount}")
    public ResponseEntity<?> resendVerificationEmail(
            @Parameter(description = "使用者帳號", required = true)
            @PathVariable String userAccount
    ){
        System.out.println("EmailController: resendVerificationEmail >> "+userAccount);
        emailService.resendVerificationEmail(userAccount);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @Operation(
            summary = "寄送重設密碼信",
            description = "用於忘記密碼"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "成功寄送重設密碼信"
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
    @PostMapping("/send/reset_password/{mail}")
    public ResponseEntity<?> resetPasswordEmail(
            @Parameter(description = "電子郵箱", required = true)
            @PathVariable String mail
    ){
        System.out.println("EmailController: resetPasswordEmail >> "+mail);

        emailService.resetPasswordEmail(mail);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
