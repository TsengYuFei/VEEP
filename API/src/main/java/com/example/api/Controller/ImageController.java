package com.example.api.Controller;

import com.example.api.Service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Tag(name = "單一圖片相關")
@RequestMapping("/image")
@RestController
@RequiredArgsConstructor
public class ImageController {
    @Autowired
    private final ImageService imageService;


    @Operation(
            summary = "上傳圖片",
            description = "from使用者本機"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功上傳"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "未獲取到圖片"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadImage(
            @Parameter(description = "圖片檔案", required = true)
            @RequestParam MultipartFile file
    ){
        System.out.println("ImageController: uploadImage");
        Map<String, String> response = imageService.uploadImage(file);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
