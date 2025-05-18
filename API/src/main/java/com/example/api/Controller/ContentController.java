package com.example.api.Controller;

import com.example.api.DTO.Request.ContentUpdateRequest;
import com.example.api.DTO.Response.ContentEditResponse;
import com.example.api.Service.ContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "單一攤位內容相關", description = "每個攤位固定有6個內容，創建booth時會自動建立")
@RequestMapping("/content")
@RestController
@RequiredArgsConstructor
public class ContentController {
    @Autowired
    private final ContentService contentService;



    @Operation(
            summary = "獲取攤位內容(編輯用)",
            description = "用於攤位內容編輯頁面"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功取得攤位內容(編輯用)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ContentEditResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "找不到攤位"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "找不到內容編號"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @GetMapping("/edit/{boothID}/{number}")
    public ResponseEntity<ContentEditResponse> getContentEditByBoothIDAndNumber(
            @Parameter(description = "攤位ID", required = true)
            @PathVariable Integer boothID,
            @Parameter(description = "內容編號", required = true)
            @PathVariable Integer number
    ){
        System.out.println("ContentController: getContentEditByBoothIDAndNumber >> "+boothID+", "+number);
        ContentEditResponse content = contentService.getContentEditByBoothIDAndNumber(boothID, number);
        return ResponseEntity.status(HttpStatus.OK).body(content);
    }


    @Operation(
            summary = "更新攤位內容",
            description = "用於攤位內容更新頁面。"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功更新攤位內容",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ContentEditResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "輸入格式錯誤"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "找不到攤位"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "找不到攤位內容編號"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @PutMapping("/{boothID}/{number}")
    public ResponseEntity<ContentEditResponse> updateContentEditByBoothIDAndNumber(
            @Parameter(description = "攤位ID", required = true)
            @PathVariable Integer boothID,
            @Parameter(description = "內容編號", required = true)
            @PathVariable Integer number,
            @Valid @RequestBody ContentUpdateRequest contentRequest
    ){
        System.out.println("ContentController: updateContentEditByBoothIDAndNumber >> "+boothID+", "+number);
        contentService.updateContentByBoothIDAndNumber(boothID, number, contentRequest);
        ContentEditResponse content = contentService.getContentEditByBoothIDAndNumber(boothID, number);
        return ResponseEntity.status(HttpStatus.OK).body(content);
    }
}
