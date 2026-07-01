package com.Isabela01vSilva.bank_isabela.service.account;

import com.Isabela01vSilva.bank_isabela.commons.Formatters;
import com.Isabela01vSilva.bank_isabela.controller.request.account.CreateAccountDTO;
import com.Isabela01vSilva.bank_isabela.controller.request.account.UpdateAccountStatusRequest;
import com.Isabela01vSilva.bank_isabela.controller.response.account.UpdateAccountStatusResponse;
import com.Isabela01vSilva.bank_isabela.domain.account.Account;
import com.Isabela01vSilva.bank_isabela.domain.account.AccountRepository;
import com.Isabela01vSilva.bank_isabela.domain.account.AccountStatus;
import com.Isabela01vSilva.bank_isabela.domain.account.AccountType;
import com.Isabela01vSilva.bank_isabela.domain.customer.Customer;
import com.Isabela01vSilva.bank_isabela.domain.customer.CustomerRepository;
import com.Isabela01vSilva.bank_isabela.mapper.AccountMappers;
import com.Isabela01vSilva.bank_isabela.service.history.HistoryService;
import com.Isabela01vSilva.bank_isabela.service.customer.CustomerStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountLifecycleService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final AccountValidationService validationService;
    private final AccountNumberGenerator accountNumberGenerator;
    private final CustomerStatusService customerStatusService;
    private final HistoryService historyService;

    @Transactional
    public Account createAccountForCpf(String cpf, AccountType requestedType) {
        validationService.validateCpf(cpf);

        String normalizedCpf = Formatters.normalize(cpf);
        Customer customer = findCustomerByCpf(normalizedCpf);

        List<Account> accountsOfType = accountRepository
                .findByCustomerCpfAndAccountType(normalizedCpf, requestedType);

        validationService.validateNoActiveAccount(accountsOfType, requestedType);

        return accountsOfType.stream()
                .filter(Account::isClosed)
                .findFirst()
                .map(closed -> reactivateAccount(closed, customer))
                .orElseGet(() -> createNewAccount(customer, requestedType));
    }

    @Transactional
    public List<Account> createMultipleAccounts(List<CreateAccountDTO> dtos) {
        List<Account> accounts = dtos.stream()
                .map(dto -> Account.create(
                        accountNumberGenerator.generateAccountNumber(),
                        "0001",
                        dto.accountType(),
                        dto.customer()))
                .toList();

        List<Account> saved = accountRepository.saveAll(accounts);
        saved.forEach(historyService::registerAccountCreated);
        return saved;
    }

    @Transactional
    public UpdateAccountStatusResponse updateAccountStatus(UpdateAccountStatusRequest request) {
        Account account = findByAccountNumberAndAgencyNumber(request.accountNumber(), request.agencyNumber());

        validationService.validateStatusChange(account, request);

        if (request.accountStatus() == AccountStatus.ENCERRADO) {
            account.close(request.statusChangeReason());
            historyService.registerAccountClosed(account);
        } else {
            account.reactivate();
            historyService.registerAccountReactivated(account);
        }

        Account saved = accountRepository.save(account);

        boolean hasActiveAccount = accountRepository
                .existsByCustomerAndAccountStatus(saved.getCustomer(), AccountStatus.ATIVO);
        customerStatusService.toUpdateAccountStatusResponse(saved.getCustomer(), hasActiveAccount);

        return AccountMappers.toUpdateStatusResponse(saved);
    }

    private Account createNewAccount(Customer customer, AccountType accountType) {
        Account account = Account.create(
                accountNumberGenerator.generateAccountNumber(),
                "0001",
                accountType,
                customer);

        Account saved = accountRepository.save(account);
        customer.activate();
        customerRepository.save(customer);
        historyService.registerAccountCreated(saved);
        return saved;
    }

    private Account reactivateAccount(Account account, Customer customer) {
        account.reactivate();
        Account saved = accountRepository.save(account);

        if (!customer.isActive()) {
            customer.activate();
            customerRepository.save(customer);
            historyService.registerCustomerReactivated(saved, customer);
        }

        historyService.registerAccountReactivated(saved);
        return saved;
    }

    public Account findByAccountNumberAndAgencyNumber(String accountNumber, String agencyNumber) {
        return accountRepository.findByAccountNumberAndAgencyNumber(accountNumber, agencyNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));
    }

    private Customer findCustomerByCpf(String cpf) {
        return customerRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }
}
