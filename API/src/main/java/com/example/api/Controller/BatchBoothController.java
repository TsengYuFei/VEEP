package com.example.api.Controller;

import com.example.api.DTO.Response.BoothOverviewReaponse;
import com.example.api.Service.BatchBoothService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "批量攤位相關", description = "")
@RequestMapping("/batch/booth")
@RestController
@RequiredArgsConstructor
public class BatchBoothController {
    @Autowired
    private final BatchBoothService batchBoothService;



    @Operation(
            summary = "獲取所有攤位(概略)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功取得所有攤位(概略)",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = BoothOverviewReaponse.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @GetMapping("/overview")
    public ResponseEntity<List<BoothOverviewReaponse>> getAllBoothOverview(){
        System.out.println("BatchBoothController: getAllBoothOverview");
        List<BoothOverviewReaponse> booths = batchBoothService.getAllBoothOverview();
        return ResponseEntity.status(HttpStatus.OK).body(booths);
    }
}
