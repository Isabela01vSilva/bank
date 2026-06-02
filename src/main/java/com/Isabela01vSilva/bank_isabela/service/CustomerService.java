package com.Isabela01vSilva.bank_isabela.service;

import com.Isabela01vSilva.bank_isabela.controller.request.CustomerAccountRequest;
import com.Isabela01vSilva.bank_isabela.controller.request.customer.CustomerRequest;
import com.Isabela01vSilva.bank_isabela.controller.request.conta.CriarContaDTO;
import com.Isabela01vSilva.bank_isabela.domain.customer.Customer;
import com.Isabela01vSilva.bank_isabela.domain.customer.CustomerRepository;
import com.Isabela01vSilva.bank_isabela.domain.conta.Conta;
import com.Isabela01vSilva.bank_isabela.domain.mapper.CustomerMappers;
import com.Isabela01vSilva.bank_isabela.service.dto.ClienteContaDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ContaService contaService;

    @Transactional
    public ClienteContaDTO register(CustomerAccountRequest data) {
        validateCustomerCreation(data);

        Customer createdCustomer  =  customerRepository.save(CustomerMappers.fromRequestToCustomer(data));

        Conta contaCriada = contaService.cadastrar(new CriarContaDTO(data.accountType(), createdCustomer));

        return  new ClienteContaDTO(contaCriada, createdCustomer);
    }

    private void validateCustomerCreation(CustomerAccountRequest data) {
        if (!LocalDate.now().minus(17, ChronoUnit.YEARS).isBefore(data.birthDate())) {
            throw new IllegalArgumentException("Cliente deve ter pelo menos 18 anos");
        }
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
    }

    @Transactional
    public Customer updateCustomer(Long id, CustomerRequest dados) {
        Customer cliente = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        cliente.updateInfoCustomer(dados);
        customerRepository.save(cliente);

        return cliente;
    }
}