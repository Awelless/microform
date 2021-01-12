package com.softarex.internship.controller;

import com.softarex.internship.domain.field.Field;
import com.softarex.internship.service.FieldService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public Field createField(@RequestBody Field field) {
        return fieldService.create(field);
    }

    @PutMapping("/{id}")
    public Field updateField(
            @RequestParam("id") Field oldField,
            @RequestBody Field newField
    ) {
        return fieldService.update(oldField, newField);
    }

    @DeleteMapping("/{id}")
    public void deleteField(@RequestParam("id") Field field) {
        fieldService.delete(field);
    }
}
