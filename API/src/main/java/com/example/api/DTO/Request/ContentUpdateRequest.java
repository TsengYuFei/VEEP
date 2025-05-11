package com.example.api.DTO.Request;

import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class ContentUpdateRequest {
    @Size(max = 100, message = "攤位內容標題不可超過100字元")
    private String title;

    private String content;

    @URL(message = "攤位內容圖像不是正確的圖片路徑")
    private String image;
}
