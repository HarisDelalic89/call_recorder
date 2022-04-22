package com.qtelecom.call_recorder.web.controllers.v1;

import com.qtelecom.call_recorder.services.CustomerService;
import com.qtelecom.call_recorder.services.ICustomerService;
import com.qtelecom.call_recorder.web.dtos.CustomerDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/customers")
public class CustomerController {

    private final ICustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Get all customers")
    @ApiResponses(value = {@ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "200", description = "Ok")})
    @GetMapping
    public ResponseEntity<List<CustomerDto>> getCustomers() {
        return ResponseEntity.ok(customerService.getCustomers());
    }

    @Operation(summary = "Get customer by id")
    @ApiResponses(value = {@ApiResponse(responseCode = "404", description = "Resource Not Found"),
            @ApiResponse(responseCode = "200", description = "Ok")})
    @GetMapping(value = "/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @PostMapping
    @Operation(summary = "Create Customer")
    @ApiResponses(value = {@ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "201", description = "Created")})
    public ResponseEntity<CustomerDto> create(@RequestBody @Valid CustomerDto customerDto) {
        CustomerDto createdCustomerDto = customerService.create(customerDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdCustomerDto.getId()).toUri();
        return ResponseEntity.created(location).body(createdCustomerDto);
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Update Customer")
    @ApiResponses(value = {@ApiResponse(responseCode = "404", description = "Resource for update Not Found"),
            @ApiResponse(responseCode = "200", description = "Ok")})
    public ResponseEntity<CustomerDto> update(@PathVariable("id") Integer id, @RequestBody @Valid CustomerDto customerDto) {
        return ResponseEntity.ok().body(customerService.update(id, customerDto));
    }
}
