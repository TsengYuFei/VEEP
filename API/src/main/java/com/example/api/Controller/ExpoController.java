package com.example.api.Controller;

import com.example.api.DTO.ExpoEditDTO;
import com.example.api.DTO.UserDetailDTO;
import com.example.api.Request.ExpoRequest;
import com.example.api.Service.ExpoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "單一展會相關", description = "")
@RequestMapping("/expo")
@RestController
public class ExpoController {
    @Autowired
    private ExpoService expoService;

    @Operation(
            summary = "獲取展會資訊(編輯用)",
            description = "用於展會編輯頁面。可獲取除了expoID外之所有欄位"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功取得展會資訊(編輯用)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExpoEditDTO.class)
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
    public ResponseEntity<ExpoEditDTO> getExpoEdit(
            @Parameter(description = "展會ID", required = true)
            @PathVariable Integer expoID
    ){
        System.out.println("ExpoController: getExpoEdit >> "+expoID);
        ExpoEditDTO expo = expoService.getExpoEditByID(expoID);
        return ResponseEntity.status(HttpStatus.OK).body(expo);
    }

    @Operation(
            summary = "新增展會",
            description = "可輸入欄位 1.展會名稱 2.圖像URL 3.價錢 4.介紹 5.開放模式 6.開放狀態 7.開始時間 8.結束時間 9.驗證碼 10.同時間最大參與人數 11.是否顯示於展會總覽頁面"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "成功新增展會",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExpoEditDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @PostMapping("")
    public ResponseEntity<ExpoEditDTO> createExpo(@Valid @RequestBody ExpoRequest expoRequest){
        System.out.println("ExpoController: createExpo");
        System.out.println(expoRequest);
        Integer expoID = expoService.createExpo(expoRequest);
        ExpoEditDTO expo = expoService.getExpoEditByID(expoID);
        return ResponseEntity.status(HttpStatus.CREATED).body(expo);
    }


    @Operation(
            summary = "更新展會",
            description = "用於展會資料更新頁面。可更新除了expoID外之所有欄位"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功更新展會資訊",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExpoEditDTO.class)
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
    public ResponseEntity<ExpoEditDTO> updateExpoByID(
            @Parameter(description = "展會ID", required = true)
            @PathVariable Integer expoID,
            @Valid @RequestBody ExpoRequest expoRequest
    ){
        System.out.println("ExpoController: updateExpoByID >> "+expoID);
        expoService.updateExpoByID(expoID, expoRequest);
        ExpoEditDTO expo = expoService.getExpoEditByID(expoID);
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
    public ResponseEntity<?> deleteExpo(
            @Parameter(description = "展會ID", required = true)
            @PathVariable Integer expoID
    ){
        System.out.println("ExpoController: deleteExpo >> "+expoID);
        expoService.deleteExpoByID(expoID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
