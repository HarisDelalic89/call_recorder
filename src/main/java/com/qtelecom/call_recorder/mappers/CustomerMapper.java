package com.qtelecom.call_recorder.mappers;

import com.qtelecom.call_recorder.domain.Customer;
import com.qtelecom.call_recorder.web.dtos.CustomerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CustomerMapper {
    //    No need to map version field, it is for internal use cases only
    @Mapping(target = "version", ignore = true)
    Customer customerDtoToCustomer(CustomerDto customerDto);

    @Mapping(target = "version", ignore = true)
    CustomerDto customerToCustomerDto(Customer customer);
}
