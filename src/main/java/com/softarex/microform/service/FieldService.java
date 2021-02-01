package com.softarex.microform.service;

import com.softarex.microform.domain.field.Field;
import com.softarex.microform.domain.field.FieldType;
import com.softarex.microform.dto.PageDto;
import com.softarex.microform.repository.FieldRepository;
import com.softarex.microform.repository.ResponseRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@Service
@AllArgsConstructor
public class FieldService {
    private final FieldRepository fieldRepository;
    private final ResponseRepository responseRepository;

    public Field getById(final long id) {
        return fieldRepository.findById(id).orElse(null);
    }

    /**
     * @return Page assigned to number
     */
    public PageDto<Field> getAll(Pageable pageable) {
        Page<Field> page = fieldRepository.findAll(pageable);

        return new PageDto<>(
                page.getContent(),
                pageable.getPageNumber(),
                page.getTotalPages()
        );
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
        checkValidity(field);

        return fieldRepository.save(field);
    }

    /**
     * Validates newField options (if exist) and updates base field
     * @param field The Field which is updated
     * @param newField New version of the field
     * @return Updated Field from DB
     */
    public Field update(@NonNull Field field, @NonNull final Field newField) {
        checkValidity(newField);

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

    /**
     * Further validation of field. Throws exception if it isn't valid
     * @param field - Response to validate
     */
    private void checkValidity(@NonNull final Field field) {
        if (field.isRequired() && !field.isActive()) {
            throw new IllegalArgumentException(
                    "Field can't be required and inactive at the same time");
        }

        //If field doesn't have options, it'll be 100% valid
        if (!field.getType().equals(FieldType.COMBOBOX) &&
                !field.getType().equals(FieldType.RADIO_BUTTON)) {
            return;
        }

        if (field.getOptions() == null || field.getOptions().size() == 0) {
            throw new IllegalArgumentException("You should set at least 1 option");
        }

        //Validating every option
        field.getOptions().forEach(option -> {
            if (StringUtils.isEmptyOrWhitespace(option)) {
                throw new IllegalArgumentException("Field option shouldn't be blank");
            }
            if (option.length() > 255) {
                throw new IllegalArgumentException("Field option shouldn't be longer than 255");
            }
        });
    }
}
