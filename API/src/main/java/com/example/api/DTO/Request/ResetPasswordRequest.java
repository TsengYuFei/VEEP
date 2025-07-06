package com.example.api.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordRequest {
    @NotBlank(message = "The reset password token cannot be empty.")
    @Size(max = 255, message = "Reset password的token長度應該在255字元以下")
    private String token;

    @NotBlank(message = "The password cannot be empty.")
    @Size(min = 6, max = 20, message = "密碼長度應該在6~20字元之間")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$", message = "密碼須包含至少一個大寫字母、一個小寫字母及一個數字")
    private String password;
}
