package com.example.dt.service;

import com.example.dt.dto.FieldDTO;
import com.example.dt.dto.FormDTO;
import com.example.dt.model.Form;
import java.util.List;

public interface FormService {
    List<FormDTO> getAllForms();
    FormDTO getFormById(Long id);
    FormDTO createForm(FormDTO formDTO);
    FormDTO updateForm(Long id, FormDTO formDTO);
    void deleteForm(Long id);
    List<FormDTO> getPublishedForms();
    void toggleFormPublishStatus(Long id);
    FormDTO updateFields(Long formId, List<FieldDTO> fieldDTOs);
}
