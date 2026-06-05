package com.Isabela01vSilva.bank_isabela.controller;

import com.Isabela01vSilva.bank_isabela.commons.Formatters;
import com.Isabela01vSilva.bank_isabela.controller.request.CustomerAccountRequest;
import com.Isabela01vSilva.bank_isabela.controller.request.customer.CustomerRequest;
import com.Isabela01vSilva.bank_isabela.controller.request.customer.UpdateCustomerRequest;
import com.Isabela01vSilva.bank_isabela.controller.response.ClienteContasResponse;
import com.Isabela01vSilva.bank_isabela.controller.response.customer.CustomerResponse;
import com.Isabela01vSilva.bank_isabela.controller.response.account.AccountResponse;
import com.Isabela01vSilva.bank_isabela.domain.customer.Customer;
import com.Isabela01vSilva.bank_isabela.service.CustomerService;
import com.Isabela01vSilva.bank_isabela.service.dto.AccountCustomerDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("clientes")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<ClienteContasResponse> createCustomer(@Valid @RequestBody CustomerAccountRequest data) {
        AccountCustomerDTO createCustomer = customerService.register(data);

        List<AccountResponse> contas = createCustomer.conta().stream().map(c -> new AccountResponse(
                c.getAccountNumber(),
                c.getAgencyNumber(),
                c.getAccountType(),
                c.getAccountStatus(),
                c.getBalance(),
                c.getCreationDate())
        ).toList();
        Customer cliente = createCustomer.customer();

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ClienteContasResponse(
                        new CustomerResponse(
                                cliente.getId(),
                                cliente.getFullName(),
                                cliente.getBirthDate(),
                                cliente.getCpf(),
                                cliente.getEmail(),
                                cliente.getPhoneNumber(),
                                cliente.getCustomerStatus()),
                        contas));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long id) {
        Customer getCustomer = customerService.getCustomerById(id);

        return ResponseEntity.ok(
                new CustomerResponse(
                        getCustomer.getId(),
                        getCustomer.getFullName(),
                        getCustomer.getBirthDate(),
                        Formatters.formatCPF(getCustomer.getCpf()),
                        Formatters.formatEmail(getCustomer.getEmail()),
                        Formatters.formatPhone(getCustomer.getPhoneNumber()),
                        getCustomer.getCustomerStatus()
                )
        );

    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable Long id, @Valid @RequestBody UpdateCustomerRequest data) {
        Customer updatedCustomer = customerService.updateCustomer(id, data);

        return ResponseEntity.ok(
                new CustomerResponse(
                        updatedCustomer.getId(),
                        updatedCustomer.getFullName(),
                        updatedCustomer.getBirthDate(),
                        Formatters.formatCPF(updatedCustomer.getCpf()),
                        Formatters.formatEmail(updatedCustomer.getEmail()),
                        Formatters.formatPhone(updatedCustomer.getPhoneNumber()),
                        updatedCustomer.getCustomerStatus()));
    }
}