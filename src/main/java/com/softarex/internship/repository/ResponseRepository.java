package com.softarex.internship.repository;

import com.softarex.internship.domain.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ResponseRepository extends JpaRepository<Response, Long> {
    @Query(
            nativeQuery = true,
            value = "delete from response_field where field_id = :id"
    )
    void clearFieldsByFieldId(@Param("id") Long id);
}
