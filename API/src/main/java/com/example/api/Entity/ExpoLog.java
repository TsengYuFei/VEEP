package com.example.api.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "expo_user_log")
public class ExpoLog {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "sessionID", nullable = false, unique = true)
    private String sessionID;

    @Column(name = "enter_at", updatable = false, nullable = false)
    private LocalDateTime enterAt;

    @Column(name = "exit_at")
    private LocalDateTime exitAt;

    @Column(name = "last_active_at")
    private LocalDateTime lastActiveAt;

    @Column(name = "has_used_ai", nullable = false)
    private Boolean hasUsedAi;

    @Column(name = "ai_message_count", nullable = false)
    private Integer aiMessageCount;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "is_owner", nullable = false)
    private Boolean isOwner;

    @Column(name = "is_collaborator", nullable = false)
    private Boolean isCollaborator;

    @Column(name = "is_white_listed", nullable = false)
    private Boolean isWhiteListed;

    @ManyToOne
    @JoinColumn(name = "userAccount", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "expoID", nullable = false)
    private Expo expo;


    @PrePersist
    protected void onCreate() {
        enterAt = LocalDateTime.now();
        exitAt = null;
        lastActiveAt = null;
        hasUsedAi = false;
        aiMessageCount = 0;
        if(isOwner == null) isOwner = false;
        if(isCollaborator == null) isCollaborator = false;
        if(isWhiteListed == null) isWhiteListed = false;
    }
}
