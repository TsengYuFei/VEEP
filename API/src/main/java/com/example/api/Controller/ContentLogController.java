package com.example.api.Controller;

import com.example.api.DTO.Response.ContentLogResponse;
import com.example.api.Service.ContentLogService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "攤位內容Log相關")
@RequestMapping("/log/content")
@RestController
@RequiredArgsConstructor
public class ContentLogController {
    @Autowired
    private final ContentLogService contentLogService;


    @Operation(
            summary = "新增攤位內容log",
            description = "進入攤位時呼叫。"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "成功新增攤位內容log"
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
                    responseCode = "404",
                    description = "找不到攤位內容"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @PostMapping("/create/{boothID}/{number}")
    public ResponseEntity<?> createContentLog(
            @Parameter(description = "攤位ID", required = true)
            @PathVariable Integer boothID,
            @Parameter(description = "內容編號", required = true)
            @PathVariable Integer number
    ){
        System.out.print("ContentLogController: createContentLog>> ");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userAccount = authentication.getName();
        System.out.println(userAccount);

        contentLogService.createContentLogByBoothIDAndNumber(boothID, number, userAccount);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @Operation(
            summary = "獲取某展會的所有攤位內容log",
            description = "目前無排序"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功獲取含該展會的所有攤位內容log",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ContentLogResponse.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @GetMapping("/all/by_expo/{expoID}")
    public ResponseEntity<List<ContentLogResponse>> getAllContentLogByExpoID(
            @Parameter(description = "展會ID", required = true)
            @PathVariable Integer expoID
    ){
        System.out.println("ContentLogController: getAllContentLogByExpoID >> "+expoID);
        List<ContentLogResponse> logs = contentLogService.getAllContentLogByExpoID(expoID);
        return ResponseEntity.status(HttpStatus.OK).body(logs);
    }


    @Operation(
            summary = "獲取某攤位的所有攤位內容log",
            description = "目前無排序"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功獲取含該攤位的所有攤位內容log",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ContentLogResponse.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @GetMapping("/all/by_booth/{boothID}")
    public ResponseEntity<List<ContentLogResponse>> getAllContentLogByBoothID(
            @Parameter(description = "攤位ID", required = true)
            @PathVariable Integer boothID
    ){
        System.out.println("ContentLogController: getAllContentLogByBoothID >> "+boothID);
        List<ContentLogResponse> logs = contentLogService.getAllContentLogByBoothID(boothID);
        return ResponseEntity.status(HttpStatus.OK).body(logs);
    }


    @Operation(
            summary = "獲取某攤位內容的所有攤位內容log",
            description = "目前無排序"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功獲取含該攤位內容的所有攤位內容log",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ContentLogResponse.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @GetMapping("/all/by_content/{boothID}/{number}")
    public ResponseEntity<List<ContentLogResponse>> getAllContentLogByBoothIDAndNumber(
            @Parameter(description = "攤位ID", required = true)
            @PathVariable Integer boothID,
            @Parameter(description = "內容編號", required = true)
            @PathVariable Integer number
    ){
        System.out.println("ContentLogController: getAllContentLogByBoothIDAndNumber >> "+boothID+", "+number);
        List<ContentLogResponse> logs = contentLogService.getAllContentLogByBoothIDAndNumber(boothID, number);
        return ResponseEntity.status(HttpStatus.OK).body(logs);
    }


    @Operation(
            summary = "獲取某使用者的所有攤位內容log"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功獲取該使用者的所有攤位內容log",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ContentLogResponse.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @GetMapping("/user/{userAccount}")
    public ResponseEntity<List<ContentLogResponse>> getContentLogByUserAccount(
            @Parameter(description = "使用者帳號", required = true)
            @PathVariable String userAccount
    ){
        System.out.println("ContentLogController: getContentLogByUserAccount >> "+userAccount);
        List<ContentLogResponse> logs = contentLogService.getContentLogResponseByUserAccount(userAccount);
        return ResponseEntity.status(HttpStatus.OK).body(logs);
    }


    @Operation(
            summary = "獲取某使用者在某展會的所有攤位內容log"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功獲取該使用者在該展會的所有攤位內容log",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ContentLogResponse.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @GetMapping("/user/by_expo/{expoID}/{userAccount}")
    public ResponseEntity<List<ContentLogResponse>> getContentLogByUserAccountAndExpoID(
            @Parameter(description = "展會ID", required = true)
            @PathVariable Integer expoID,
            @Parameter(description = "使用者帳號", required = true)
            @PathVariable String userAccount
    ){
        System.out.println("ContentLogController: getContentLogByUserAccountAndExpoID >> "+userAccount+", "+expoID);
        List<ContentLogResponse> logs = contentLogService.getContentLogResponseByUserAccountAndExpoID(expoID, userAccount);
        return ResponseEntity.status(HttpStatus.OK).body(logs);
    }


    @Operation(
            summary = "獲取某使用者在某攤位的所有攤位內容log"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功獲取該使用者在該攤位的所有攤位內容log",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ContentLogResponse.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @GetMapping("/user/by_booth/{boothID}/{userAccount}")
    public ResponseEntity<List<ContentLogResponse>> getContentLogByUserAccountAndBoothID(
            @Parameter(description = "攤位ID", required = true)
            @PathVariable Integer boothID,
            @Parameter(description = "使用者帳號", required = true)
            @PathVariable String userAccount
    ){
        System.out.println("ContentLogController: getContentLogByUserAccountAndBoothID >> "+userAccount+", "+boothID);
        List<ContentLogResponse> logs = contentLogService.getContentLogResponseByUserAccountAndBoothID(boothID, userAccount);
        return ResponseEntity.status(HttpStatus.OK).body(logs);
    }


    @Operation(
            summary = "獲取某使用者在某攤位內容的所有攤位內容log"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功獲取該使用者在該攤位內容的所有攤位內容log",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ContentLogResponse.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @GetMapping("/user/by_content/{boothID}/{number}/{userAccount}")
    public ResponseEntity<List<ContentLogResponse>> getContentLogByUserAccountAndBoothIDAndNumber(
            @Parameter(description = "攤位ID", required = true)
            @PathVariable Integer boothID,
            @Parameter(description = "內容編號", required = true)
            @PathVariable Integer number,
            @Parameter(description = "使用者帳號", required = true)
            @PathVariable String userAccount
    ){
        System.out.println("ContentLogController: getContentLogByUserAccountAndBoothIDAndNumber >> "+userAccount+", "+boothID+", "+number);
        List<ContentLogResponse> logs = contentLogService.getContentLogResponseByUserAccountAndBoothIDAndNumber(boothID, number, userAccount);
        return ResponseEntity.status(HttpStatus.OK).body(logs);
    }


    @Operation(summary = "刪除某展會的所有攤位內容log")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "成功刪除某展會的所有攤位內容log"
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
    public ResponseEntity<?> deleteContentLogByExpoID(
            @Parameter(description = "展會ID", required = true)
            @PathVariable Integer expoID
    ){
        System.out.println("ContentLogController: deleteContentLogByExpoID >> "+expoID);
        contentLogService.deleteContentLogByExpoID(expoID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @Operation(summary = "刪除某攤位的所有攤位內容log")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "成功刪除某攤位的所有攤位內容log"
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
    public ResponseEntity<?> deleteContentLogByBoothID(
            @Parameter(description = "攤位ID", required = true)
            @PathVariable Integer boothID
    ){
        System.out.println("ContentLogController: deleteContentLogByBoothID >> "+boothID);
        contentLogService.deleteContentLogByBoothID(boothID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @Operation(summary = "刪除某攤位內容的所有攤位內容log")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "成功刪除某攤位內容的所有攤位內容log"
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
    @DeleteMapping("/delete/all/by_content/{boothID}/{number}")
    public ResponseEntity<?> deleteContentLogByBoothIDAndNumber(
            @Parameter(description = "攤位ID", required = true)
            @PathVariable Integer boothID,
            @Parameter(description = "內容編號", required = true)
            @PathVariable Integer number
    ){
        System.out.println("ContentLogController: deleteContentLogByBoothIDAndNumber >> "+boothID+", "+number);
        contentLogService.deleteContentLogByBoothIDAndNumber(boothID, number);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
