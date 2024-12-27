package com.example.dt.controller;

import com.example.dt.dto.FieldDTO;
import com.example.dt.dto.FormDTO;
import com.example.dt.exception.InvalidInputException;
import com.example.dt.exception.OperationNotAllowedException;
import com.example.dt.exception.ResourceNotFoundException;
import com.example.dt.service.FormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FormController {

    @Autowired
    private FormService formService;

    @GetMapping("/forms")
    public List<FormDTO> getAllForms() {
        return formService.getAllForms();
    }

    @PostMapping("/forms")
    public FormDTO createForm(@RequestBody FormDTO formDTO) {
        if (formDTO.getName() == null || formDTO.getName().isEmpty()) {
            throw new InvalidInputException("Form name cannot be null or empty.");
        }
        System.out.println("Saving Form: " + formDTO.toString());
        return formService.createForm(formDTO);
    }

    @GetMapping("/forms/{id}")
    public FormDTO getFormById(@PathVariable Long id) {
        FormDTO formDTO = formService.getFormById(id);
        if (formDTO == null) {
            throw new ResourceNotFoundException("Form with ID " + id + " does not exist.");
        }
        return formDTO;
    }

    @PutMapping("/forms/{id}")
    public FormDTO updateForm(@PathVariable Long id, @RequestBody FormDTO formDTO) {
        FormDTO existingForm = formService.getFormById(id);
        if (existingForm == null) {
            throw new ResourceNotFoundException("Form with ID " + id + " does not exist.");
        }
        if (formDTO.getName() == null || formDTO.getName().isEmpty()) {
            throw new InvalidInputException("Form name cannot be null or empty.");
        }
        return formService.updateForm(id, formDTO);
    }

    @DeleteMapping("/forms/{id}")
    public void deleteForm(@PathVariable Long id) {
        FormDTO existingForm = formService.getFormById(id);
        if (existingForm == null) {
            throw new ResourceNotFoundException("Form with ID " + id + " does not exist.");
        }
        formService.deleteForm(id);
    }

    @PostMapping("/forms/{id}/publish")
    public void toggleFormPublish(@PathVariable Long id) {
        FormDTO existingForm = formService.getFormById(id);
        if (existingForm == null) {
            throw new ResourceNotFoundException("Form with ID " + id + " does not exist.");
        }
        formService.toggleFormPublishStatus(id);
    }

    @GetMapping("/forms/published")
    public List<FormDTO> getPublishedForms() {
        return formService.getPublishedForms();
    }

    @GetMapping("/forms/{id}/fields")
    public List<FieldDTO> getFieldsByFormId(@PathVariable Long id) {
        FormDTO formDTO = formService.getFormById(id);
        if (formDTO == null) {
            throw new ResourceNotFoundException("Form with ID " + id + " does not exist.");
        }
        return formDTO.getFields();
    }

    @PutMapping("/forms/{id}/fields")
    public List<FieldDTO> updateFields(@PathVariable Long id, @RequestBody List<FieldDTO> fieldDTOs) {
        FormDTO formDTO = formService.getFormById(id);
        if (formDTO == null) {
            throw new ResourceNotFoundException("Form with ID " + id + " does not exist.");
        }
        if (fieldDTOs == null || fieldDTOs.isEmpty()) {
            throw new InvalidInputException("Field list cannot be null or empty.");
        }
        System.out.println("Form before updating fields: " + formDTO);
        FormDTO updatedForm = formService.updateFields(id, fieldDTOs);
        System.out.println("Form after updating fields: " + updatedForm);
        return updatedForm.getFields();
    }

}
