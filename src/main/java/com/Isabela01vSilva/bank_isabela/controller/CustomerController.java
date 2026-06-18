package com.Isabela01vSilva.bank_isabela.controller;

import com.Isabela01vSilva.bank_isabela.controller.request.CustomerAccountRequest;
import com.Isabela01vSilva.bank_isabela.controller.request.customer.UpdateCustomerRequest;
import com.Isabela01vSilva.bank_isabela.controller.response.CustomerAccountsResponse;
import com.Isabela01vSilva.bank_isabela.controller.response.customer.CustomerResponse;
import com.Isabela01vSilva.bank_isabela.domain.customer.Customer;
import com.Isabela01vSilva.bank_isabela.domain.mapper.CustomerMappers;
import com.Isabela01vSilva.bank_isabela.service.CustomerService;
import com.Isabela01vSilva.bank_isabela.service.dto.AccountCustomerDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("clientes")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerAccountsResponse> createCustomer(@Valid @RequestBody CustomerAccountRequest data) {
        AccountCustomerDTO createCustomer = customerService.register(data);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CustomerMappers.fromAccountCustomerDTOToResponse(createCustomer));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long id) {
        Customer customer = customerService.findCustomerById(id);

        return ResponseEntity.ok(
                CustomerMappers.fromCustomerToResponse(customer)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable Long id, @Valid @RequestBody UpdateCustomerRequest data) {
        Customer updatedCustomer = customerService.updateCustomer(id, data);

        return ResponseEntity.ok(
                CustomerMappers.fromCustomerToResponse(updatedCustomer)
        );
    }
}