package com.example.dt.repository;

import com.example.dt.model.Form;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FormRepository extends JpaRepository<Form, Long> {
    List<Form> findByPublishedTrue();
}
