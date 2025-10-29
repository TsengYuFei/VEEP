package com.example.api.DTO.Response;

import com.example.api.Entity.UserMessage;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserMessageResponse {
    private String senderAccount;
    private String senderAvatar;
    private String receiverAccount;
    private String receiverAvatar;
    private String message;
    private LocalDateTime sendAt;
    private Boolean isRead;

    public static UserMessageResponse fromUserMessage(UserMessage userMessage) {
        UserMessageResponse response = new UserMessageResponse();
        response.setSenderAccount(userMessage.getSender().getUserAccount());
        response.setSenderAvatar(userMessage.getSender().getAvatar());
        response.setReceiverAccount(userMessage.getReceiver().getUserAccount());
        response.setReceiverAvatar(userMessage.getReceiver().getAvatar());
        response.setMessage(userMessage.getMessage());
        response.setSendAt(userMessage.getSendAt());
        response.setIsRead(userMessage.getIsRead());
        return response;
    }
}
