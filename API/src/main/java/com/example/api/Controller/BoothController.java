package com.example.api.Controller;

import com.example.api.DTO.BoothEditDTO;
import com.example.api.DTO.ExpoEditDTO;
import com.example.api.Dao.BoothDao;
import com.example.api.Request.BoothRequest;
import com.example.api.Request.ExpoRequest;
import com.example.api.Service.BoothService;
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

@Tag(name = "單一攤位相關")
@RequestMapping("/booth")
@RestController
public class BoothController {
    @Autowired
    private BoothService boothService;

    @Operation(
            summary = "獲取攤位資訊(編輯用)",
            description = "用於攤位編輯頁面。可獲取除了boothID外之所有欄位"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功取得攤位資訊(編輯用)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BoothEditDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "找不到攤位"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @GetMapping("/edit/{boothID}")
    public ResponseEntity<BoothEditDTO> getBoothEdit(
            @Parameter(description = "攤位ID", required = true)
            @PathVariable Integer boothID
    ){
        System.out.println("BoothController: getBoothEdit >> "+boothID);
        BoothEditDTO booth = boothService.getBoothByID(boothID);
        return ResponseEntity.status(HttpStatus.OK).body(booth);
    }


    @Operation(
            summary = "新增攤位",
            description = "可輸入欄位 1.攤位名稱 2.圖像URL 3.介紹 4.開放模式 5.開放狀態 6.開始時間 7.結束時間 8.同時間最大參與人數 9.是否顯示於攤位總覽頁面"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "成功新增攤位",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BoothEditDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @PostMapping("")
    public ResponseEntity<BoothEditDTO> createBooth(@Valid @RequestBody BoothRequest boothRequest){
        System.out.println("BoothController: createBooth");
        System.out.println(boothRequest);
        Integer boothID = boothService.createBooth(boothRequest);
        BoothEditDTO booth = boothService.getBoothByID(boothID);
        return ResponseEntity.status(HttpStatus.CREATED).body(booth);
    }

    @Operation(
            summary = "更新攤位",
            description = "用於攤位資料更新頁面。可更新除了boothID外之所有欄位"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功更新攤位資訊",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BoothEditDTO.class)
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
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @PutMapping("/{boothID}")
    public ResponseEntity<BoothEditDTO> updateBoothByID(
            @Parameter(description = "攤位ID", required = true)
            @PathVariable Integer boothID,
            @Valid @RequestBody BoothRequest boothRequest
    ){
        System.out.println("BoothController: updateBoothByID >> "+boothID);
        boothService.updateBoothByID(boothID, boothRequest);
        BoothEditDTO booth = boothService.getBoothByID(boothID);
        return ResponseEntity.status(HttpStatus.OK).body(booth);
    }
}
