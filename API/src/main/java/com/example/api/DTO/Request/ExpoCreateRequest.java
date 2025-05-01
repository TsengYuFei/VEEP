package com.example.api.DTO.Request;

import com.example.api.Entity.OpenMode;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

@Data
public class ExpoCreateRequest {
    @Size(min = 1, max = 30, message = "展會名稱長度應在1~30字元之間")
    private String name;

    @URL(message = "展會圖像不是正確的圖片路徑")
    private String avatar;

    @NotNull(message = "展會價錢不可為空")
    @PositiveOrZero(message = "展會價錢必須為非負整數")
    private Integer price;

    private String introduction;

    @NotNull(message = "展會開放模式不可為空")
    private OpenMode openMode;

    private Boolean openStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime openStart;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime openEnd;

    private String accessCode;

    @NotNull(message = "展會同時間最大參與人數不可為空")
    @Min(value = 1, message = "展會同時間最大參與人數至少需一人")
    @Max(value = 1500, message = "展會同時間最大參與人數至多1500人")
    private Integer maxParticipants;

    @NotNull(message = "展會是否顯示於總覽頁面不可為空")
    private Boolean display;
}
