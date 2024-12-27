package com.example.dt.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.ArrayList;

@Entity
@Data
public class Form {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private boolean published;

    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Field> fields = new ArrayList<>();

    private String submitText;

    private String submitUrl;

    public void addField(Field field) {
        field.setForm(this);
        this.fields.add(field);
    }

    public void removeField(Field field) {
        field.setForm(null);
        this.fields.remove(field);
    }
}
