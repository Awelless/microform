package com.softarex.microform.repository;

import com.softarex.microform.domain.field.Field;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FieldRepository extends JpaRepository<Field, Long> {
    Page<Field> findAll(Pageable pageable);

    @Query("from Field f where f.active = true")
    List<Field> findActive();
}
