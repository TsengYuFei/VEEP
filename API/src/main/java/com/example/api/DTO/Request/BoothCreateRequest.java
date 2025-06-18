package com.example.api.DTO.Request;

import com.example.api.Entity.OpenMode;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BoothCreateRequest {
    @Size(min = 1, max = 30, message = "The length of the booth name should be between 1 and 30 characters.")
    private String name;

    private String avatar;

    private String introduction;

    private List<String> tags;

    @NotNull(message = "The booth opening mode cannot be empty.")
    private OpenMode openMode;

    private Boolean openStatus;

    @FutureOrPresent(message = "The start time of the booth cannot be in the past.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime openStart;

    @FutureOrPresent(message = "The end time of the booth cannot be in the past.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime openEnd;

    @NotNull(message = "The maximum number of participants at a booth at the same time cannot be empty.")
    @Min(value = 1, message = "The maximum number of participants at a booth at the same time is at least one person.")
    @Max(value = 500, message = "The maximum number of participants at the booth at the same time is 500.")
    private Integer maxParticipants;

    @NotNull(message = "Whether the booth is displayed on the overview page cannot be empty.")
    private Boolean display;

    private List<String> collaborators;

    private List<String> staffs;
}
