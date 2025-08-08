package com.example.api.Controller;

import com.example.api.DTO.Request.ExpoEnterRequest;
import com.example.api.DTO.Request.ExpoUpdateRequest;
import com.example.api.DTO.Response.BoothOverviewResponse;
import com.example.api.DTO.Response.ExpoEditResponse;
import com.example.api.DTO.Request.ExpoCreateRequest;
import com.example.api.DTO.Response.ExpoEnterResponse;
import com.example.api.DTO.Response.UserListResponse;
import com.example.api.Service.SingleExpoService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "單一展會相關")
@RequestMapping("/expo")
@RestController
@RequiredArgsConstructor
public class SingleExpoController {
    private final SingleExpoService singleExpoService;



    @Operation(
            summary = "獲取展會資訊(編輯用)",
            description = "用於展會編輯頁面。"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功取得展會資訊(編輯用)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExpoEditResponse.class)
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
    @GetMapping("/edit/{expoID}")
    public ResponseEntity<ExpoEditResponse> getExpoEditByID(
            @Parameter(description = "展會ID", required = true)
            @PathVariable Integer expoID
    ){
        System.out.println("SingleExpoController: getExpoEdit >> "+expoID);

        ExpoEditResponse expo = singleExpoService.getExpoEditByID(expoID);
        return ResponseEntity.status(HttpStatus.OK).body(expo);
    }


