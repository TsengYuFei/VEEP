package com.example.api.DTO.Response;

import lombok.Data;

@Data
public class DataOverviewResponse {
    private Integer totalVisitors;
    private Integer newVisitorsLast7Days;
}
