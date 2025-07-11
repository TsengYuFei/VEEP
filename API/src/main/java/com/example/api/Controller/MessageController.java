package com.example.api.Controller;

import com.example.api.DTO.Request.SendMessageRequest;
import com.example.api.DTO.Request.UserCreateRequest;
import com.example.api.DTO.Response.*;
import com.example.api.Service.MessageService;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "聊天室相關")
@RequestMapping("/message")
@RestController
@RequiredArgsConstructor
public class MessageController {
    @Autowired
    private final MessageService messageService;



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
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@Valid @RequestBody SendMessageRequest request){
        System.out.print("MessageController: sendMessage >> ");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userAccount = authentication.getName();
        System.out.println(userAccount);

        messageService.sendMessage(userAccount, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @Operation(
            summary = "獲取聊天紀錄",
            description = "用於一對一聊天室"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功取得聊天紀錄",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = MessageResponse.class)
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
    @GetMapping("/conversation/{targetAccount}")
    public ResponseEntity<List<MessageResponse>> getConversation(
            @Parameter(description = "使用者帳號", required = true)
            @PathVariable String targetAccount,
            @Parameter(description = "頁數(第幾頁)", required = true)
            @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "數量(一頁幾筆資料)", required = true)
            @RequestParam(defaultValue = "15") Integer size
    ) {
        System.out.print("MessageController: getConversation >> ");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userAccount = authentication.getName();
        System.out.println(userAccount+", "+targetAccount);

        List<MessageResponse> messageList = messageService.getConversation(userAccount, targetAccount, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(messageList);

    }


    @Operation(
            summary = "獲取未讀訊息數",
            description = "針對單一使用者，非全部"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功取得未讀訊息數",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = UnreadMessageResponse.class)
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
    @GetMapping("/unread/count/{targetAccount}")
    public ResponseEntity<UnreadMessageResponse> getUnreadCount(
            @Parameter(description = "使用者帳號", required = true)
            @PathVariable String targetAccount
    ){
        System.out.print("MessageController: getUnreadCount >> ");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userAccount = authentication.getName();
        System.out.println(userAccount+", "+targetAccount);

        UnreadMessageResponse response = messageService.getUnreadCount(userAccount, targetAccount);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
