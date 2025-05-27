package com.example.api.DTO.Request;

import com.example.api.Entity.OpenMode;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BoothUpdateRequest {
    @Size(min = 1, max = 30, message = "The length of the booth name should be between 1 and 30 characters.")
    private String name;

    private String avatar;

    private String introduction;

    private List<String> tags;

    private OpenMode openMode;

    private Boolean openStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime openStart;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime openEnd;

    @Min(value = 1, message = "The maximum number of participants at a booth at the same time is at least one person.")
    @Max(value = 500, message = "The maximum number of participants at the booth at the same time is 500.")
    private Integer maxParticipants;

    private Boolean display;

    private List<String> collaborators;

    private List<String> staffs;
}
