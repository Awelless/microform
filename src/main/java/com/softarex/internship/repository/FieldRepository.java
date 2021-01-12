package com.softarex.internship.repository;

import com.softarex.internship.domain.field.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FieldRepository extends JpaRepository<Field, Long> {
    @Query("from Field f where f.active = true")
    List<Field> findActive();
}
