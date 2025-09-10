package com.example.api.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "expo_group_message")
public class ExpoGroupMessage {

    @Id
    @Column(name = "messageID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer messageID;

    @Lob
    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "send_at", updatable = false)
    private LocalDateTime sendAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expoID", nullable = false)
    private Expo expo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senderAccount", nullable = false)
    private User sender;



    @PrePersist
    protected void onCreate() {
        sendAt = LocalDateTime.now();
    }
}