    @Operation(
            summary = "新增展會",
            description = "可輸入欄位 1.展會擁有者的使用者帳號 2.展會名稱 3.圖像 4.價錢 5.介紹 " +
                    "6.開放模式 7.開放狀態 8.開始時間 9.結束時間 10.驗證碼 11.同時間最大參與人數 " +
                    "12.是否顯示於展會總覽頁面 13.合作者(數個) 14.白名單(數個) 15.黑名單(數個)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "成功新增展會",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExpoEditResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @PreAuthorize("hasRole('FOUNDER')")
    @PostMapping()
    public ResponseEntity<ExpoEditResponse> createExpo(
            @Valid @RequestBody ExpoCreateRequest expoCreateRequest
    ){
        System.out.print("SingleExpoController: createExpo >> ");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userAccount = authentication.getName();
        System.out.println(userAccount);

        Integer expoID = singleExpoService.createExpo(userAccount, expoCreateRequest);
        ExpoEditResponse expo = singleExpoService.getExpoEditByID(expoID);
        return ResponseEntity.status(HttpStatus.CREATED).body(expo);
    }


    @Operation(
            summary = "更新展會",
            description = "用於展會資料更新頁面。可更新除了expoID、ownerAccount外之所有欄位"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功更新展會資訊",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExpoEditResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "輸入格式錯誤"
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
    @PutMapping("/{expoID}")
    public ResponseEntity<ExpoEditResponse> updateExpoByID(
            @Parameter(description = "展會ID", required = true)
            @PathVariable Integer expoID,
            @Valid @RequestBody ExpoUpdateRequest expoRequest
    ){
        System.out.println("SingleExpoController: updateExpoByID >> "+expoID);

        singleExpoService.updateExpoByID(expoID, expoRequest);
        ExpoEditResponse expo = singleExpoService.getExpoEditByID(expoID);
        return ResponseEntity.status(HttpStatus.OK).body(expo);
    }


    @Operation(
            summary = "刪除展會",
            description = "將所屬booth的對應expo設為null、刪除封面圖片、刪除所有expo log紀錄"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "成功刪除展會"
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
    @PreAuthorize("hasRole('FOUNDER') and @expoSecurity.isOwner(#expoID)")
    @DeleteMapping("/{expoID}")
    public ResponseEntity<?> deleteExpoByID(
            @Parameter(description = "展會ID", required = true)
            @PathVariable Integer expoID
    ){
        System.out.println("SingleExpoController: deleteExpoByID >> "+expoID);
        singleExpoService.deleteExpoByID(expoID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @Operation(
            summary = "獲取所有合作者",
            description = "合作者可共同編輯此攤位"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功獲取所有合作者",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = UserListResponse.class)
                            )
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
    @GetMapping("/collaborator/{expoID}")
    public ResponseEntity<List<UserListResponse>> getAllColList(
            @Parameter(description = "展會ID", required = true)
            @PathVariable Integer expoID
    ){
        System.out.println("SingleExpoController: getAllColList >> "+expoID);
        List<UserListResponse> collaborator = singleExpoService.getAllColList(expoID);
        return ResponseEntity.status(HttpStatus.OK).body(collaborator);
    }


    @Operation(
            summary = "獲取黑名單中的所有使用者",
            description = "黑名單中的使用者不可進入此展會"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功獲取黑名單中的所有使用者",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = UserListResponse.class)
                            )
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
    @GetMapping("/blacklist/{expoID}")
    public ResponseEntity<List<UserListResponse>> getAllBlack(
            @Parameter(description = "展會ID", required = true)
            @PathVariable Integer expoID
    ){
        System.out.println("SingleExpoController: getAllBlack >> "+expoID);
        List<UserListResponse> blacklisted = singleExpoService.getAllBlack(expoID);
        return ResponseEntity.status(HttpStatus.OK).body(blacklisted);
    }


    @Operation(
            summary = "獲取白名單中的所有使用者",
            description = "白名單中的使用者不可進入此展會"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功獲取白名單中的所有使用者",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = UserListResponse.class)
                            )
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
    @GetMapping("/whitelist/{expoID}")
    public ResponseEntity<List<UserListResponse>> getAllWhite(
            @Parameter(description = "展會ID", required = true)
            @PathVariable Integer expoID
    ){
        System.out.println("SingleExpoController: getAllWhite >> "+expoID);
        List<UserListResponse> whitelisted = singleExpoService.getAllWhite(expoID);
        return ResponseEntity.status(HttpStatus.OK).body(whitelisted);
    }


    @Operation(
            summary = "獲取所有攤位"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功獲取所有攤位",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = BoothOverviewResponse.class)
                            )
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
    @GetMapping("/booths/{expoID}")
    public ResponseEntity<List<BoothOverviewResponse>> getAllBoothOverview(
            @Parameter(description = "展會ID", required = true)
            @RequestParam Integer expoID
    ){
        System.out.println("SingleExpoController: getAllBoothOverview >> "+expoID);
        List<BoothOverviewResponse> booths = singleExpoService.getAllBoothOverview(expoID);
        return ResponseEntity.status(HttpStatus.OK).body(booths);
    }


    @Operation(
            summary = "進入展會時呼叫",
            description = "會驗證展會驗證碼(如果沒有傳入null即可)。如果是展會或攤位的任何工作人員可直接進入"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功進入展會",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ExpoEnterResponse.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "驗證碼錯誤"
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
    @PostMapping("/enter/{expoID}")
    public ResponseEntity<ExpoEnterResponse> enterExpo(
            @Parameter(description = "展會ID", required = true)
            @RequestParam Integer expoID,
            @RequestBody ExpoEnterRequest expoRequest
    ){
        System.out.print("SingleExpoController: enterExpo >> "+expoID);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userAccount = authentication.getName();
        System.out.println(", "+userAccount);

        ExpoEnterResponse response = singleExpoService.enterExpo(expoID, userAccount, expoRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @Operation(
            summary = "獲取展會是否開放中"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功取得展會是否開放中",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class)
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
    @GetMapping("/is_opening_or_not/{expoID}")
    public ResponseEntity<Boolean> getOpeningOrNot(
            @Parameter(description = "展會ID", required = true)
            @PathVariable Integer expoID
    ){
        System.out.println("SingleExpoController: getOpeningOrNot >> "+expoID);

        Boolean isOpening = singleExpoService.isOpening(expoID);
        return ResponseEntity.status(HttpStatus.OK).body(isOpening);
    }
}
