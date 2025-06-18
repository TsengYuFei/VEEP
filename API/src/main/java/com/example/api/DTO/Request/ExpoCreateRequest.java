package com.example.api.DTO.Request;

import com.example.api.Entity.OpenMode;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ExpoCreateRequest {
    @Size(min = 1, max = 30, message = "The expo opening mode cannot be empty.")
    private String name;

    private String avatar;

    @NotNull(message = "The expo price cannot be empty.")
    @PositiveOrZero(message = "The expo price must be a non-negative integer.")
    private Integer price;

    private String introduction;

    private List<String> tags;

    @NotNull(message = "The expo opening mode cannot be empty.")
    private OpenMode openMode;

    private Boolean openStatus;

    @FutureOrPresent(message = "The start time of the expo cannot be in the past.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime openStart;

    @FutureOrPresent(message = "The end time of the expo cannot be in the past.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime openEnd;

    private String accessCode;

    @NotNull(message = "The maximum number of participants at a expo at the same time cannot be empty.")
    @Min(value = 1, message = "The maximum number of participants at a expo at the same time is at least one person.")
    @Max(value = 1500, message = "The maximum number of participants at the expo at the same time is 500.")
    private Integer maxParticipants;

    @NotNull(message = "Whether the expo is displayed on the overview page cannot be empty.")
    private Boolean display;

    private List<String> collaborators;

    private List<String> whitelist;

    private List<String> blacklist;
}
