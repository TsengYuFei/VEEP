package com.example.api.DTO.Response;

import com.example.api.Entity.Message;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageResponse {
    private String senderAccount;
    private String receiverAccount;
    private String message;
    private LocalDateTime sendAt;
    private Boolean isRead;

    public static MessageResponse fromMessage(Message message) {
        MessageResponse response = new MessageResponse();
        response.setSenderAccount(message.getSender().getUserAccount());
        response.setReceiverAccount(message.getReceiver().getUserAccount());
        response.setMessage(message.getMessage());
        response.setSendAt(message.getSendAt());
        response.setIsRead(message.getIsRead());
        return response;
    }
}
