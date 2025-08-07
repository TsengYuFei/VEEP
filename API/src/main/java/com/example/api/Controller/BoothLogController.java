package com.example.api.Controller;

import com.example.api.DTO.Request.BoothLogUpdateRequest;
import com.example.api.DTO.Response.BoothLogResponse;
import com.example.api.DTO.Response.ExpoEnterResponse;
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
                            schema = @Schema(implementation = ExpoEnterResponse.class)
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
    @PostMapping("/create/{boothID}")
    public ResponseEntity<ExpoEnterResponse> createBoothLog(
            @Parameter(description = "攤位ID", required = true)
            @PathVariable Integer boothID
    ){
        System.out.print("BoothLogController: createBoothLog>> ");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userAccount = authentication.getName();
        System.out.println(userAccount);

        ExpoEnterResponse response = boothLogService.createBoothLog(boothID, userAccount);
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
                    description = "成功獲取含該展會的所有攤位log",
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
                    description = "成功獲取含該攤位的所有攤位log",
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
            summary = "獲取某使用者的所有攤位log",
            description = "依大->小排序"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功獲取該使用者的所有攤位log",
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
    @GetMapping("/user/{userAccount}")
    public ResponseEntity<List<BoothLogResponse>> getBoothLogByUserAccount(
            @Parameter(description = "使用者帳號", required = true)
            @PathVariable String userAccount
    ){
        System.out.println("BoothLogController: getBoothLogByUserAccount >> "+userAccount);
        List<BoothLogResponse> logs = boothLogService.getBoothLogResponseByUserAccount(userAccount);
        return ResponseEntity.status(HttpStatus.OK).body(logs);
    }


    @Operation(
            summary = "獲取某使用者在某展會的所有攤位log",
            description = "依大->小排序"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功獲取該使用者含該展會的所有攤位log",
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
    @GetMapping("/user/by_expo/{expoID}/{userAccount}")
    public ResponseEntity<List<BoothLogResponse>> getBoothLogByUserAccountAndExpoID(
            @Parameter(description = "展會ID", required = true)
            @PathVariable Integer expoID,
            @Parameter(description = "使用者帳號", required = true)
            @PathVariable String userAccount
    ){
        System.out.println("BoothLogController: getBoothLogByUserAccountAndExpoID >> "+userAccount+", "+expoID);
        List<BoothLogResponse> logs = boothLogService.getBoothLogResponseByUserAccountAndExpoID(expoID, userAccount);
        return ResponseEntity.status(HttpStatus.OK).body(logs);
    }


    @Operation(
            summary = "獲取某使用者在某攤位的所有攤位log",
            description = "依大->小排序"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功獲取該使用者含該攤位的所有攤位log",
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
    @GetMapping("/user/by_booth/{boothID}/{userAccount}")
    public ResponseEntity<List<BoothLogResponse>> getBoothLogByUserAccountAndBoothID(
            @Parameter(description = "攤位ID", required = true)
            @PathVariable Integer boothID,
            @Parameter(description = "使用者帳號", required = true)
            @PathVariable String userAccount
    ){
        System.out.println("BoothLogController: getBoothLogByUserAccountAndBoothID >> "+userAccount+", "+boothID);
        List<BoothLogResponse> logs = boothLogService.getBoothLogResponseByUserAccountAndBoothID(boothID, userAccount);
        return ResponseEntity.status(HttpStatus.OK).body(logs);
    }


    @Operation(
            summary = "獲取某展會現正進行中(目前在場)的攤位log",
            description = "目前無排序"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功獲取該展會現正進行中(目前在場)的所有攤位log",
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
    @GetMapping("/online/by_expo/{expoID}")
    public ResponseEntity<List<BoothLogResponse>> getOnlineBoothLogByExpoID(
            @Parameter(description = "展會ID", required = true)
            @PathVariable Integer expoID
    ){
        System.out.println("BoothLogController: getOnlineBoothLogByExpoID >> "+expoID);
        List<BoothLogResponse> logs = boothLogService.getOnlineBoothLogResponseByExpoID(expoID);
        return ResponseEntity.status(HttpStatus.OK).body(logs);
    }


    @Operation(
            summary = "獲取某攤位現正進行中(目前在場)的攤位log",
            description = "目前無排序"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功獲取該攤位現正進行中(目前在場)的所有攤位log",
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
    @GetMapping("/online/by_booth/{boothID}")
    public ResponseEntity<List<BoothLogResponse>> getOnlineBoothLogByBoothID(
            @Parameter(description = "攤位ID", required = true)
            @PathVariable Integer boothID
    ){
        System.out.println("BoothLogController: getOnlineBoothLogByBoothID >> "+boothID);
        List<BoothLogResponse> logs = boothLogService.getOnlineBoothLogResponseByBoothID(boothID);
        return ResponseEntity.status(HttpStatus.OK).body(logs);
    }


    @Operation(
            summary = "獲取某攤位目前的在場人數"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功獲取該攤位目前的在場人數",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = Integer.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @GetMapping("/online/number/{boothID}")
    public ResponseEntity<Integer> getOnlineNumberByBoothID(
            @Parameter(description = "攤位ID", required = true)
            @PathVariable Integer boothID
    ){
        System.out.println("BoothLogController: getOnlineNumberByBoothID >> "+boothID);
        Integer number = boothLogService.getOnlineNumberByBoothID(boothID);
        return ResponseEntity.status(HttpStatus.OK).body(number);
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


    @Operation(summary = "刪除單筆攤位log")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "成功刪除單筆攤位log"
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
        System.out.println("BoothLogController: deleteBoothLogBySessionID >> "+sessionID);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userAccount = authentication.getName();

        boothLogService.deleteBoothLogBySessionID(sessionID, userAccount);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @Operation(summary = "刪除某展會的所有攤位log")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "成功刪除某展會的所有攤位log"
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
        System.out.println("BoothLogController: deleteBoothLogByExpoID >> "+expoID);
        boothLogService.deleteBoothLogByExpoID(expoID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @Operation(summary = "刪除某攤位的所有攤位log")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "成功刪除某攤位的所有攤位log"
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
        System.out.println("BoothLogController: deleteBoothLogByBoothID >> "+boothID);
        boothLogService.deleteBoothLogByBoothID(boothID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
