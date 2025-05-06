package com.example.api.Controller;

import com.example.api.DTO.Request.UserCreateRequest;
import com.example.api.DTO.Request.UserUpdateRequest;
import com.example.api.DTO.Response.UserDetailResponse;
import com.example.api.DTO.Response.UserEditResponse;
import com.example.api.DTO.Response.UserOverviewResponse;
import com.example.api.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.web.bind.annotation.*;

@Tag(name = "單一使用者相關", description = "含三種不同範圍的GET")
@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private final UserService userService;


    @Operation(
            summary = "獲取使用者資訊(詳細)",
            description = "使用於個人檔案頁面。" +
                    "取得 1.使用者名稱 2.使用者帳號 3.頭像URL 4.個人簡介 5.背景圖片URL" +
                    " 7.顯示追蹤者 8.顯示追蹤中 9.顯示參與紀錄 10.顯示進行中的持有展會 11.顯示進行中的持有攤位 12.身分"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功取得使用者資訊(詳細)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDetailResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "找不到使用者"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @GetMapping("/detail/{userAccount}")
    public ResponseEntity<UserDetailResponse> getUsrDetailByAccount(
            @Parameter(description = "使用者帳號", required = true)
            @PathVariable String userAccount
    ){
        System.out.println("UserController: getUsrDetailByAccount >> "+userAccount);
        UserDetailResponse user = userService.getUserDetailByAccount(userAccount);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }


    @Operation(
            summary = "獲取使用者資訊(概略)",
            description = "使用於展會中。" +
                    "取得 1.使用者名稱 2.使用者帳號 3.頭像 4.身分"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功取得使用者資訊(概略)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserOverviewResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "找不到使用者"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @GetMapping("/overview/{userAccount}")
    public ResponseEntity<UserOverviewResponse> getUserOverviewByAccount(
            @Parameter(description = "使用者帳號", required = true)
            @PathVariable String userAccount
    ){
        System.out.println("UserController: getUserOverviewByAccount >> "+userAccount);
        UserOverviewResponse user = userService.getUserOverviewByAccount(userAccount);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }


    @Operation(
            summary = "獲取使用者資訊(編輯用)",
            description = "用於個人資料更新頁面。可獲取除了userAccount、password及role外之所有欄位"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功取得使用者資訊(編輯用)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserEditResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "找不到使用者"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @GetMapping("/edit/{userAccount}")
    public ResponseEntity<UserEditResponse> getUserEditByAccount(
            @Parameter(description = "使用者帳號", required = true)
            @PathVariable String userAccount
    ) {
        System.out.println("UserService: getUserEditByAccount >> "+userAccount);
        UserEditResponse user = userService.getUserEditByAccount(userAccount);
        return ResponseEntity.status(HttpStatus.OK).body(user);

    }


    @Operation(
            summary = "新增使用者",
            description = "可輸入欄位 1.使用者名稱 2.使用者帳號 3.密碼 4.電話 5.電子郵箱 6.頭像URL 7.生日(yyyy-MM-dd格式)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "成功新增使用者",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDetailResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "使用者帳號已存在"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @PostMapping("")
    public ResponseEntity<UserDetailResponse> createUser(@Valid @RequestBody UserCreateRequest userRequest){
        System.out.println("UserController: createUser");
        String userAccount = userService.createUser(userRequest);
        UserDetailResponse user = userService.getUserDetailByAccount(userAccount);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }


    @Operation(
            summary = "更新使用者",
            description = "用於個人資料更新頁面。可更新除了userAccount、password及role外之所有欄位"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功更新使用者資訊",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDetailResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "輸入格式錯誤"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "找不到使用者"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @PutMapping("/{userAccount}")
    public ResponseEntity<UserDetailResponse> updateUserByAccount(
            @Parameter(description = "使用者帳號", required = true)
            @PathVariable String userAccount,
            @Valid @RequestBody UserUpdateRequest userRequest
    ){
        System.out.println("UserController: updateUserByAccount >> "+userAccount);
        userService.updateUserByAccount(userAccount, userRequest);
        UserDetailResponse user = userService.getUserDetailByAccount(userAccount);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }


    @Operation(summary = "刪除使用者")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "成功刪除使用者"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "找不到使用者"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @DeleteMapping("/{userAccount}")
    public ResponseEntity<?> deleteUserByAccount(
            @Parameter(description = "使用者帳號", required = true)
            @PathVariable String userAccount
    ){
        System.out.println("UserController: deleteUserByAccount >> "+userAccount);
        userService.deleteUserByAccount(userAccount);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
