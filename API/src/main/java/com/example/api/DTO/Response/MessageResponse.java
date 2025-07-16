package com.example.api.DTO.Response;

import com.example.api.Entity.UserMessage;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageResponse {
    private String senderAccount;
    private String receiverAccount;
    private String message;
    private LocalDateTime sendAt;
    private Boolean isRead;

    public static MessageResponse fromMessage(UserMessage userMessage) {
        MessageResponse response = new MessageResponse();
        response.setSenderAccount(userMessage.getSender().getUserAccount());
        response.setReceiverAccount(userMessage.getReceiver().getUserAccount());
        response.setMessage(userMessage.getMessage());
        response.setSendAt(userMessage.getSendAt());
        response.setIsRead(userMessage.getIsRead());
        return response;
    }
}
