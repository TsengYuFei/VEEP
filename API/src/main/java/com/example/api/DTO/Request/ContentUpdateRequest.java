package com.example.api.DTO.Request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ContentUpdateRequest {
    @Size(max = 100, message = "攤位內容標題不可超過100字元")
    private String title;

    private String content;

    private String image;
}
