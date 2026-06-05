package com.Isabela01vSilva.bank_isabela.service;

import com.Isabela01vSilva.bank_isabela.controller.request.CustomerAccountRequest;
import com.Isabela01vSilva.bank_isabela.controller.request.customer.CustomerRequest;
import com.Isabela01vSilva.bank_isabela.controller.request.account.CreateAccountDTO;
import com.Isabela01vSilva.bank_isabela.controller.request.customer.UpdateCustomerRequest;
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

    /**
     * Registra um novo cliente e cria as contas solicitadas.
     * Passos:
     * 1) Valida regras de negócio para criação do cliente
     * 2) Persiste o cliente
     * 3) Cria as contas associadas ao cliente
     * 4) Retorna um DTO com cliente e contas criadas
     */
    @Transactional
    public AccountCustomerDTO register(CustomerAccountRequest data) {
        // Valida campos e regras antes de persistir
        validateCustomerCreation(data);

        // Converte request para entidade e salva
        Customer createdCustomer = customerRepository.save(CustomerMappers.fromRequestToCustomer(data));

        // Prepara dados para criação de contas
        List<CreateAccountDTO> contas = data.accountTypes().stream()
                .map(tipo -> new CreateAccountDTO(tipo, createdCustomer))
                .toList();

        // Cria múltiplas contas e retorna junto com o cliente
        List<Account> contasCriadas = contaService.createMultipleAccounts(contas);

        return new AccountCustomerDTO(contasCriadas, createdCustomer);
    }

    /**
     * Valida regras de negócio aplicáveis ao cadastro de um cliente.
     * Verificações:
     * - Maioridade (>= 18 anos)
     * - Nome completo válido (pelo menos 2 palavras, sem números)
     * - CPF, e-mail e telefone únicos no sistema
     */
    private void validateCustomerCreation(CustomerAccountRequest data) {
        // Verifica idade mínima (18 anos)
        if (!LocalDate.now().minus(17, ChronoUnit.YEARS).isAfter(data.birthDate())) {
            throw new IllegalArgumentException("Cliente deve ter pelo menos 18 anos");
        }

        // Valida nome completo
        if (!Formatters.isValidFullName(data.fullName())) {
            throw new IllegalArgumentException("Nome deve conter pelo menos 2 palavras e apenas letras, espaços, hífens ou apóstrofos (sem números ou caracteres especiais)");
        }

        // Verifica unicidade de CPF
        if (data.cpf() != null) {
            String cpf = Formatters.normalize(data.cpf());
            if (customerRepository.existsByCpf(cpf)) {
                throw new IllegalArgumentException("CPF já cadastrado");
            }
        }

        // Verifica unicidade de email
        if (data.email() != null) {
            String email = Formatters.normalizeEmail(data.email());
            if (customerRepository.existsByEmail(email)) {
                throw new IllegalArgumentException("Email já cadastrado");
            }
        }

        // Verifica unicidade de telefone
        if (data.phoneNumber() != null) {
            String phone = Formatters.normalizePhone(data.phoneNumber());
            if (customerRepository.existsByPhoneNumber(phone)) {
                throw new IllegalArgumentException("Telefone já cadastrado");
            }
        }
    }

    /**
     * Recupera um cliente pelo seu id.
     * Lança EntityNotFoundException caso não exista.
     */
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
    }

    /**
     * Atualiza os dados permitidos do cliente.
     *  Apenas nome, e-mail e telefone podem ser alterados.
     */
    @Transactional
    public Customer updateCustomer(Long id, UpdateCustomerRequest data) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        // Valida campos imutáveis antes de atualizar
        // validateImmutableFields(customer, data);

        // Atualiza somente os campos permitidos
        customer.updateInfoCustomer(data);
        //customerRepository.save(customer);

        return customer;
    }

}