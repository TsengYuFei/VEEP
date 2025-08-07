package com.example.api.Controller;

import com.example.api.DTO.Request.SendExpoGroupMessageRequest;
import com.example.api.DTO.Request.SendUserMessageRequest;
import com.example.api.Service.ExpoGroupMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@Tag(name = "群組聊天室相關")
@RequestMapping("/message/group")
@RestController
@RequiredArgsConstructor
public class ExpoGroupMessageController {
    @Autowired
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
}
