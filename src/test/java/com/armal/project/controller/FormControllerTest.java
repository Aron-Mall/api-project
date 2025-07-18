package com.armal.project.controller;

import com.armal.project.model.Address;
import com.armal.project.model.Form;
import com.armal.project.model.PersonalDetails;
import com.armal.project.service.AuthService;
import com.armal.project.service.FormService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.json.JsonCompareMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

import static com.armal.project.controller.FormController.Endpoints.*;
import static org.mockito.Mockito.times;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {FormController.class})
public class FormControllerTest {


    @MockitoBean
    AuthService authService;

    @MockitoBean
    FormService formService;

    @Autowired
    private MockMvc mockMvc;


    private Form form;
    private Address address;
    private PersonalDetails personalDetails;
    private String contract;

    static RequestPostProcessor httpBasicAuth = httpBasic("apiusertest", "P@$sW0rDTe$T");
    @BeforeEach
    void setUp() {
        final Long formId = 101L;
        address = new Address(1L,formId,40,"TEST_STREET","TEST_CITY", "TEST_POSTCODE");
        personalDetails = new PersonalDetails(1L,formId,"Peter", "Parker", "pp@test.com");
        form = new Form(formId,personalDetails,address,"B680CD12F44E79199FAD6263CD16CD1B",false);

        contract =  """
                {
                  "formId": 101,
                  "personalDetails": {
                    "formId":101,
                    "firstName": "Peter",
                    "lastName": "Parker",
                    "email": "pp@test.com"
                  },
                  "address": {
                    "formId":101,
                    "houseNumber": 40,
                    "street": "TEST_STREET",
                    "city": "TEST_CITY",
                    "postCode": "TEST_POSTCODE"
                  },
                  "finalized": false
                }
                """;
    }

    @Test
    void givenFormId_whenGettingForm_thenReturnForm() throws Exception {
        //Given
        Long formId = 101L;
        Mockito.when(formService.getFormById(formId))
                .thenReturn(form);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(BASE_MAPPING + VIEW_FORM_MAPPING, formId)
                .with(httpBasicAuth);

        //When
        ResultActions result = mockMvc.perform(request);

        //Then
        result.andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().json(contract, JsonCompareMode.STRICT));

        Mockito.verify(authService,times(1)).checkFormBelongsToUserSession(Mockito.anyLong(),Mockito.anyString());
        Mockito.verify(formService,times(1)).getFormById(formId);
    }

    @Test
    void givenFormId_whenGettingFormProgress_thenReturnForm() throws Exception {
        //Given
        Long formId = 101L;

        Mockito.when(formService.getFormProgressById(formId))
                .thenReturn(Optional.of(form));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(BASE_MAPPING + PROGRESS_MAPPING, formId)
                .with(httpBasicAuth);

        //When
        ResultActions result = mockMvc.perform(request);

        //Then
        result.andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().json(contract, JsonCompareMode.STRICT));

        Mockito.verify(authService,times(1)).checkFormBelongsToUserSession(Mockito.anyLong(),Mockito.anyString());
        Mockito.verify(formService,times(1)).getFormProgressById(formId);
    }

    @Test
    void givenPostRequest_whenCreatingForm_thenReturnStatusCreatedAndFormId() throws Exception {

        //Given
        final Long formId = 47L;
        Mockito.when(formService.createForm(Mockito.anyString()))
                .thenReturn(47L);
        MockHttpServletRequestBuilder post = MockMvcRequestBuilders.post(BASE_MAPPING+ CREATE_FORM_MAPPING)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(httpBasicAuth);

        URI expedtedUri = UriComponentsBuilder.newInstance().path(BASE_MAPPING + FORM_MAPPING)
                .build(formId);

        //When
        MvcResult mvcResult = mockMvc.perform(post)
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("47"))
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andReturn();

        //Then
        String uriToCreatedForm = mvcResult.getResponse().getHeader("Location");
        Assertions.assertTrue(uriToCreatedForm.contains(expedtedUri.toString()));
        Mockito.verify(formService,times(1)).createForm(Mockito.anyString());

    }

    @Test
    void givenPostRequest_whenSubmittingForm_thenReturnSubmitted() throws Exception {

        //Given
        final Long formId = 47L;
        MockHttpServletRequestBuilder post = MockMvcRequestBuilders.post(BASE_MAPPING + SUBMIT_MAPPING, formId)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(httpBasicAuth);

        //When
        mockMvc.perform(post)
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().string("Form submitted"));

        //Then
        Mockito.verify(authService,times(1)).checkFormBelongsToUserSession(Mockito.anyLong(),Mockito.anyString());
        Mockito.verify(formService,times(1)).submitForm(formId);
    }

}
