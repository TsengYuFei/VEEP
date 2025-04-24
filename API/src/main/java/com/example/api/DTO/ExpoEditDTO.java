package com.example.api.DTO;

import com.example.api.Model.OpenMode;
import lombok.Data;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Data
@Component
public class ExpoEditDTO {
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
