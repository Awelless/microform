package com.softarex.internship.service;

import com.softarex.internship.domain.field.Field;
import com.softarex.internship.repository.FieldRepository;
import com.softarex.internship.repository.ResponseRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FieldService {
    private final FieldRepository fieldRepository;
    private final ResponseRepository responseRepository;

    public List<Field> getAll() {
        return fieldRepository.findAll();
    }

    public List<Field> getActive() {
        return fieldRepository.findActive();
    }

    public Field create(@NonNull final Field field) {
        return fieldRepository.save(field);
    }

    public Field update(@NonNull Field oldField, @NonNull final Field newField) {
        BeanUtils.copyProperties(newField, oldField, "id");
        return fieldRepository.save(oldField);
    }

    public void delete(@NonNull final Field field) {
        //Deleting all response properties with deleted field
        responseRepository.clearFieldsByFieldId(field.getId());

        fieldRepository.delete(field);
    }
}
