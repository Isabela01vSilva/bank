package com.Isabela01vSilva.bank_isabela.controller;

import com.Isabela01vSilva.bank_isabela.controller.request.customer.CustomerAccountRequest;
import com.Isabela01vSilva.bank_isabela.controller.request.customer.UpdateCustomerRequest;
import com.Isabela01vSilva.bank_isabela.controller.response.customer.CustomerAccountsResponse;
import com.Isabela01vSilva.bank_isabela.controller.response.customer.CustomerResponse;
import com.Isabela01vSilva.bank_isabela.domain.customer.Customer;
import com.Isabela01vSilva.bank_isabela.mapper.CustomerMappers;
import com.Isabela01vSilva.bank_isabela.service.customer.CustomerService;
import com.Isabela01vSilva.bank_isabela.service.dto.AccountCustomerDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("clientes")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerAccountsResponse> createCustomer(@RequestBody @Valid CustomerAccountRequest data) {
        AccountCustomerDTO createCustomer = customerService.register(data);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CustomerMappers.fromAccountCustomerDTOToResponse(createCustomer));
    }

    @GetMapping("/{id}")
    public CustomerResponse getCustomerById(@PathVariable Long id) {
        Customer customer = customerService.findCustomerById(id);
        return CustomerMappers.fromCustomerToResponse(customer);
    }

    @PutMapping("/{id}")
    public CustomerResponse updateCustomer(@PathVariable Long id,
                                                           @RequestBody @Valid UpdateCustomerRequest request) {
        Customer updatedCustomer = customerService.updateCustomer(id, request);
        return CustomerMappers.fromCustomerToResponse(updatedCustomer);
    }
}