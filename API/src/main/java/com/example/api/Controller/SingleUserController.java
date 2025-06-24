package com.example.api.Controller;

import com.example.api.DTO.Request.UserCreateRequest;
import com.example.api.DTO.Request.UserUpdateRequest;
import com.example.api.DTO.Response.*;
import com.example.api.Entity.User;
import com.example.api.Exception.ForibiddenException;
import com.example.api.Service.SingleUserService;
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

@Tag(name = "單一使用者相關", description = "含三種不同範圍的GET")
@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class SingleUserController {
    @Autowired
    private final SingleUserService singleUserService;


    @Operation(
            summary = "獲取使用者資訊(詳細)",
            description = "使用於個人檔案頁面。" +
                    "取得 1.使用者名稱 2.使用者帳號 3.頭像 4.個人簡介 5.背景圖片" +
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
        System.out.print("SingleUserController: getUsrDetailByAccount >> "+userAccount);
        UserDetailResponse user = singleUserService.getUserDetailByAccount(userAccount);
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
        System.out.println("SingleUserController: getUserOverviewByAccount >> "+userAccount);
        UserOverviewResponse user = singleUserService.getUserOverviewByAccount(userAccount);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }


    @Operation(
            summary = "獲取使用者資訊(編輯用)",
            description = "只能獲取本人，用於個人資料更新頁面。可獲取除了password外之所有欄位"
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
    @GetMapping("/edit")
    public ResponseEntity<UserEditResponse> getUserEditByAccount() {
        System.out.print("SingleUserController: getUserEditByAccount >> ");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userAccount = authentication.getName();
        System.out.println(userAccount);

        UserEditResponse user = singleUserService.getUserEditByAccount(userAccount);
        return ResponseEntity.status(HttpStatus.OK).body(user);

    }


    @Operation(
            summary = "新增使用者",
            description = "會自動寄送驗證信。可輸入欄位 1.使用者名稱 2.使用者帳號 3.密碼 4.電話 5.電子郵箱 6.頭像 7.生日(yyyy-MM-dd格式)"
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
    @PostMapping("/create")
    public ResponseEntity<UserDetailResponse> createUser(@Valid @RequestBody UserCreateRequest userRequest){
        System.out.println("SingleUserController: createUser");
        String userAccount = singleUserService.createUser(userRequest);
        UserDetailResponse user = singleUserService.getUserDetailByAccount(userAccount);
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
    @PutMapping("/update")
    public ResponseEntity<UserDetailResponse> updateUserByAccount(@Valid @RequestBody UserUpdateRequest userRequest){
        System.out.println("SingleUserController: updateUserByAccount >> ");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userAccount = authentication.getName();
        System.out.println(userAccount);

        singleUserService.updateUserByAccount(userAccount, userRequest);
        UserDetailResponse user = singleUserService.getUserDetailByAccount(userAccount);
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
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUserByAccount(){
        System.out.println("SingleUserController: deleteUserByAccount >> ");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userAccount = authentication.getName();
        System.out.println(userAccount);

        singleUserService.deleteUserByAccount(userAccount);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @Operation(
            summary = "獲取所有擁有的展會"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功獲取所有擁有的展會",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ExpoOverviewResponse.class)
                            )
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
    @GetMapping("/expos/{userAccount}")
    public ResponseEntity<List<ExpoOverviewResponse>> getAllExpoOverview(
            @Parameter(description = "使用者帳號", required = true)
            @PathVariable String userAccount
    ){
        System.out.println("SingleUserController: getAllExpoOverview >> "+userAccount);
        List<ExpoOverviewResponse> expos = singleUserService.getAllExpoOverview(userAccount);
        return ResponseEntity.status(HttpStatus.OK).body(expos);
    }


    @Operation(
            summary = "獲取所有擁有的攤位"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功獲取所有擁有的攤位",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = BoothOverviewResponse.class)
                            )
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
    @GetMapping("/booths/{userAccount}")
    public ResponseEntity<List<BoothOverviewResponse>> getAllBoothOverview(
            @Parameter(description = "使用者帳號", required = true)
            @PathVariable String userAccount
    ){
        System.out.println("SingleUserController: getAllBoothOverview >> "+userAccount);
        List<BoothOverviewResponse> booths = singleUserService.getAllBoothOverview(userAccount);
        return ResponseEntity.status(HttpStatus.OK).body(booths);
    }


    @Operation(
            summary = "切換使用者身分",
            description = "有GENERAL和FOUNDER兩種"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功切換使用者身分",
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
    @PutMapping("/switch_role")
    public ResponseEntity<UserOverviewResponse> switchRole(){
        System.out.print("SingleUserController: switchRole >> ");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userAccount = authentication.getName();
        System.out.println(userAccount);

        singleUserService.switchRole(userAccount);
        UserOverviewResponse user = singleUserService.getUserOverviewByAccount(userAccount);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }


    @Operation(
            summary = "信箱驗證"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "驗證成功，啟用帳號"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "驗證失敗，無效的驗證碼"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @GetMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam("code") String code){
        System.out.println("SingleUserController: verifyUser >> "+code);
        singleUserService.verifyUser(code);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
