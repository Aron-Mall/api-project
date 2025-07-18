package com.armal.project.service;

import com.armal.project.exceptions.FormFinalizedExecption;
import com.armal.project.exceptions.FormSequenceException;
import com.armal.project.exceptions.NoAddressFoundException;
import com.armal.project.exceptions.NoFormFoundException;
import com.armal.project.model.Address;
import com.armal.project.model.AddressDTO;
import com.armal.project.model.Form;
import com.armal.project.model.PersonalDetails;
import com.armal.project.repository.AddressRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    private final FormService formService;

    public AddressService(AddressRepository addressRepository, FormService formService) {
        this.addressRepository = addressRepository;
        this.formService = formService;
    }

    public AddressDTO getAddressByFormId(Long formId) throws NoAddressFoundException {
        Address address = addressRepository.findAddressByFormId(formId)
                .orElseThrow(NoAddressFoundException::new);
        return new AddressDTO(
                address.getFormId(),
                address.getHouseNumber(),
                address.getStreet(),
                address.getCity(),
                address.getPostCode()
        );
    }

    public void saveAddress(AddressDTO dto) throws NoFormFoundException, FormSequenceException, FormFinalizedExecption {
        Form form = formService.getFormById(dto.formId());
        Address address = form.getAddress();

        if(Objects.isNull(form.getPersonalDetails())) {
              throw new FormSequenceException();
        }

        if(form.isFinalized()) {
            throw new FormFinalizedExecption();
        }

        if(Objects.isNull(address)) {
            address = new Address();
        }

        address.setFormId(dto.formId());
        address.setHouseNumber(dto.houseNumber());
        address.setStreet(dto.street());
        address.setCity(dto.city());
        address.setPostCode(dto.postCode());

        form.setAddress(address);
        formService.saveForm(form);
    }
}
