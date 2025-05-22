package com.example.api.DTO.Request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;

@Data
public class UserUpdateRequest {
    @Size(min = 1, max = 20, message = "使用者名稱長度應該在1~10字元之間")
    private String name;

    @Pattern(regexp = "^09\\d{8}$", message = "請輸入正確的台灣手機號碼 (09開頭，共10碼數字)")
    private String tel;

    @Email(message = "請輸入正確的電子郵箱地址")
    private String mail;

    private String avatar;

    @Past(message = "生日必須為過去的日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    private String bio;

    @URL(message = "使用者背景圖片不是正確的圖片路徑")
    private String background;

    private Boolean showFollowers;

    private Boolean showFollowing;

    private Boolean showHistory;

    private Boolean showCurrentExpo;

    private Boolean showCurrentBooth;
}
