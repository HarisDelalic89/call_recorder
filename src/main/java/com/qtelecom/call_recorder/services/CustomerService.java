package com.qtelecom.call_recorder.services;

import com.qtelecom.call_recorder.domain.Customer;
import com.qtelecom.call_recorder.exceptions.ResourceNotFoundException;
import com.qtelecom.call_recorder.mappers.CustomerMapper;
import com.qtelecom.call_recorder.repositories.CustomerRepository;
import com.qtelecom.call_recorder.web.dtos.CustomerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService implements ICustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public List<CustomerDto> getCustomers() {
        return customerRepository.findAll().stream().map(customerMapper::customerToCustomerDto).collect(Collectors.toList());
    }

    @Override
    public CustomerDto getCustomerById(Integer id) {
        return customerMapper.customerToCustomerDto(customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with ID: " + id + " Not Found")));
    }

    @Override
    public CustomerDto create(CustomerDto customerDto) {
        Customer savedCustomer = customerRepository.save(customerMapper.customerDtoToCustomer(customerDto));
        savedCustomer.calculateSearchName();
        return customerMapper
                .customerToCustomerDto(savedCustomer);
    }

    @Override
    public CustomerDto update(Integer id, CustomerDto customerDto) {
        return customerMapper.customerToCustomerDto(customerRepository.findById(id).map(foundCustomer -> {
            foundCustomer.setFirstName(customerDto.getFirstName());
            foundCustomer.setLastName(customerDto.getLastName());
            foundCustomer.setAge(customerDto.getAge());
            foundCustomer.calculateSearchName();
            return customerRepository.save(foundCustomer);
        }).orElseThrow(() -> new ResourceNotFoundException("Customer with ID: " + id + " Not Found")));
    }
}
