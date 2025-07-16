package com.example.api.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_message")
public class UserMessage {

    @Id
    @Column(name = "messageID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer messageID;

    @Lob
    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "send_at", updatable = false)
    private LocalDateTime sendAt;

    @Column(name = "isRead", nullable = false)
    private Boolean isRead = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senderAccount", nullable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiverAccount", nullable = false)
    private User receiver;


    @PrePersist
    protected void onCreate() {
        sendAt = LocalDateTime.now();
    }
}
