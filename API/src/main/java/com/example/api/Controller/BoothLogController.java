package com.example.api.Controller;

import com.example.api.DTO.Request.BoothLogUpdateRequest;
import com.example.api.DTO.Request.ExpoLogUpdateRequest;
import com.example.api.DTO.Response.BoothLogResponse;
import com.example.api.DTO.Response.ExpoLogResponse;
import com.example.api.DTO.Response.LogCreateResponse;
import com.example.api.Service.BoothLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "攤位Log相關")
@RequestMapping("/log/booth")
@RestController
@RequiredArgsConstructor
public class BoothLogController {
    @Autowired
    private final BoothLogService boothLogService;



    @Operation(
            summary = "新增攤位log",
            description = "進入攤位時呼叫。可輸入欄位 1.使用者帳號 2.展會ID 3.攤位ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "成功新增攤位Log",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LogCreateResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "找不到展會"
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
    @PostMapping("/create/{expoID}/{boothID}")
    public ResponseEntity<LogCreateResponse> createBoothLog(
            @Parameter(description = "展會ID", required = true)
            @PathVariable Integer expoID,
            @Parameter(description = "攤位ID", required = true)
            @PathVariable Integer boothID
    ){
        System.out.print("BoothLogController: createBoothLog>> ");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userAccount = authentication.getName();
        System.out.println(userAccount);

        LogCreateResponse response = boothLogService.createBoothLog(expoID, boothID, userAccount);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Operation(
            summary = "獲取單筆攤位log"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功取得單筆攤位og",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BoothLogResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "找不到攤位log資料"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @GetMapping("/{sessionID}")
    public ResponseEntity<BoothLogResponse> getBoothLogBySessionID(
            @Parameter(description = "攤位log的Session ID", required = true)
            @PathVariable String sessionID
    ) {
        System.out.println("BoothLogController: getBoothLogBySessionID>> "+sessionID);
        BoothLogResponse response = boothLogService.getBoothLogResponse(sessionID);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @Operation(
            summary = "獲取某展會的所有攤位log",
            description = "目前無排序"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功獲取含該展會的所有log",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = BoothLogResponse.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @GetMapping("/all/by_expo/{expoID}")
    public ResponseEntity<List<BoothLogResponse>> getAllBoothLogByExpoID(
            @Parameter(description = "展會ID", required = true)
            @PathVariable Integer expoID
    ){
        System.out.println("BoothLogController: getAllBoothLogByExpoID >> "+expoID);
        List<BoothLogResponse> logs = boothLogService.getAllBoothLogResponseByExpoID(expoID);
        return ResponseEntity.status(HttpStatus.OK).body(logs);
    }


    @Operation(
            summary = "獲取某攤位的所有攤位log",
            description = "目前無排序"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功獲取含該攤位的所有log",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = BoothLogResponse.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @GetMapping("/all/by_booth/{boothID}")
    public ResponseEntity<List<BoothLogResponse>> getAllBoothLogByBoothID(
            @Parameter(description = "攤位ID", required = true)
            @PathVariable Integer boothID
    ){
        System.out.println("BoothLogController: getAllBoothLogByBoothID >> "+boothID);
        List<BoothLogResponse> logs = boothLogService.getAllBoothLogResponseByBoothID(boothID);
        return ResponseEntity.status(HttpStatus.OK).body(logs);
    }


    @Operation(
            summary = "更新攤位log",
            description = "在攤位中有任何點擊時呼叫。可針對exit at及last active at欄位傳入boolean值"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功更新攤位log",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BoothLogResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "輸入格式錯誤"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "找不到攤位log"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @PutMapping("/update/{sessionID}")
    public ResponseEntity<BoothLogResponse> updateExpoLogBySessionID(
            @Parameter(description = "攤位log的Session ID", required = true)
            @PathVariable String sessionID,
            @Valid @RequestBody BoothLogUpdateRequest request){
        System.out.println("BoothLogController: updateExpoLogBySessionID >> "+ sessionID);

        boothLogService.updateBoothLog(sessionID, request);
        BoothLogResponse response = boothLogService.getBoothLogResponse(sessionID);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @Operation(summary = "刪除單筆log")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "成功刪除單筆log"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "找不到攤位log"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @PreAuthorize("hasRole('FOUNDER')")
    @DeleteMapping("/delete/{sessionID}")
    public ResponseEntity<?> deleteBoothLogBySessionID(
            @Parameter(description = "攤位log的Session ID", required = true)
            @PathVariable String sessionID
    ){
        System.out.println("BoothLogController: deleteBoothLogBySessionID>> "+sessionID);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userAccount = authentication.getName();

        boothLogService.deleteBoothLogBySessionID(sessionID, userAccount);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @Operation(summary = "刪除某展會的所有log")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "成功刪除某展會的所有log"
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
    @DeleteMapping("/delete/all/by_expo/{expoID}")
    public ResponseEntity<?> deleteBoothLogByExpoID(
            @Parameter(description = "展會ID", required = true)
            @PathVariable Integer expoID
    ){
        System.out.println("BoothLogController: deleteBoothLogByExpoID>> "+expoID);
        boothLogService.deleteBoothLogByExpoID(expoID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @Operation(summary = "刪除某攤位的所有log")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "成功刪除某攤位的所有log"
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
    @DeleteMapping("/delete/all/by_booth/{boothID}")
    public ResponseEntity<?> deleteBoothLogByBoothID(
            @Parameter(description = "攤位ID", required = true)
            @PathVariable Integer boothID
    ){
        System.out.println("BoothLogController: deleteBoothLogByBoothID>> "+boothID);
        boothLogService.deleteBoothLogByBoothID(boothID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
