package com.qtelecom.call_recorder.services;

import com.qtelecom.call_recorder.domain.Customer;
import com.qtelecom.call_recorder.exceptions.ResourceNotFoundException;
import com.qtelecom.call_recorder.mappers.CustomerMapper;
import com.qtelecom.call_recorder.repositories.CustomerRepository;
import com.qtelecom.call_recorder.web.dtos.CustomerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;

    Customer customer1;
    Customer customer2;
    CustomerDto customer1Dto;

    @BeforeEach
    void setUp() {
        customer1 = Customer.builder()
                .id(1).email("customer1@test.com")
                .firstName("firstName")
                .lastName("lastName")
                .age(10)
                .build();
        customer1Dto = CustomerDto.builder().id(1).email("customer1@test.com")
                .firstName("firstName")
                .lastName("lastName")
                .age(10)
                .build();
        customer2 = Customer.builder().id(1).email("customer2@test.com").build();
    }

    @Test
    public void shouldRetrieveAllCustomersAndMapThemToDto() {
        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer1, customer2));

        List<CustomerDto> customers = customerService.getCustomers();

        verify(customerRepository).findAll();
        verify(customerMapper, times(2)).customerToCustomerDto(any(Customer.class));

        assertEquals(2, customers.size());
    }

    @Test
    public void shouldRetrieveCustomerByIdIfCustomerExists() {
        when(customerRepository.findById(customer1.getId())).thenReturn(Optional.of(customer1));
        when(customerMapper.customerToCustomerDto(any(Customer.class))).thenReturn(customer1Dto);

        CustomerDto customer = customerService.getCustomerById(customer1.getId());

        verify(customerRepository).findById(customer1.getId());
        verify(customerMapper).customerToCustomerDto(any(Customer.class));

        assertEquals(1, customer.getId());
        assertEquals("customer1@test.com", customer.getEmail());
    }

    @Test
    public void shouldThrowExceptionIfCustomerToRetrieveDoesNotExist() {
        when(customerRepository.findById(customer1.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            customerService.getCustomerById(customer1.getId());
        });

        verify(customerRepository).findById(customer1.getId());
        verify(customerMapper, never()).customerToCustomerDto(any(Customer.class));
    }

    @Test
    public void shouldCreateCustomer() {
        when(customerMapper.customerDtoToCustomer(customer1Dto)).thenReturn(customer1);
        when(customerRepository.save(customer1)).thenReturn(customer1);

        customerService.create(customer1Dto);

        verify(customerRepository).save(customer1);
        verify(customerMapper).customerDtoToCustomer(customer1Dto);
    }

    @Test
    public void shouldUpdateCustomerIfCustomerExists() {
        CustomerDto customer1DtoUpdated = CustomerDto.builder()
                .id(1)
                .email("customer1@test.com")
                .firstName("firstNameUpdated")
                .lastName("lastNameUpdated")
                .age(20)
                .build();
        Customer customer1Updated = Customer.builder()
                .id(1)
                .email("customer1@test.com")
                .firstName("firstNameUpdated")
                .lastName("lastNameUpdated")
                .searchName("firstNameUpdatedlastNameUpdated")
                .age(20)
                .build();

        when(customerRepository.findById(customer1.getId())).thenReturn(Optional.of(customer1));
        when(customerRepository.save(argThat(new CustomerMatcher(customer1Updated)))).thenReturn(customer1Updated);
        when(customerMapper.customerToCustomerDto(customer1Updated)).thenReturn(customer1DtoUpdated);

        CustomerDto customer = customerService.update(customer1.getId(), customer1DtoUpdated);

        verify(customerRepository).findById(customer1.getId());
        verify(customerRepository).save(argThat(new CustomerMatcher(customer1Updated)));
        verify(customerMapper).customerToCustomerDto(customer1Updated);

        assertAll("Property assertions",
                () -> assertEquals(1, customer.getId()),
                () -> assertEquals("customer1@test.com", customer.getEmail()),
                () ->  assertEquals("firstNameUpdated", customer.getFirstName()),
                () -> assertEquals("lastNameUpdated", customer.getLastName()),
                () -> assertEquals(20, customer.getAge()));
    }

    static class CustomerMatcher implements ArgumentMatcher<Customer> {
        private Customer customer;

        public CustomerMatcher(Customer customer) {
            this.customer = customer;
        }

        @Override
        public boolean matches(Customer updatedCustomer) {
            return updatedCustomer.getFirstName().equals(customer.getFirstName()) &&
                    updatedCustomer.getLastName().equals(customer.getLastName()) &&
                    updatedCustomer.getAge().equals(customer.getAge());
        }
    }

    @Test
    public void shouldThrowExceptionIfCustomerToUpdateDoesNotExist() {
        when(customerRepository.findById(customer1.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            customerService.update(customer1.getId(), any(CustomerDto.class));
        });

        verify(customerRepository).findById(customer1.getId());
        verify(customerMapper, never()).customerToCustomerDto(any(Customer.class));
    }
}