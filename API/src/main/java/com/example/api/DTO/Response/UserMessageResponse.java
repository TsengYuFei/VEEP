package com.example.api.DTO.Response;

import com.example.api.Entity.UserMessage;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserMessageResponse {
    private String senderAccount;
    private String receiverAccount;
    private String message;
    private LocalDateTime sendAt;
    private Boolean isRead;

    public static UserMessageResponse fromUserMessage(UserMessage userMessage) {
        UserMessageResponse response = new UserMessageResponse();
        response.setSenderAccount(userMessage.getSender().getUserAccount());
        response.setReceiverAccount(userMessage.getReceiver().getUserAccount());
        response.setMessage(userMessage.getMessage());
        response.setSendAt(userMessage.getSendAt());
        response.setIsRead(userMessage.getIsRead());
        return response;
    }
}
