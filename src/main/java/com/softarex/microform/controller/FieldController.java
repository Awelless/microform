package com.softarex.microform.controller;

import com.softarex.microform.domain.field.Field;
import com.softarex.microform.service.FieldService;
import lombok.AllArgsConstructor;
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
    public List<Field> getAllFields() {
        return fieldService.getAll();
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
            @PathVariable("id") Field oldField,
            @Valid @RequestBody Field newField,
            BindingResult bindingResult
    ) {
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
