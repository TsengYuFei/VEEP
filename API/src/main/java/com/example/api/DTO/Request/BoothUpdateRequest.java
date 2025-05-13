package com.example.api.DTO.Request;

import com.example.api.Entity.OpenMode;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BoothUpdateRequest {
    @Size(min = 1, max = 30, message = "攤位名稱長度應在1~30字元之間")
    private String name;

    @URL(message = "攤位圖像不是正確的圖片路徑")
    private String avatar;

    private String introduction;

    private List<String> tags;

    private OpenMode openMode;

    private Boolean openStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime openStart;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime openEnd;

    @Min(value = 1, message = "攤位同時間最大參與人數至少需一人")
    @Max(value = 500, message = "攤位同時間最大參與人數至多500人")
    private Integer maxParticipants;

    private Boolean display;

    private List<String> collaborators;

    private List<String> staffs;
}
