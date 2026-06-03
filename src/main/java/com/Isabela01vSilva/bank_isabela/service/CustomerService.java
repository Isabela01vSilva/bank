package com.Isabela01vSilva.bank_isabela.service;

import com.Isabela01vSilva.bank_isabela.controller.request.CustomerAccountRequest;
import com.Isabela01vSilva.bank_isabela.controller.request.customer.CustomerRequest;
import com.Isabela01vSilva.bank_isabela.controller.request.account.CreateAccountDTO;
import com.Isabela01vSilva.bank_isabela.domain.customer.Customer;
import com.Isabela01vSilva.bank_isabela.domain.customer.CustomerRepository;
import com.Isabela01vSilva.bank_isabela.domain.account.Account;
import com.Isabela01vSilva.bank_isabela.domain.mapper.CustomerMappers;
import com.Isabela01vSilva.bank_isabela.commons.Formatters;
import com.Isabela01vSilva.bank_isabela.service.dto.AccountCustomerDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountService contaService;

    @Transactional
    public AccountCustomerDTO register(CustomerAccountRequest data) {
        validateCustomerCreation(data);

        Customer createdCustomer  =  customerRepository.save(CustomerMappers.fromRequestToCustomer(data));

        List<CreateAccountDTO> contas = data.accountTypes().stream().map(tipo -> {
            return new CreateAccountDTO(tipo, createdCustomer);
        }).toList();

        List<Account> contasCriadas = contaService.createMultipleAccounts(contas);

        return  new AccountCustomerDTO(contasCriadas, createdCustomer);
    }

    private void validateCustomerCreation(CustomerAccountRequest data) {
        if (!LocalDate.now().minus(17, ChronoUnit.YEARS).isAfter(data.birthDate())) {
            throw new IllegalArgumentException("Cliente deve ter pelo menos 18 anos");
        }

        // Valida nome completo
        if (!Formatters.isValidFullName(data.fullName())) {
            throw new IllegalArgumentException("Nome deve conter pelo menos 2 palavras e apenas letras, espaços, hífens ou apóstrofos (sem números ou caracteres especiais)");
        }

        // Verifica unicidade de CPF, email e telefone antes de criar
        if (data.cpf() != null) {
            String cpf = Formatters.normalize(data.cpf());
            if (customerRepository.existsByCpf(cpf)) {
                throw new IllegalArgumentException("CPF já cadastrado");
            }
        }

        if (data.email() != null) {
            String email = Formatters.normalizeEmail(data.email());
            if (customerRepository.existsByEmail(email)) {
                throw new IllegalArgumentException("Email já cadastrado");
            }
        }

        if (data.phoneNumber() != null) {
            String phone = Formatters.normalizePhone(data.phoneNumber());
            if (customerRepository.existsByPhoneNumber(phone)) {
                throw new IllegalArgumentException("Telefone já cadastrado");
            }
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

        // Valida campos imutáveis antes de atualizar
        validateImmutableFields(cliente, dados);

        cliente.updateInfoCustomer(dados);
        customerRepository.save(cliente);

        return cliente;
    }

    /**
     * Valida que campos imutáveis não sejam alterados: CPF e data de nascimento.
     */
    private void validateImmutableFields(Customer cliente, CustomerRequest dados) {
        // CPF não pode ser alterado
        if (dados.cpf() != null) {
            String cpfNormalizado = Formatters.normalize(dados.cpf());
            if (!cpfNormalizado.equals(cliente.getCpf())) {
                throw new IllegalArgumentException("CPF não pode ser alterado");
            }
        }

        // Data de nascimento não pode ser alterada
        if (dados.birthDate() != null && !dados.birthDate().equals(cliente.getBirthDate())) {
            throw new IllegalArgumentException("Data de nascimento não pode ser alterada");
        }
    }
}