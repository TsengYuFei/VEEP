package com.example.api.Model;


import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import java.util.Date;

@Data
@Component
public class User {
    @NotBlank(message = "使用者名稱不可為空")
    @Size(min = 1, max = 10, message = "使用者名稱長度應該在1~10字元之間")
    private String name;

    @NotBlank(message = "使用者帳號不可為空")
    @Size(min = 6, max = 20, message = "使用者帳號長度應該在6~20字元之間")
    private String userAccount;

    @NotBlank(message = "密碼不可為空")
    @Size(min = 6, max = 20, message = "密碼長度應該在6~20字元之間")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d).+$", message = "密碼須包含至少一個大寫字母、一個小寫字母及一個數字")
    private String password;

    @NotBlank(message = "電話不可為空")
    @Pattern(regexp = "^09\\d{8}$", message = "請輸入正確的台灣手機號碼 (09開頭，共10碼數字)")
    private String tel;

    @NotBlank(message = "電子郵箱不可為空")
    @Email(message = "請輸入正確的電子郵箱地址")
    private String mail;

    @URL(message = "不是正確的圖片路徑")
    private String avatar;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    private String bio;

    @URL(message = "不是正確的圖片路徑")
    private String background;

    @NotNull(message = "追蹤者不可為空")
    @PositiveOrZero(message = "追蹤者不可為負數")
    private Integer followers;

    @NotNull(message = "追蹤中不可為空")
    @PositiveOrZero(message = "追蹤中不可為負數")
    private Integer following;

    @NotNull(message = "顯示追蹤者不可為空")
    private boolean showFollowers;

    @NotNull(message = "顯示追蹤中不可為空")
    private boolean showFollowing;

    @NotNull(message = "顯示參與紀錄不可為空")
    private boolean showHistory;

    @NotNull(message = "顯示進行中的持有展會不可為空")
    private boolean showCurrentExpo;

    @NotNull(message = "顯示進行中的持有攤位不可為空")
    private boolean showCurrentBooth;

    @NotNull(message = "身分不可為空")
    private Role role;
}
