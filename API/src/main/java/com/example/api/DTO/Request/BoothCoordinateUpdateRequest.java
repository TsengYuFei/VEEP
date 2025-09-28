package com.example.api.DTO.Request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;


@Data
public class BoothCoordinateUpdateRequest {
    private Integer boothID;

    @Min(value = 0, message = "The minimum X-coordinate value of booth is 0.")
    @Max(value = 4, message = "The maximum X-coordinate value of booth is 4.")
    private Integer coordinateX;

    @Min(value = 0, message = "The minimum Y-coordinate value of booth is 0.")
    @Max(value = 6, message = "The maximum Y-coordinate value of booth is 6.")
    private Integer coordinateY;
}
