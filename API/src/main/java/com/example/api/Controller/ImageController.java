package com.example.api.Controller;

import com.example.api.DTO.Response.ImageUploadResponse;
import com.example.api.Service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "單一圖片相關")
@RequestMapping("/image")
@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;



    @Operation(
            summary = "上傳圖片",
            description = "from使用者本機，目前將上傳的圖片存在本地端(VEEP/uploads資料夾中)。" +
                    "一些限制: 5MB   .jpg or .png"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功上傳",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ImageUploadResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "未獲取到圖片"
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "圖片大小不得超過 5MB"
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "圖片沒有副檔名"
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "只能上傳 .jpg 或 .png 類型的圖片"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageUploadResponse> uploadImage(
            @Parameter(description = "圖片檔案", required = true)
            @RequestParam MultipartFile image
    ){
        System.out.println("ImageController: uploadImage");
        ImageUploadResponse response = imageService.uploadImage(image);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @Operation(summary = "刪除圖片")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "成功刪除圖片"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "找不到圖片"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @DeleteMapping("")
    public ResponseEntity<?> deleteImageByName(
            @Parameter(description = "圖片名稱", required = true)
            @RequestParam String imageName
    ) {
        System.out.println("ImageController: deleteImageByName  >>"+imageName);
        imageService.deleteImageByName(imageName);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
