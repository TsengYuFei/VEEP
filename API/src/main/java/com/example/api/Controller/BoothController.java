package com.example.api.Controller;

import com.example.api.DTO.BoothEditDTO;
import com.example.api.DTO.ExpoEditDTO;
import com.example.api.Dao.BoothDao;
import com.example.api.Service.BoothService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        System.out.println("BoothController: getExpoEdit >> "+boothID);
        BoothEditDTO booth = boothService.getBoothByID(boothID);
        return ResponseEntity.status(HttpStatus.OK).body(booth);
    }
}
