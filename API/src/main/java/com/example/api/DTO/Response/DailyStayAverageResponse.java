package com.example.api.DTO.Response;

import lombok.Data;
import java.time.LocalDate;

@Data
public class DailyStayAverageResponse {
    private LocalDate date;
    private long avgDurationSeconds;
}
