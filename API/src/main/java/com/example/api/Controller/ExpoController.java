package com.example.api.Controller;

import com.example.api.DTO.ExpoEditDTO;
import com.example.api.Service.ExpoService;
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

@Tag(name = "單一展會相關", description = "")
@RequestMapping("/expo")
@RestController
public class ExpoController {
    @Autowired
    private ExpoService expoService;

    @Operation(
            summary = "獲取展會資訊(編輯用)",
            description = "用於展會編輯頁面。可獲取除了expoID及link外之所有欄位"
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
    @GetMapping("/{expoID}/edit")
    public ResponseEntity<ExpoEditDTO> getExpoEdit(
            @Parameter(description = "展會ID", required = true)
            @PathVariable int expoID
    ){
        System.out.println("ExpoController: getExpoEdit >> "+expoID);
        ExpoEditDTO expo = expoService.getExpoEditByID(expoID);
        return ResponseEntity.status(HttpStatus.OK).body(expo);
    }
}
