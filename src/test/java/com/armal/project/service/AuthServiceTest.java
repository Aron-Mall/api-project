package com.armal.project.service;


import com.armal.project.exceptions.NoAccessException;
import com.armal.project.exceptions.NoFormFoundException;
import com.armal.project.model.Address;
import com.armal.project.model.Form;
import com.armal.project.model.PersonalDetails;
import com.armal.project.repository.FormRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    FormRepository formRepository;

    @InjectMocks
    AuthService authService;

    private Form form;

    @BeforeEach
    void setUp() {
        form = new Form(47L,new PersonalDetails(),new Address(), "B680CD12F44E79199FAD6263CD16CD1B", false);
    }


    @Test
    void givenUserSessionMismatch_whenCheckingFormBelongingToUser_thenThrowsNoAccessException() throws NoAccessException, NoFormFoundException {

        //Given
        final Long formId = 47L;
        Mockito.when(formRepository.findByFormId(formId))
                .thenReturn(Optional.of(form));
        //When
        Assertions.assertThrows(NoAccessException.class,() -> authService.checkFormBelongsToUserSession(formId,"17DCE40A5E5DBA10C941AB93F4638515"));

        //Then
        Mockito.verify(formRepository, Mockito.times(1)).findByFormId(formId);
    }

    @Test
    void givenInvalidFormId_whenCheckingFormBelongingToUser_thenThrowsNoFormFoundException() throws NoAccessException, NoFormFoundException {

        //Given
        final Long formId = 47L;
        Mockito.when(formRepository.findByFormId(formId))
                        .thenReturn(Optional.empty());
        //When
        Assertions.assertThrows(NoFormFoundException.class,() -> authService.checkFormBelongsToUserSession(47L,"17DCE40A5E5DBA10C941AB93F4638515"));

        //Then
        Mockito.verify(formRepository, Mockito.times(1)).findByFormId(formId);
    }




}
