package com.example.api.DTO;

import com.example.api.Model.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.springframework.stereotype.Component;

@Data
@Component
public class UserDetailDTO {
    @NotBlank(message = "使用者名稱不可為空")
    @Size(min = 1, max = 10, message = "使用者名稱長度應該在1~10字元之間")
    private String name;

    @NotBlank(message = "使用者帳號不可為空")
    @Size(min = 6, max = 20, message = "使用者帳號長度應該在6~20字元之間")
    private String userAccount;

    @URL(message = "不是正確的圖片路徑")
    private String avatar;

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