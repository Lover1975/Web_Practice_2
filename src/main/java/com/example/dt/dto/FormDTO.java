package com.example.dt.dto;

import lombok.Data;
import java.util.List;

@Data
public class FormDTO {
    private Long id;
    private String name;
    private boolean published;
    private List<FieldDTO> fields;
    private String submitText;
    private String submitUrl;
}
