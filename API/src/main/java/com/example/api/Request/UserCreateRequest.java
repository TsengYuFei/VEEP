package com.example.api.Request;

import com.example.api.Model.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import java.time.LocalDate;

@Data
public class UserCreateRequest {
    @NotBlank(message = "使用者名稱不可為空")
    @Size(min = 1, max = 20, message = "使用者名稱長度應該在1~20字元之間")
    private String name;

    @NotBlank(message = "使用者帳號不可為空")
    @Size(min = 6, max = 30, message = "使用者帳號長度應該在6~20字元之間")
    private String userAccount;

    @NotBlank(message = "密碼不可為空")
    @Size(min = 6, max = 20, message = "密碼長度應該在6~20字元之間")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$", message = "密碼須包含至少一個大寫字母、一個小寫字母及一個數字")
    private String password;

    @NotBlank(message = "電話不可為空")
    @Pattern(regexp = "^09\\d{8}$", message = "請輸入正確的台灣手機號碼 (09開頭，共10碼數字)")
    private String tel;

    @NotBlank(message = "電子郵箱不可為空")
    @Email(message = "請輸入正確的電子郵箱地址")
    private String mail;

    @URL(message = "使用者頭像不是正確的圖片路徑")
    private String avatar;

    @Past(message = "生日必須為過去的日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @NotNull(message = "身分不可為空")
    private Role role;
}
