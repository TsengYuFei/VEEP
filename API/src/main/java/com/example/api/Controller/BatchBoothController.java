package com.example.api.Controller;

import com.example.api.DTO.Response.BoothOverviewResponse;
import com.example.api.Service.BatchBoothService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "批量攤位相關")
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
                                    schema = @Schema(implementation = BoothOverviewResponse.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @GetMapping("/overview")
    public ResponseEntity<List<BoothOverviewResponse>> getAllBoothOverview(){
        System.out.println("BatchBoothController: getAllBoothOverview");
        List<BoothOverviewResponse> booths = batchBoothService.getAllBoothOverview();
        return ResponseEntity.status(HttpStatus.OK).body(booths);
    }

    @Operation(
            summary = "獲取所有攤位(概略)-分頁版"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功取得所有攤位(概略)-分頁版",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = BoothOverviewResponse.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @GetMapping("/overview/page")
    public ResponseEntity<Page<BoothOverviewResponse>> getAllBoothOverviewPage(
            @Parameter(description = "頁數(第幾頁)", required = true)
            @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "數量(一頁幾筆資料)", required = true)
            @RequestParam(defaultValue = "5") Integer size
    ){
        System.out.println("BatchBoothController: getAllBoothOverviewPage");
        Page<BoothOverviewResponse> booths = batchBoothService.getAllBoothOverviewPage(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(booths);
    }
}
