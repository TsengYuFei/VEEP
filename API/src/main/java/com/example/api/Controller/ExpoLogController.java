package com.example.api.Controller;

import com.example.api.DTO.Response.ExpoLogCreateResponse;
import com.example.api.DTO.Response.ExpoLogResponse;
import com.example.api.DTO.Response.UserDetailResponse;
import com.example.api.DTO.Response.UserEditResponse;
import com.example.api.Service.ExpoLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "展會Log相關")
@RequestMapping("/log/expo")
@RestController
@RequiredArgsConstructor
public class ExpoLogController {
    @Autowired
    private final ExpoLogService expoLogService;



    @Operation(
            summary = "新增展會log",
            description = "可輸入欄位 1.使用者帳號 2.展會ID"
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


    @Operation(summary = "刪除展會log")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "成功刪除展會log"
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
    @DeleteMapping("/delete/{sessionID}")
    public ResponseEntity<?> deleteExpoLogBySessionID(
            @Parameter(description = "展會ID", required = true)
            @PathVariable Integer expoID,
            @Parameter(description = "展會log的Session ID", required = true)
            @PathVariable String sessionID
    ){
        System.out.println("ExpoLogController: deleteExpoLogBySessionID>> "+sessionID);
        expoLogService.deleteExpoLog(sessionID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
