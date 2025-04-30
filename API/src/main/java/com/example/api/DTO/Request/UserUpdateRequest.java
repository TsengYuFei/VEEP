package com.example.api.DTO.Request;

import com.example.api.Entity.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;

@Data
public class UserUpdateRequest {
    @NotBlank(message = "使用者名稱不可為空")
    @Size(min = 1, max = 20, message = "使用者名稱長度應該在1~10字元之間")
    private String name;

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

    private String bio;

    @URL(message = "使用者背景圖片不是正確的圖片路徑")
    private String background;

    @NotNull(message = "顯示追蹤者不可為空")
    private Boolean showFollowers;

    @NotNull(message = "顯示追蹤中不可為空")
    private Boolean showFollowing;

    @NotNull(message = "顯示參與紀錄不可為空")
    private Boolean showHistory;

    @NotNull(message = "顯示進行中的持有展會不可為空")
    private Boolean showCurrentExpo;

    @NotNull(message = "顯示進行中的持有攤位不可為空")
    private Boolean showCurrentBooth;

    @NotNull(message = "使用者身分不可為空")
    private Role role;
}
