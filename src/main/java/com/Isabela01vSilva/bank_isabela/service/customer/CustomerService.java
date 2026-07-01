package com.Isabela01vSilva.bank_isabela.service.customer;

import com.Isabela01vSilva.bank_isabela.controller.request.customer.CustomerAccountRequest;
import com.Isabela01vSilva.bank_isabela.controller.request.account.CreateAccountDTO;
import com.Isabela01vSilva.bank_isabela.controller.request.customer.UpdateCustomerRequest;
import com.Isabela01vSilva.bank_isabela.domain.customer.Customer;
import com.Isabela01vSilva.bank_isabela.domain.customer.CustomerRepository;
import com.Isabela01vSilva.bank_isabela.domain.account.Account;
import com.Isabela01vSilva.bank_isabela.mapper.CustomerMappers;
import com.Isabela01vSilva.bank_isabela.service.history.HistoryService;
import com.Isabela01vSilva.bank_isabela.service.account.AccountService;
import com.Isabela01vSilva.bank_isabela.service.dto.AccountCustomerDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AccountService accountService;
    private final HistoryService historyService;
    private final CustomerValidationService validationService;

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
        validationService.validateCustomerCreation(data);

        // Converte request para entidade e salva
        Customer createdCustomer = customerRepository.save(CustomerMappers.fromRequestToCustomer(data));

        // Prepara dados para criação de contas
        List<CreateAccountDTO> accountDtos = data.accountTypes().stream()
                .map(type -> new CreateAccountDTO(type, createdCustomer))
                .toList();

        // Cria múltiplas contas e retorna junto com o cliente
        List<Account> accounts = accountService.createMultipleAccounts(accountDtos);

        return new AccountCustomerDTO(accounts, createdCustomer);
    }

    /**
     * Atualiza os dados permitidos do cliente.
     * Apenas nome, e-mail e telefone podem ser alterados.
     */
    @Transactional
    public Customer updateCustomer(Long id, UpdateCustomerRequest data) {
        Customer customer = findCustomerById(id);

        // Captura estado anterior para o histórico
        CustomerSnapshot before = CustomerSnapshot.of(customer);

        //Atualiza os dados
        customer.updateInfo(data);

        //Monta descrição do historico
        String description = buildUpdateDescription(before, customer);
        if (!description.isBlank()) {
            historyService.registerCustomerUpdate(customer, description);
        }

        return customerRepository.save(customer);
    }

    public Customer findCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
    }

    private String buildUpdateDescription(CustomerSnapshot before, Customer after) {
        StringBuilder updateDescription = new StringBuilder();

        if (!before.fullName().equals(after.getFullName()))
            updateDescription.append("Nome: ").append(before.fullName()).append(" -> ").append(after.getFullName()).append(". ");

        if (!before.email().equals(after.getEmail()))
            updateDescription.append("Email: ").append(before.email()).append(" -> ").append(after.getEmail()).append(". ");

        if (!before.phoneNumber().equals(after.getPhoneNumber()))
            updateDescription.append("Telefone: ").append(before.phoneNumber()).append(" -> ").append(after.getPhoneNumber()).append(". ");

        return updateDescription.toString().trim();
    }

    private record CustomerSnapshot(String fullName, String email, String phoneNumber) {
        static CustomerSnapshot of(Customer customer) {
            return new CustomerSnapshot(customer.getFullName(), customer.getEmail(), customer.getPhoneNumber());
        }
    }
}