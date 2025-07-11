package com.example.api.DTO.Response;

import java.time.LocalDateTime;

public interface MessageListView {
    String getTargetAccount();
    String getTargetName();
    String getTargetAvatar();
    LocalDateTime getLatestTime();
    String getLatestMessage();
    Integer getUnreadCount();
}
