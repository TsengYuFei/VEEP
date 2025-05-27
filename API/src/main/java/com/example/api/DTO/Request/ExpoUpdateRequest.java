package com.example.api.DTO.Request;

import com.example.api.Entity.OpenMode;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ExpoUpdateRequest {
    @Size(min = 1, max = 30, message = "The expo opening mode cannot be empty.")
    private String name;

    private String avatar;

    @PositiveOrZero(message = "The expo price must be a non-negative integer.")
    private Integer price;

    private String introduction;

    private List<String> tags;

    private OpenMode openMode;

    private Boolean openStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime openStart;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime openEnd;

    private String accessCode;

    @Min(value = 1, message = "The maximum number of participants at a expo at the same time is at least one person.")
    @Max(value = 1500, message = "The maximum number of participants at the expo at the same time is 500.")
    private Integer maxParticipants;

    private Boolean display;

    private List<String> collaborators;

    private List<String> whitelist;

    private List<String> blacklist;
}
