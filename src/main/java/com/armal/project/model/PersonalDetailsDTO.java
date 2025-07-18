package com.armal.project.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PersonalDetailsDTO(
        @NotNull(message = "Form id cannot be null")
        @Min(value = 1, message = "Provide a form id")
        Long formId,
        @NotNull(message = "Firstname cannot be null")
        @NotBlank(message = "Firstname cannot be blank")
        String firstName,
        @NotNull(message = "Lastname cannot be null")
        @NotBlank(message = "Lastname cannot be blank")
        String lastName,
        @Email(message = "Provide a valid email format")
        @NotBlank(message = "Email cannot be blank")
        String email) {
}
