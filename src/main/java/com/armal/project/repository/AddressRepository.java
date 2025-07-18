package com.armal.project.repository;

import com.armal.project.model.Address;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AddressRepository extends CrudRepository<Address, Long> {

    @Query("""
    SELECT a FROM Address a
    WHERE a.formId = :formId
""")
    Optional<Address> findAddressByFormId(@Param("formId") Long formId);

}
