package com.example.api.Entity;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@Component
public class Expo {
    private int expoID;
    private String name;
    private String avatar;
    private int price;
    private String introduction;
    private OpenMode openMode;
    private boolean openStatus;
    private LocalDateTime openStart;
    private LocalDateTime openEnd;
    private String accessCode;
    private int maxParticipants;
    private boolean display;
}
