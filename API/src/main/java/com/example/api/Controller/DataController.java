package com.example.api.Controller;

import com.example.api.DTO.Response.DataResponse;
import com.example.api.DTO.Response.ExpoGroupMessageResponse;
import com.example.api.DTO.Response.ExpoLogResponse;
import com.example.api.Service.DataService;
import com.example.api.Service.ExpoLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Tag(name = "數據分析相關")
@RequestMapping("/data")
@RestController
@RequiredArgsConstructor
public class DataController {
    private final DataService dataService;



    @Operation(summary = "獲取expo數據分析")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功取得expo數據分析",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DataResponse.class)
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
    @PreAuthorize("hasRole('FOUNDER') and (@expoSecurity.isOwner(#expoID) or @expoSecurity.isCollaborator(#expoID))")
    @GetMapping("/expo/{expoID}")
    public ResponseEntity<DataResponse> getExpoData(
            @Parameter(description = "展會ID", required = true)
            @PathVariable Integer expoID
    ){
        System.out.println("DataController: getExpoData >> "+expoID);
        DataResponse response = dataService.expoDataAnalysis(expoID);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "獲取booth數據分析")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功取得booth數據分析",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DataResponse.class)
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
    @PreAuthorize("hasRole('FOUNDER') and (@boothSecurity.isOwner(#boothID) or @boothSecurity.isCollaborator(#boothID))")
    @GetMapping("/booth/{boothID}")
    public ResponseEntity<DataResponse> getBoothData(
            @Parameter(description = "攤位ID", required = true)
            @PathVariable Integer boothID
    ){
        System.out.println("DataController: getBoothData >> "+boothID);
        DataResponse response = dataService.boothDataAnalysis(boothID);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
