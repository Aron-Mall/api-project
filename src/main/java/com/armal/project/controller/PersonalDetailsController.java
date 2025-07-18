package com.armal.project.controller;


import com.armal.project.exceptions.FormFinalizedExecption;
import com.armal.project.exceptions.NoFormFoundException;
import com.armal.project.model.PersonalDetailsDTO;
import com.armal.project.service.AuthService;
import com.armal.project.service.PersonalDetailsService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static com.armal.project.controller.PersonalDetailsController.Endpoints.*;

@Validated
@RestController
@RequestMapping(BASE_MAPPING)
public class PersonalDetailsController {

    public static final class Endpoints {
        public static final String BASE_MAPPING = "/form";
        public static final String PERSONAL_DETAILS_MAPPING = "/personal-details/{formId}";
        public static final String SAVE_PERSONAL_DETAILS_MAPPING = "/save/personal-details";
    }

    private  final AuthService authService;
    private final PersonalDetailsService personalDetailsService;

    public PersonalDetailsController(AuthService authService, PersonalDetailsService personalDetailsService) {
        this.authService = authService;
        this.personalDetailsService = personalDetailsService;
    }

    @GetMapping(PERSONAL_DETAILS_MAPPING)
    public PersonalDetailsDTO getPersonalDetails(@PathVariable("formId") Long formId, HttpSession session) throws Exception {

        authService.checkFormBelongsToUserSession(formId,session.getId());

        return personalDetailsService.getPersonalDetailsByFormId(formId);
    }

    @PostMapping(SAVE_PERSONAL_DETAILS_MAPPING)
    public ResponseEntity<Void> savePersonalDetails(@Valid @RequestBody PersonalDetailsDTO personalDetailsDTO, BindingResult bindingResult,
                                                    UriComponentsBuilder builder) throws FormFinalizedExecption, NoFormFoundException {
        personalDetailsService.savePersonalDetails(personalDetailsDTO);

        URI uri = builder.path(BASE_MAPPING + PERSONAL_DETAILS_MAPPING)
                .build(personalDetailsDTO.formId());

        return ResponseEntity.created(uri).build();
    }

}
