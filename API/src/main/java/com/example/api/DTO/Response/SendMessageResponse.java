package com.example.api.DTO.Response;

import com.example.api.Entity.Message;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SendMessageResponse {
    private String senderAccount;
    private String receiverAccount;
    private String message;
    private LocalDateTime sendAt;
    private Boolean isRead;

    public static SendMessageResponse fromMessage(Message message) {
        SendMessageResponse response = new SendMessageResponse();
        response.setSenderAccount(message.getSender().getUserAccount());
        response.setReceiverAccount(message.getReceiver().getUserAccount());
        response.setMessage(message.getMessage());
        response.setSendAt(message.getSendAt());
        response.setIsRead(message.getIsRead());
        return response;
    }
}
