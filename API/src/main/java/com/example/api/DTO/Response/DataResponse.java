package com.example.api.DTO.Response;

import lombok.Data;

import java.util.List;

@Data
public class DataResponse {
    private DataOverviewResponse overview;
    private List<DailyIncreaseResponse> dailyVisitor;
    private List<DailyStayAverageResponse> dailyStayAverage;
    private Long totalAvgDurationSeconds;
    private DataRateResponse engagement;
}
