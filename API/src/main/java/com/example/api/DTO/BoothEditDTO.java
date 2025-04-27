package com.example.api.DTO;

import com.example.api.Model.OpenMode;
import lombok.Data;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Data
@Component
public class BoothEditDTO {
    private String name;
    private String avatar;
    private String introduction;
    private OpenMode openMode;
    private boolean openStatus;
    private LocalDateTime openStart;
    private LocalDateTime openEnd;
    private int maxParticipants;
    private boolean display;
}
