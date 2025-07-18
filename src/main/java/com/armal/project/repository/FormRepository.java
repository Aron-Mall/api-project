package com.armal.project.repository;

import com.armal.project.model.Form;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FormRepository extends CrudRepository<Form, Long> {

    Optional<Form> findByFormId(Long formId);

}
