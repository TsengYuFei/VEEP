package com.example.api.Controller;

import com.example.api.DTO.Request.ExpoLogUpdateRequest;
import com.example.api.DTO.Request.UserUpdateRequest;
import com.example.api.DTO.Response.BoothOverviewResponse;
import com.example.api.DTO.Response.ExpoLogCreateResponse;
import com.example.api.DTO.Response.ExpoLogResponse;
import com.example.api.DTO.Response.UserDetailResponse;
import com.example.api.Service.ExpoLogService;
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

@Tag(name = "展會Log相關")
@RequestMapping("/log/expo")
@RestController
@RequiredArgsConstructor
public class ExpoLogController {
    @Autowired
    private final ExpoLogService expoLogService;



    @Operation(
            summary = "新增展會log",
            description = "進入展會時呼叫。可輸入欄位 1.使用者帳號 2.展會ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "成功新增展會Log",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExpoLogCreateResponse.class)
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
    @PostMapping("/create/{expoID}")
    public ResponseEntity<ExpoLogCreateResponse> createExpoLog(
            @Parameter(description = "展會ID", required = true)
            @PathVariable Integer expoID
    ){
        System.out.print("ExpoLogController: createExpoLog>> ");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userAccount = authentication.getName();
        System.out.println(userAccount);

        ExpoLogCreateResponse response = expoLogService.createExpoLog(expoID, userAccount);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Operation(
            summary = "獲取單筆展會log"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功取得單筆展會log",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExpoLogResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "找不到展會log資料"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @GetMapping("/{sessionID}")
    public ResponseEntity<ExpoLogResponse> getExpoLogBySessionID(
            @Parameter(description = "展會log的Session ID", required = true)
            @PathVariable String sessionID
    ) {
        System.out.println("ExpoLogController: getExpoLogBySessionID>> "+sessionID);
        ExpoLogResponse response = expoLogService.getExpoLogResponse(sessionID);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @Operation(
            summary = "獲取某展會的所有log",
            description = "目前無排序"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功獲取含該展會的所有log",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ExpoLogResponse.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @GetMapping("/all/{expoID}")
    public ResponseEntity<List<ExpoLogResponse>> getAllExpoLog(
            @Parameter(description = "展會ID", required = true)
            @PathVariable Integer expoID
    ){
        System.out.println("ExpoLogController: getAllExpoLog >> "+expoID);
        List<ExpoLogResponse> logs = expoLogService.getAllExpoLogResponse(expoID);
        return ResponseEntity.status(HttpStatus.OK).body(logs);
    }


    @Operation(
            summary = "更新展會log",
            description = "在展會中有任何點擊時呼叫。可針對exit at及last active at欄位傳入boolean值"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功更新展會log",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExpoLogResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "輸入格式錯誤"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "找不到展會log"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @PutMapping("/update/{sessionID}")
    public ResponseEntity<ExpoLogResponse> updateExpoLogBySessionID(
            @Parameter(description = "展會log的Session ID", required = true)
            @PathVariable String sessionID,
            @Valid @RequestBody ExpoLogUpdateRequest request){
        System.out.println("ExpoLogController: updateExpoLogBySessionID >> "+ sessionID);

        expoLogService.updateExpoLog(sessionID, request);
        ExpoLogResponse response = expoLogService.getExpoLogResponse(sessionID);
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
                    description = "找不到展會log"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @PreAuthorize("hasRole('FOUNDER') and (@expoSecurity.isOwner(#expoID) or @expoSecurity.isCollaborator(#expoID))")
    @DeleteMapping("/delete/{expoID}/{sessionID}")
    public ResponseEntity<?> deleteExpoLogBySessionID(
            @Parameter(description = "展會ID", required = true)
            @PathVariable Integer expoID,
            @Parameter(description = "展會log的Session ID", required = true)
            @PathVariable String sessionID
    ){
        System.out.println("ExpoLogController: deleteExpoLogBySessionID>> "+expoID+", "+sessionID);
        expoLogService.deleteExpoLogBySessionID(sessionID);
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
    @DeleteMapping("/delete/{expoID}")
    public ResponseEntity<?> deleteExpoLogByExponID(
            @Parameter(description = "展會ID", required = true)
            @PathVariable Integer expoID
    ){
        System.out.println("ExpoLogController: deleteExpoLogByExponID>> "+expoID);
        expoLogService.deleteExpoLogByExpoID(expoID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
