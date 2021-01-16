package com.softarex.microform.repository;

import com.softarex.microform.domain.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface ResponseRepository extends JpaRepository<Response, Long> {
    @Transactional
    @Modifying
    @Query(
            nativeQuery = true,
            value = "delete from response_field where field_id = :id"
    )
    void clearFieldsByFieldId(@Param("id") Long id);
}
