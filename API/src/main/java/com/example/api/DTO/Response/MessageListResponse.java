package com.example.api.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageListResponse {
    private String targetAccount;
    private String targetName;
    private String targetAvatar;
    private String latestMessage;
    private LocalDateTime latestTime;
    private Integer unreadCount;

    public static MessageListResponse fromMessageView(MessageListView view) {
        MessageListResponse response = new MessageListResponse();
        response.setTargetAccount(view.getTargetAccount());
        response.setTargetName(view.getTargetName());
        response.setTargetAvatar(view.getTargetAvatar());
        response.setLatestMessage(view.getLatestMessage());
        response.setLatestTime(view.getLatestTime());
        response.setUnreadCount(view.getUnreadCount());
        return response;
    }
}
