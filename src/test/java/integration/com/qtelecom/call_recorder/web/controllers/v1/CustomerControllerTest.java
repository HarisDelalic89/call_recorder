package com.qtelecom.call_recorder.web.controllers.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qtelecom.call_recorder.exceptions.ResourceNotFoundException;
import com.qtelecom.call_recorder.services.CustomerService;
import com.qtelecom.call_recorder.web.dtos.CustomerDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.core.Is.is;

@WebMvcTest(CustomerController.class)
@AutoConfigureMockMvc
class CustomerControllerTest {
    private static final Integer NON_EXISTING_CUSTOMER_ID = 2;
    private static final String CUSTOMER_NOT_FOUND_MSG = "Customer with ID: " + NON_EXISTING_CUSTOMER_ID + " Not Found";

    @MockBean
    CustomerService customerService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    CustomerDto customerDto;

    @BeforeEach
    void setUp() {
        customerDto = CustomerDto.builder()
                .id(1)
                .email("firstName_lastName@test.com")
                .firstName("firstName")
                .lastName("lastname")
                .age(30)
                .build();
    }

    @AfterEach
    void tearDown() {
        reset(customerService);
    }

    @Nested
    @DisplayName("Section: Get Customers")
    class AllCustomersRetrieveTest {
        @Test
        void testGetCustomers() throws Exception {

            mockMvc.perform(get("/v1/customers"))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("Section: Get Customer By Id")
    class IndividualCustomerRetrieveTest {
        @Test
        void shouldReturn200_WhenCustomerDoesExist() throws Exception {
            when(customerService.getCustomerById(customerDto.getId()))
                    .thenReturn(customerDto);

            mockMvc.perform(get("/v1/customers/" + customerDto.getId()))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(customerDto.getId())))
                    .andExpect(jsonPath("$.email", is(customerDto.getEmail())))
                    .andExpect(jsonPath("$.firstName", is(customerDto.getFirstName())))
                    .andExpect(jsonPath("$.lastName", is(customerDto.getLastName())))
                    .andExpect(jsonPath("$.age", is(customerDto.getAge())));
        }

        @Test
        void shouldReturn404_WhenCustomerDoesNotExist() throws Exception {
            when(customerService.getCustomerById(NON_EXISTING_CUSTOMER_ID))
                    .thenThrow(new ResourceNotFoundException(CUSTOMER_NOT_FOUND_MSG));

            mockMvc.perform(get("/v1/customers/"+NON_EXISTING_CUSTOMER_ID))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", is(CUSTOMER_NOT_FOUND_MSG)))
                    .andExpect(jsonPath("$.statusCode", is(HttpStatus.NOT_FOUND.value())));
        }
    }

    @Nested
    @DisplayName("Section: Create Customer")
    class CustomerCreateTest {
        @Test
        void shouldReturn201_WhenCustomerIsCreated() throws Exception {
            when(customerService.create(customerDto)).thenReturn(customerDto);

            mockMvc.perform(post("/v1/customers")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(customerDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id", is(customerDto.getId())))
                    .andExpect(jsonPath("$.email", is(customerDto.getEmail())))
                    .andExpect(jsonPath("$.firstName", is(customerDto.getFirstName())))
                    .andExpect(jsonPath("$.lastName", is(customerDto.getLastName())))
                    .andExpect(jsonPath("$.age", is(customerDto.getAge())));
        }

        @Test
        void shouldReturn400_whenCustomerIsNotCreatedDueToValidationErrors() throws Exception {
            customerDto.setEmail("");

            mockMvc.perform(post("/v1/customers")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(customerDto)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Section: Update Customer")
    class CustomerUpdateTest {
        @Test
        void shouldReturn200_WhenCustomerIsUpdated() throws Exception {
            when(customerService.update(customerDto.getId(), customerDto)).thenReturn(customerDto);

            mockMvc.perform(put("/v1/customers/" + customerDto.getId())
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(customerDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(customerDto.getId())))
                    .andExpect(jsonPath("$.email", is(customerDto.getEmail())))
                    .andExpect(jsonPath("$.firstName", is(customerDto.getFirstName())))
                    .andExpect(jsonPath("$.lastName", is(customerDto.getLastName())))
                    .andExpect(jsonPath("$.age", is(customerDto.getAge())));
        }

        @Test
        void shouldReturn404_WhenCustomerToUpdateDoesNotExist() throws Exception {
            when(customerService.update(NON_EXISTING_CUSTOMER_ID, customerDto))
                    .thenThrow(new ResourceNotFoundException(CUSTOMER_NOT_FOUND_MSG));

            mockMvc.perform(put("/v1/customers/" + NON_EXISTING_CUSTOMER_ID)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(customerDto)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", is(CUSTOMER_NOT_FOUND_MSG)))
                    .andExpect(jsonPath("$.statusCode", is(HttpStatus.NOT_FOUND.value())));
        }
    }
}