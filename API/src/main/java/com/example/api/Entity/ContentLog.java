package com.example.api.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "content_user_log")
public class ContentLog {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "contentNumber", nullable = false)
    private Integer contentNumber;

    @Column(name = "click_at", updatable = false, nullable = false)
    private LocalDateTime clickAt;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "is_owner", nullable = false)
    private Boolean isOwner;

    @Column(name = "is_collaborator", nullable = false)
    private Boolean isCollaborator;

    @ManyToOne
    @JoinColumn(name = "userAccount", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "expoID", nullable = false)
    private Expo expo;

    @ManyToOne
    @JoinColumn(name = "boothID", nullable = false)
    private Booth booth;

    @ManyToOne
    @JoinColumn(name = "contentID", nullable = false)
    private Content content;


    @PrePersist
    protected void onCreate() {
        clickAt = LocalDateTime.now();
        if(isOwner == null) isOwner = false;
        if(isCollaborator == null) isCollaborator = false;
    }
}
