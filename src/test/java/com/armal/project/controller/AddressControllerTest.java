package com.armal.project.controller;

import com.armal.project.model.Address;
import com.armal.project.model.AddressDTO;
import com.armal.project.service.AddressService;
import com.armal.project.service.AuthService;
import com.armal.project.service.FormService;
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
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

import static com.armal.project.controller.AddressController.Endpoints.*;
import static com.armal.project.controller.FormControllerTest.httpBasicAuth;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

@WebMvcTest(controllers = {AddressController.class})
public class AddressControllerTest {

    @MockitoBean
    AuthService authService;

    @MockitoBean
    AddressService addressService;

    @MockitoBean
    FormService formService;

    @Autowired
    private MockMvc mockMvc;

    private AddressDTO address;

    private String expectedJson;

    @BeforeEach
    void setUp() {
        final Long formId = 101L;
        address = address = new AddressDTO(formId,40,"TEST_STREET","TEST_CITY", "TEST_POSTCODE");

        expectedJson = """
                {
                    "formId":101,
                    "houseNumber": 40,
                    "street": "TEST_STREET",
                    "city": "TEST_CITY",
                    "postCode": "TEST_POSTCODE"
                }
                """;
      }

    @Test
    void givenFormId_whenGettingAddress_thenReturnAddress() throws Exception {

        //Given
        final Long formId = 101L;
        Mockito.when(addressService.getAddressByFormId(formId))
                .thenReturn(address);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(BASE_MAPPING + ADDRESS_MAPPING, formId)
                .with(httpBasicAuth);

        //When
        ResultActions result = mockMvc.perform(request);

        //Then
        result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().json(expectedJson, JsonCompareMode.STRICT));

        Mockito.verify(authService, Mockito.times(1)).checkFormBelongsToUserSession(Mockito.anyLong(), Mockito.anyString());
        Mockito.verify(addressService, Mockito.times(1)).getAddressByFormId(formId);

    }

    @Test
    void givenAddressDTO_whenSubmittingAddress_thenReturnCreated() throws Exception {
        //Given
        final Long formId = 101L;

        URI expedtedUri = UriComponentsBuilder.newInstance().path(BASE_MAPPING + ADDRESS_MAPPING)
                .build(formId);

        final String addressJson = """
            {
               "formId":101,
               "houseNumber": 40,
               "street": "TEST_STREET",
               "city": "TEST_CITY",
               "postCode": "TEST_POSTCODE"  
            }
            """;

        MockHttpServletRequestBuilder post = MockMvcRequestBuilders.post(BASE_MAPPING + SAVE_ADDRESS_MAPPING, formId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(addressJson)
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
