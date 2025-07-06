package com.example.api.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    @NotBlank(message = "The password cannot be empty.")
    @Size(min = 6, max = 20, message = "舊密碼長度應該在6~20字元之間")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$", message = "舊密碼須包含至少一個大寫字母、一個小寫字母及一個數字")
    String oldPassword;

    @NotBlank(message = "The password cannot be empty.")
    @Size(min = 6, max = 20, message = "新密碼長度應該在6~20字元之間")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$", message = "新密碼須包含至少一個大寫字母、一個小寫字母及一個數字")
    String newPassword;
}
