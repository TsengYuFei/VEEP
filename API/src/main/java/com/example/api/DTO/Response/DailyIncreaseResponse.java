package com.example.api.DTO.Response;

import lombok.Data;
import java.time.LocalDate;

@Data
public class DailyIncreaseResponse {
    private LocalDate date;
    private Integer count;
}
