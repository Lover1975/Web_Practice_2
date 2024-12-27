package com.example.dt.dto;

import com.example.dt.model.FieldType;
import lombok.Data;

@Data
public class FieldDTO {
    private Long id;
    private String name;
    private String label;
    private FieldType type;
    private String defaultValue;
}
