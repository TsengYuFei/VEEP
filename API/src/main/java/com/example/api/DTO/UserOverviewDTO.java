package com.example.api.DTO;

import com.example.api.Model.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.springframework.stereotype.Component;

@Data
@Component
public class UserOverviewDTO {
    @NotBlank(message = "使用者名稱不可為空")
    @Size(min = 1, max = 10, message = "使用者名稱長度應該在1~10字元之間")
    private String name;

    @NotBlank(message = "使用者帳號不可為空")
    @Size(min = 6, max = 20, message = "使用者帳號長度應該在6~20字元之間")
    private String userAccount;

    @URL(message = "不是正確的圖片路徑")
    private String avatar;

    @NotNull(message = "身分不可為空")
    private Role role;
}
