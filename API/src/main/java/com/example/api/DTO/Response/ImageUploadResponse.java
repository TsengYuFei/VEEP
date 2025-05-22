package com.example.api.DTO.Response;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class ImageUploadResponse {
    private String name;
    private String url;
}
