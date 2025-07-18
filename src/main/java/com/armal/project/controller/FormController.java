package com.armal.project.controller;
import com.armal.project.exceptions.FormFinalizedExecption;
import com.armal.project.exceptions.FormIncompleteException;
import com.armal.project.exceptions.NoAccessException;
import com.armal.project.exceptions.NoFormFoundException;
import com.armal.project.model.Form;
import com.armal.project.service.AuthService;
import com.armal.project.service.FormService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static com.armal.project.controller.FormController.Endpoints.*;

@RestController
@RequestMapping(BASE_MAPPING)
public class FormController {

    public static final class Endpoints {
        public static final String BASE_MAPPING = "/form";
        public static final String FORM_MAPPING = "/{formId}";
        public static final String VIEW_FORM_MAPPING = "/view/{formId}";
        public static final String CREATE_FORM_MAPPING = "/create";
        public static final String PROGRESS_MAPPING = "/progress" + FORM_MAPPING;
        public static final String SUBMIT_MAPPING = "/submit" + FORM_MAPPING;
    }

    private final AuthService authService;
    private final FormService formService;

    public FormController(AuthService authService, FormService formService) {
        this.authService = authService;
        this.formService = formService;
    }

    @GetMapping(VIEW_FORM_MAPPING)
    public Form getFrom(@PathVariable("formId") Long formId, HttpSession session) throws NoFormFoundException, NoAccessException {
        authService.checkFormBelongsToUserSession(formId,session.getId());
        return formService.getFormById(formId);
    }


    @GetMapping(PROGRESS_MAPPING)
    public Form getProgress(@PathVariable("formId") Long formId, HttpSession session) throws NoFormFoundException, NoAccessException {
        // this should not have finalized
        // map to a record
        authService.checkFormBelongsToUserSession(formId,session.getId());
        return formService.getFormProgressById(formId)
                .orElseThrow(NoFormFoundException::new);

    }

    @PostMapping(CREATE_FORM_MAPPING)
    public ResponseEntity<Long> createForm(HttpSession session, UriComponentsBuilder builder) {

        Long formId = formService.createForm(session.getId());

        URI uri = builder.path(BASE_MAPPING + FORM_MAPPING)
                .build(formId);

        return ResponseEntity.created(uri).body(formId);
    }

    @PostMapping(SUBMIT_MAPPING)
    public ResponseEntity<String> submitForm(@PathVariable("formId") Long formId, HttpSession session) throws NoAccessException, NoFormFoundException, FormFinalizedExecption, FormIncompleteException {
        authService.checkFormBelongsToUserSession(formId,session.getId());
        formService.submitForm(formId);
        return ResponseEntity.ok("Form submitted");
    }


}
