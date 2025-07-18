package com.armal.project.model;

import jakarta.validation.constraints.NotNull;

public record AddressDTO(
        @NotNull(message = "Form id cannot be null") Long formId,
        @NotNull(message = "House number cannot be null") Integer houseNumber,
        @NotNull(message = "Street cannot be null")String street,
        String city,
        @NotNull(message = "Postcode cannot be null")String postCode) {
}

