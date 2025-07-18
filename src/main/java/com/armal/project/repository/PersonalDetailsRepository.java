package com.armal.project.repository;

import com.armal.project.model.PersonalDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PersonalDetailsRepository extends JpaRepository<PersonalDetails, Long> {

    @Query("""
    SELECT p FROM PersonalDetails p
    WHERE p.formId = :formId
 """)
    Optional<PersonalDetails> findByFormId(@Param("formId") Long formId);
}
