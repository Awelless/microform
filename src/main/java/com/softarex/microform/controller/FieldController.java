package com.softarex.microform.controller;

import com.softarex.microform.domain.field.Field;
import com.softarex.microform.dto.PageDto;
import com.softarex.microform.service.FieldService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/fields")
@AllArgsConstructor
public class FieldController {
    private final FieldService fieldService;

    @GetMapping
    public PageDto<Field> getAllFields(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return fieldService.getAll(pageable);
    }

    @GetMapping("/active")
    public List<Field> getActiveFields() {
        return fieldService.getActive();
    }

    @PostMapping
    public ResponseEntity<?> createField(@Valid @RequestBody Field field, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ControllerUtils.getErrorResponse(bindingResult);
        }

        //Field options validation is handled in the fieldService
        try {
            Field fieldFromDb = fieldService.create(field);
            return ResponseEntity.ok(fieldFromDb);
        } catch (IllegalArgumentException e) {
            return ControllerUtils.getErrorResponse(e, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateField(
            @PathVariable Long id,
            @Valid @RequestBody Field newField,
            BindingResult bindingResult
    ) {
        Field oldField = fieldService.getById(id);

        if (oldField == null) {
            return ControllerUtils.getErrorResponse("Updated field doesn't exist. Please, reload page", HttpStatus.CONFLICT);
        }

        if (bindingResult.hasErrors()) {
            return ControllerUtils.getErrorResponse(bindingResult);
        }

        //Field options validation is handled in the fieldService
        try {
            Field fieldFromDb = fieldService.update(oldField, newField);
            return ResponseEntity.ok(fieldFromDb);
        } catch (IllegalArgumentException e) {
            return ControllerUtils.getErrorResponse(e, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteField(@PathVariable("id") Field field) {
        fieldService.delete(field);
    }
}
