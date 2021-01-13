package com.softarex.internship.service;

import com.softarex.internship.domain.field.Field;
import com.softarex.internship.domain.field.FieldType;
import com.softarex.internship.repository.FieldRepository;
import com.softarex.internship.repository.ResponseRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
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

    /**
     * @return All fields where active == true
     */
    public List<Field> getActive() {
        return fieldRepository.findActive();
    }

    /**
     * Validates field options (if exist) and creates new Field
     * @return Created field from DB
     */
    public Field create(@NonNull final Field field) {
        if (isInvalid(field)) {
            throw new IllegalArgumentException("You should choose at least 1 option");
        }

        return fieldRepository.save(field);
    }

    /**
     * Validates newField options (if exist) and updates base field
     * @param field The Field which is updated
     * @param newField New version of the field
     * @return Updated Field from DB
     */
    public Field update(@NonNull Field field, @NonNull final Field newField) {
        if (isInvalid(newField)) {
            throw new IllegalArgumentException("You should choose at least 1 option");
        }

        BeanUtils.copyProperties(newField, field, "id", "options");
        field.setOptions(newField.getOptions());

        return fieldRepository.save(field);
    }

    /**
     * Deletes field and all response properties binded this field
     * @param field The Filed which is deleted
     */
    public void delete(@NonNull final Field field) {
        //Deleting all response properties which are binded with deleted field
        responseRepository.clearFieldsByFieldId(field.getId());

        fieldRepository.delete(field);
    }

    private boolean isInvalid(@NonNull final Field field) {
        //If field doesn't have options, it'll be 100% valid
        if (!field.getType().equals(FieldType.COMBOBOX) && !field.getType().equals(FieldType.RADIO_BUTTON)) {
            return false;
        }

        if (field.getOptions() == null || field.getOptions().size() == 0) {
            return true;
        }

        //Validating every option
        field.getOptions().forEach(option -> {
            if (option == null || option.isBlank()) {
                throw new IllegalArgumentException("Field option shouldn't be blank");
            }
            if (option.length() > 255) {
                throw new IllegalArgumentException("Field option shouldn't be longer than 255");
            }
        });

        return false;
    }
}
