package com.example.api.Controller;

import com.example.api.DTO.Request.SendExpoGroupMessageRequest;
import com.example.api.DTO.Response.ExpoGroupMessageResponse;
import com.example.api.Service.ExpoGroupMessageService;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "群組聊天室相關")
@RequestMapping("/message/group")
@RestController
@RequiredArgsConstructor
public class ExpoGroupMessageController {
    private final ExpoGroupMessageService expoGroupMessageService;



    @Operation(summary = "發送訊息")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "成功發送訊息"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "找不到使用者"
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
    @PostMapping("/send/{expoID}")
    public ResponseEntity<?> sendMessage(
            @Parameter(description = "展會ID", required = true)
            @PathVariable Integer expoID,
            @Valid @RequestBody SendExpoGroupMessageRequest request
    ){
        System.out.print("ExpoGroupMessageController: sendMessage >> ");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userAccount = authentication.getName();
        System.out.println(userAccount);

        expoGroupMessageService.sendGroupMessage(expoID, userAccount, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @Operation(
            summary = "獲取聊天紀錄",
            description = "用於展會群組聊天室，最後傳送的訊息會在最前面"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功取得聊天紀錄",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ExpoGroupMessageResponse.class)
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
    @GetMapping("/conversation/{expoID}")
    public ResponseEntity<List<ExpoGroupMessageResponse>> getConversation(
            @Parameter(description = "展會ID", required = true)
            @PathVariable Integer expoID,
            @Parameter(description = "頁數(第幾頁)", required = true)
            @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "數量(一頁幾筆資料)", required = true)
            @RequestParam(defaultValue = "15") Integer size
    ) {
        System.out.print("ExpoGroupMessageController: getConversation >> ");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userAccount = authentication.getName();
        System.out.println(userAccount);

        List<ExpoGroupMessageResponse> messageList = expoGroupMessageService.getConversation(expoID, userAccount, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(messageList);

    }
}
