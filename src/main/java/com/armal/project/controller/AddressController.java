package com.armal.project.controller;


import com.armal.project.exceptions.FormFinalizedExecption;
import com.armal.project.exceptions.FormSequenceException;
import com.armal.project.exceptions.NoFormFoundException;
import com.armal.project.model.Address;
import com.armal.project.model.AddressDTO;
import com.armal.project.service.AddressService;
import com.armal.project.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static com.armal.project.controller.AddressController.Endpoints.*;

@Validated
@RestController
@RequestMapping(BASE_MAPPING)
public class AddressController {

    public static final class Endpoints {
        public static final String BASE_MAPPING = "/form";
        public static final String ADDRESS_MAPPING = "/address/{formId}";
        public static final String SAVE_ADDRESS_MAPPING = "/save/address";
    }

    private final AuthService authService;

    private final AddressService addressService;

    public AddressController(AuthService authService, AddressService addressService) {
        this.authService = authService;
        this.addressService = addressService;
    }

    @GetMapping(ADDRESS_MAPPING)
    public AddressDTO getAddress(@PathVariable("formId") Long formId, HttpSession session) throws Exception {
        authService.checkFormBelongsToUserSession(formId, session.getId());
        return  addressService.getAddressByFormId(formId);
    }

    @PostMapping(SAVE_ADDRESS_MAPPING)
    public ResponseEntity<Void> saveAddress(@Valid @RequestBody AddressDTO addressDTO, UriComponentsBuilder builder) throws FormFinalizedExecption, NoFormFoundException, FormSequenceException {
        addressService.saveAddress(addressDTO);

        URI uri = builder.path(BASE_MAPPING + ADDRESS_MAPPING)
                .build(addressDTO.formId());

        return ResponseEntity.created(uri).build();
    }
}
