package com.example.api.Controller;

import com.example.api.DTO.UserOverviewDTO;
import com.example.api.DTO.UserDetailDTO;
import com.example.api.Service.UserService;
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
import org.springframework.web.bind.annotation.*;

@Tag(name = "使用者相關", description = "CRUD")
@RequestMapping("/user")
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(
            summary = "獲取使用者資訊(詳細)",
            description = "使用於人檔案頁面。" +
                    "取得 1.使用者名稱 2.使用者帳號 3.頭像URL 4.個人簡介 5.背景圖片URL" +
                    " 7.顯示追蹤者 8.顯示追蹤中 9.顯示參與紀錄 10.顯示進行中的持有展會 11.顯示進行中的持有攤位 12.身分"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功取得使用者資訊(詳細)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDetailDTO.class)
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
    @GetMapping("/{userAccount}/detail")
    public ResponseEntity<UserDetailDTO> getUserDetail(
            @Parameter(description = "使用者帳號", required = true)
            @PathVariable String userAccount
    ) {
        System.out.println("UserController: getUserDetail >> "+userAccount);
        UserDetailDTO user = userService.getUserDetailByAccount(userAccount);

        if(user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
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
                            schema = @Schema(implementation = UserOverviewDTO.class)
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
    @GetMapping("/{userAccount}/overview")
    public ResponseEntity<UserOverviewDTO> getUserOverview(
            @Parameter(description = "使用者帳號", required = true)
            @PathVariable String userAccount
    ){
        System.out.println("UserController: getUserOverview >> "+userAccount);
        UserOverviewDTO user = userService.getUserOverviewByAccount(userAccount);

        if(user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
    }
}