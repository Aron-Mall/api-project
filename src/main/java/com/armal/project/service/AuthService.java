package com.armal.project.service;

import com.armal.project.exceptions.NoAccessException;
import com.armal.project.exceptions.NoFormFoundException;
import com.armal.project.model.Form;
import com.armal.project.repository.FormRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final FormRepository formRepository;

    public AuthService(FormRepository formRepository) {
        this.formRepository = formRepository;
    }

    public void checkFormBelongsToUserSession(Long formId, String sessionId) throws NoFormFoundException, NoAccessException {
        Form form = formRepository.findByFormId(formId)
                .orElseThrow(NoFormFoundException::new);

        if(!form.getSessionId().equals(sessionId)) {
            throw new NoAccessException();
        }
    }

}
