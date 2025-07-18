package com.armal.project.service;

import com.armal.project.exceptions.FormFinalizedExecption;
import com.armal.project.exceptions.FormIncompleteException;
import com.armal.project.exceptions.NoFormFoundException;
import com.armal.project.model.Form;
import com.armal.project.repository.FormRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class FormService {

    private final FormRepository formRepository;

    public FormService(FormRepository formRepository) {
        this.formRepository = formRepository;
    }

    public Form getFormById(Long formId) throws NoFormFoundException {
        return formRepository.findByFormId(formId)
                .orElseThrow(NoFormFoundException::new);
    }

    public Optional<Form> getFormProgressById(Long formId) {
        // needs to only return the current progress so only address or only personal details
        return formRepository.findByFormId(formId);
    }

    @Transactional
    public Long createForm(String sessionId) {
        Form form = new Form();
        form.setSessionId(sessionId);
        Form savedForm = formRepository.save(form);
        return savedForm.getFormId();
    }

    @Transactional
    public void submitForm(Long formId) throws NoFormFoundException, FormFinalizedExecption, FormIncompleteException {
        Form form = getFormById(formId);

        if(form.isFinalized()) {
            throw new FormFinalizedExecption();
        }

        if(Objects.isNull(form.getPersonalDetails()) || Objects.isNull(form.getAddress())) {
            throw new FormIncompleteException();
        }

        if(Objects.nonNull(form.getAddress()) && Objects.nonNull(form.getPersonalDetails())) {
                form.setFinalized(true);
                formRepository.save(form);
        }

    }

    @Transactional
    public void saveForm(Form form) {
        formRepository.save(form);
    }

}
