package com.example.dt.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
public class Field {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String label;

    @Enumerated(EnumType.STRING)
    private FieldType type;

    private String defaultValue;

    @ManyToOne
    @JoinColumn(name = "form_id")
    @ToString.Exclude
    private Form form;
}
