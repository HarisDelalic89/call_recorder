package com.qtelecom.call_recorder.services;

import com.qtelecom.call_recorder.web.dtos.CustomerDto;

import java.util.List;

public interface ICustomerService {
    List<CustomerDto> getCustomers();

    CustomerDto getCustomerById(Integer id);

    CustomerDto create(CustomerDto customerDto);

    CustomerDto update(Integer id, CustomerDto customerDto);
}
