package com.example.api.DTO.Request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ContentUpdateRequest {
    @Size(max = 100, message = "The content title should not exceed 100 characters.")
    private String title;

    private String content;

    private String image;
}
