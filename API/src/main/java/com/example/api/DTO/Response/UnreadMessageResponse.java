package com.example.api.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UnreadMessageResponse {
    private String senderAccount;
    private Integer unreadCount;
}
