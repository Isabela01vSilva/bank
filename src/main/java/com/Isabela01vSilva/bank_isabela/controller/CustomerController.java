package com.Isabela01vSilva.bank_isabela.controller;

import com.Isabela01vSilva.bank_isabela.commons.Formatters;
import com.Isabela01vSilva.bank_isabela.controller.request.CustomerAccountRequest;
import com.Isabela01vSilva.bank_isabela.controller.request.customer.CustomerRequest;
import com.Isabela01vSilva.bank_isabela.controller.response.ClienteContaResponse;
import com.Isabela01vSilva.bank_isabela.controller.response.customer.CustomerResponse;
import com.Isabela01vSilva.bank_isabela.controller.response.conta.ContaResponse;
import com.Isabela01vSilva.bank_isabela.domain.customer.Customer;
import com.Isabela01vSilva.bank_isabela.domain.conta.Conta;
import com.Isabela01vSilva.bank_isabela.service.CustomerService;
import com.Isabela01vSilva.bank_isabela.service.dto.ClienteContaDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("clientes")
public class CustomerController {

    @Autowired
    private CustomerService clienteService;

    @PostMapping
    public ResponseEntity<ClienteContaResponse> cadastrarCliente(@Valid @RequestBody CustomerAccountRequest dados) {
        ClienteContaDTO novoClienteConta = clienteService.register(dados);

        Conta conta = novoClienteConta.conta();
        Customer cliente = novoClienteConta.customer();

        return ResponseEntity.status(HttpStatus.CREATED).body(new ClienteContaResponse(new CustomerResponse(cliente.getId(), cliente.getFullName(), cliente.getBirthDate(), cliente.getCpf(), cliente.getEmail(), cliente.getPhoneNumber()), new ContaResponse(conta.getNumero(), conta.getNumeroAgencia(), conta.getTipoConta(), conta.getStatusConta(), conta.getSaldo(), conta.getDataCriacao())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> buscarClientePorId(@PathVariable Long id) {
        Customer buscarCliente = clienteService.getCustomerById(id);
        return ResponseEntity.ok(new CustomerResponse(buscarCliente.getId(), buscarCliente.getFullName(), buscarCliente.getBirthDate(), Formatters.formatCPF(buscarCliente.getCpf()), buscarCliente.getEmail(), Formatters.formatPhone(buscarCliente.getPhoneNumber())));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> atualizarCliente(@Valid @PathVariable Long id, @RequestBody CustomerRequest dados) {
        Customer atualizar = clienteService.updateCustomer(id, dados);
        return ResponseEntity.ok(new CustomerResponse(atualizar.getId(), atualizar.getFullName(), atualizar.getBirthDate(), Formatters.formatCPF(atualizar.getCpf()), atualizar.getEmail(), Formatters.formatPhone(atualizar.getPhoneNumber())));
    }

}