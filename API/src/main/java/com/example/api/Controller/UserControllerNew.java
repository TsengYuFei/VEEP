package com.example.api.Controller;

import com.example.api.DTO.Response.UserDetailResponse;
import com.example.api.Service.UserServiceNew;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "單一使用者相關", description = "含三種不同範圍的GET")
@RequestMapping("/user")
@RestController
public class UserControllerNew {
    @Autowired
    private final UserServiceNew userService;

    public UserControllerNew(UserServiceNew userService) {
        this.userService = userService;
    }

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
        System.out.println("UserControllerNew: getUsrDetailByAccount >> "+userAccount);
        UserDetailResponse user = userService.getUserByAccount(userAccount);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
