package com.example.api.Model;


import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;

@Data
@Component
public class UserModel {
    private String name;
    private String userAccount;
    private String password;
    private String tel;
    private String mail;
    private String avatar;
    private LocalDate birthday;
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

    private Role role;
}
