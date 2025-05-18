package com.example.api.Controller;

import com.example.api.DTO.Request.ExpoUpdateRequest;
import com.example.api.DTO.Response.ExpoEditResponse;
import com.example.api.DTO.Request.ExpoCreateRequest;
import com.example.api.Service.ExpoService;
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

@Tag(name = "單一展會相關")
@RequestMapping("/expo")
@RestController
@RequiredArgsConstructor
public class ExpoController {
    @Autowired
    private final ExpoService expoService;


    @Operation(
            summary = "獲取展會資訊(編輯用)",
            description = "用於展會編輯頁面。"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功取得展會資訊(編輯用)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExpoEditResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "找不到展會"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @GetMapping("/edit/{expoID}")
    public ResponseEntity<ExpoEditResponse> getExpoEditByID(
            @Parameter(description = "展會ID", required = true)
            @PathVariable Integer expoID
    ){
        System.out.println("ExpoController: getExpoEdit >> "+expoID);
        ExpoEditResponse expo = expoService.getExpoEditByID(expoID);
        return ResponseEntity.status(HttpStatus.OK).body(expo);
    }


    @Operation(
            summary = "新增展會",
            description = "可輸入欄位 1.展會擁有者的使用者帳號 2.展會名稱 3.圖像URL 4.價錢 5.介紹 " +
                    "6.開放模式 7.開放狀態 8.開始時間 9.結束時間 10.驗證碼 11.同時間最大參與人數 " +
                    "12.是否顯示於展會總覽頁面 13.合作者(數個) 14.白名單(數個) 15.黑名單(數個)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "成功新增展會",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExpoEditResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @PostMapping("/{userAccount}")
    public ResponseEntity<ExpoEditResponse> createExpo(
            @Parameter(description = "展會擁有者的userAccount", required = true)
            @PathVariable String userAccount,
            @Valid @RequestBody ExpoCreateRequest expoCreateRequest
    ){
        System.out.println("ExpoController: createExpo >> "+ userAccount);
        Integer expoID = expoService.createExpo(userAccount, expoCreateRequest);
        ExpoEditResponse expo = expoService.getExpoEditByID(expoID);
        return ResponseEntity.status(HttpStatus.CREATED).body(expo);
    }


    @Operation(
            summary = "更新展會",
            description = "用於展會資料更新頁面。可更新除了expoID、ownerAccount外之所有欄位"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功更新展會資訊",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExpoEditResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "輸入格式錯誤"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "找不到展會"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @PutMapping("/{expoID}")
    public ResponseEntity<ExpoEditResponse> updateExpoByID(
            @Parameter(description = "展會ID", required = true)
            @PathVariable Integer expoID,
            @Valid @RequestBody ExpoUpdateRequest expoRequest
    ){
        System.out.println("ExpoController: updateExpoByID >> "+expoID);
        expoService.updateExpoByID(expoID, expoRequest);
        ExpoEditResponse expo = expoService.getExpoEditByID(expoID);
        return ResponseEntity.status(HttpStatus.OK).body(expo);
    }


    @Operation(summary = "刪除展會")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "成功刪除展會"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "找不到展會"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @DeleteMapping("/{expoID}")
    public ResponseEntity<?> deleteExpoByID(
            @Parameter(description = "展會ID", required = true)
            @PathVariable Integer expoID
    ){
        System.out.println("ExpoController: deleteExpoByID >> "+expoID);
        expoService.deleteExpoByID(expoID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
