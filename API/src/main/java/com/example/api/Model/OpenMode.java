package com.example.api.Model;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum OpenMode {
    MANUAL,
    AUTO
}
