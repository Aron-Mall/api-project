package com.armal.project.service;

import com.armal.project.exceptions.FormFinalizedExecption;
import com.armal.project.exceptions.NoFormFoundException;
import com.armal.project.exceptions.NoPersonalDetailsFoundException;
import com.armal.project.model.Form;
import com.armal.project.model.PersonalDetails;
import com.armal.project.model.PersonalDetailsDTO;
import com.armal.project.repository.PersonalDetailsRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class PersonalDetailsService {

    private final PersonalDetailsRepository personalDetailsRepository;
    private final FormService formService;

    public PersonalDetailsService(PersonalDetailsRepository personalDetailsRepository, FormService formService) {
        this.personalDetailsRepository = personalDetailsRepository;
        this.formService = formService;
    }


    public PersonalDetailsDTO getPersonalDetailsByFormId(Long formId) throws NoPersonalDetailsFoundException {
        PersonalDetails personalDetails = personalDetailsRepository.findByFormId(formId)
                .orElseThrow(NoPersonalDetailsFoundException::new);
        return  new PersonalDetailsDTO(
                personalDetails.getFormId(),
                personalDetails.getFirstName(),
                personalDetails.getLastName(),
                personalDetails.getEmail()
        );
    }


    @Transactional
    public void savePersonalDetails(PersonalDetailsDTO dto) throws NoFormFoundException, FormFinalizedExecption {
        Form form = formService.getFormById(dto.formId());
        PersonalDetails personalDetails = form.getPersonalDetails();

        if(form.isFinalized()) {
            throw new FormFinalizedExecption();
        }

        if(Objects.isNull(personalDetails)) {
            personalDetails = new PersonalDetails();
        }
        personalDetails.setFormId(dto.formId());
        personalDetails.setFirstName(dto.firstName());
        personalDetails.setLastName(dto.lastName());
        personalDetails.setEmail(dto.email());

        form.setPersonalDetails(personalDetails);
        formService.saveForm(form);

    }
}

