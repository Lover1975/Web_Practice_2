package com.example.dt.service;

import com.example.dt.dto.FieldDTO;
import com.example.dt.dto.FormDTO;
import com.example.dt.exception.InvalidInputException;
import com.example.dt.exception.ResourceNotFoundException;
import com.example.dt.model.Field;
import com.example.dt.model.Form;
import com.example.dt.repository.FormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FormServiceImpl implements FormService {

    @Autowired
    private FormRepository formRepository;

    private FormDTO toDTO(Form form) {
        FormDTO dto = new FormDTO();
        dto.setId(form.getId());
        dto.setName(form.getName());
        dto.setPublished(form.isPublished());
        dto.setSubmitText(form.getSubmitText());
        dto.setSubmitUrl(form.getSubmitUrl());
        dto.setFields(form.getFields() == null ? new ArrayList<>() :
                form.getFields().stream().map(this::toFieldDTO).toList());
        return dto;
    }

    private FieldDTO toFieldDTO(Field field) {
        FieldDTO dto = new FieldDTO();
        dto.setId(field.getId());
        dto.setName(field.getName());
        dto.setLabel(field.getLabel());
        dto.setType(field.getType());
        dto.setDefaultValue(field.getDefaultValue());
        return dto;
    }

    private Form toEntity(FormDTO dto) {
        Form form = new Form();
        form.setId(dto.getId());
        form.setName(dto.getName());
        form.setPublished(dto.isPublished());
        form.setSubmitText(dto.getSubmitText());
        form.setSubmitUrl(dto.getSubmitUrl());
        System.out.println("FormDTO Fields: " + dto.getFields());
        if (dto.getFields() != null) {
            List<Field> fields = new ArrayList<>();
            for (FieldDTO fieldDTO : dto.getFields()) {
                Field field = new Field();
                field.setId(fieldDTO.getId());
                field.setName(fieldDTO.getName());
                field.setLabel(fieldDTO.getLabel());
                field.setType(fieldDTO.getType());
                field.setDefaultValue(fieldDTO.getDefaultValue());
                field.setForm(form);
                fields.add(field);
            }
            System.out.println("Mapped Fields: " + fields);
            form.setFields(fields);
        }
        return form;
    }


    @Override
    public List<FormDTO> getAllForms() {
        return formRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public FormDTO getFormById(Long id) {
        return formRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Form with ID " + id + " not found."));
    }

    @Override
    public FormDTO createForm(FormDTO formDTO) {
        System.out.println("Saving Form before toEntity: " + formDTO.toString());
        Form form = toEntity(formDTO);
        System.out.println("Saving Form before anything: " + form);
        if (form.getFields() != null) {
            for (Field field : form.getFields()) {
                field.setForm(form);
            }
        }
        form = formRepository.save(form);
        System.out.println("Saving Form: " + form);
        return toDTO(form);
    }

    @Override
    public FormDTO updateForm(Long id, FormDTO formDTO) {
        if (formDTO.getName() == null || formDTO.getName().isEmpty()) {
            throw new InvalidInputException("Form name cannot be null or empty.");
        }
        Form existingForm = formRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Form with ID " + id + " not found."));
        existingForm.setName(formDTO.getName());
        existingForm.setPublished(formDTO.isPublished());
        existingForm.setSubmitText(formDTO.getSubmitText());
        existingForm.setSubmitUrl(formDTO.getSubmitUrl());
        return toDTO(formRepository.save(existingForm));
    }

    @Override
    public void deleteForm(Long id) {
        if (!formRepository.existsById(id)) {
            throw new ResourceNotFoundException("Form with ID " + id + " not found.");
        }
        formRepository.deleteById(id);
    }

    @Override
    public List<FormDTO> getPublishedForms() {
        return formRepository.findByPublishedTrue().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public void toggleFormPublishStatus(Long id) {
        Form form = formRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Form with ID " + id + " not found."));
        form.setPublished(!form.isPublished());
        formRepository.save(form);
    }

    @Override
    public FormDTO updateFields(Long formId, List<FieldDTO> fieldDTOs) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new ResourceNotFoundException("Form with ID " + formId + " not found."));
        List<Field> existingFields = new ArrayList<>(form.getFields());
        for (Field field : existingFields) {
            form.removeField(field);
        }

        for (FieldDTO fieldDTO : fieldDTOs) {
            Field field = new Field();
            field.setId(fieldDTO.getId());
            field.setName(fieldDTO.getName());
            field.setLabel(fieldDTO.getLabel());
            field.setType(fieldDTO.getType());
            field.setDefaultValue(fieldDTO.getDefaultValue());
            form.addField(field);
        }
        form = formRepository.save(form);
        return toDTO(form);
    }
}
