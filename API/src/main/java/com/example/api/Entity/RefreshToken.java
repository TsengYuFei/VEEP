package com.example.api.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "refresh_token")
@EqualsAndHashCode(exclude = "user")
@ToString(exclude = "user")
public class RefreshToken {
    @Id
    @Column(name = "tokenID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tokenID;

    @OneToOne
    @JoinColumn(name = "userAccount", nullable = false, unique = true)
    @JsonIgnore
    private User user;

    @Column(name = "token", length = 512, nullable = false)
    private String token;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
