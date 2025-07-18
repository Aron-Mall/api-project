package com.armal.project.controller;

import com.armal.project.model.PersonalDetails;
import com.armal.project.model.PersonalDetailsDTO;
import com.armal.project.service.AuthService;
import com.armal.project.service.FormService;
import com.armal.project.service.PersonalDetailsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.json.JsonCompareMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.UriComponentsBuilder;


import java.net.URI;
import java.util.Optional;

import static com.armal.project.controller.FormControllerTest.httpBasicAuth;
import static com.armal.project.controller.PersonalDetailsController.Endpoints.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

@WebMvcTest(controllers = {PersonalDetailsController.class})
public class PersonalDetailsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    AuthService authService;

    @MockitoBean
    PersonalDetailsService personalDetailsService;

    @MockitoBean
    FormService formService;

    private PersonalDetailsDTO personalDetails;

    private String contract;

    @BeforeEach
    void setUp() throws Exception {
        final Long formId = 101L;
        personalDetails = new PersonalDetailsDTO(formId,"Peter", "Parker", "pp@test.com");

        contract = """
            {
                "formId":101,
                "firstName": "Peter",
                "lastName": "Parker",
                "email": "pp@test.com"
            }
            """;
    }

    @Test
    void givenFormId_whenGetPersonalDetails_ThenReturnPersonalDetails() throws Exception {

        //Given
        final Long formId = 101L;
        Mockito.when(personalDetailsService.getPersonalDetailsByFormId(formId))
                .thenReturn(personalDetails);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(BASE_MAPPING + PERSONAL_DETAILS_MAPPING, formId)
                .with(httpBasicAuth);

        //When
        ResultActions result = mockMvc.perform(request);

        //Then
        result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().json(contract, JsonCompareMode.STRICT));

        Mockito.verify(authService, Mockito.times(1)).checkFormBelongsToUserSession(Mockito.anyLong(), Mockito.anyString());
        Mockito.verify(personalDetailsService, Mockito.times(1)).getPersonalDetailsByFormId(formId);

    }

    @Test
    void givenPersonalDetailsDTO_whenSubmittingPersonalDetails_ThenReturnStatusCreated() throws Exception {


        //Given
        final Long formId = 101L;

        URI expedtedUri = UriComponentsBuilder.newInstance().path(BASE_MAPPING + PERSONAL_DETAILS_MAPPING)
                .build(formId);

        final String personalDetailsJson = """
            {
                "formId":101,
                "firstName": "Jane",
                "lastName": "Doe",
                "email": "jane.doe@test.com"
            }
                """;

        MockHttpServletRequestBuilder post = MockMvcRequestBuilders.post(BASE_MAPPING + SAVE_PERSONAL_DETAILS_MAPPING,formId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personalDetailsJson)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(httpBasicAuth);


        //When
        ResultActions result = mockMvc.perform(post);

        //Then
        MvcResult response = result.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andReturn();

        String uriToCreatedPersonalDetails = response.getResponse().getHeader("Location");

        Assertions.assertTrue(uriToCreatedPersonalDetails.endsWith(expedtedUri.toString()));

    }
}
