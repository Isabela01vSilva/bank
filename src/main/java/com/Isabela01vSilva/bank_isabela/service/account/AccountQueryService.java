package com.Isabela01vSilva.bank_isabela.service.account;

import com.Isabela01vSilva.bank_isabela.commons.Formatters;
import com.Isabela01vSilva.bank_isabela.controller.response.account.AccountWithCustomerResponse;
import com.Isabela01vSilva.bank_isabela.domain.account.Account;
import com.Isabela01vSilva.bank_isabela.domain.account.AccountRepository;
import com.Isabela01vSilva.bank_isabela.mapper.AccountMappers;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountQueryService {

    private final AccountRepository accountRepository;
    private final AccountValidationService accountValidationService;

    public List<AccountWithCustomerResponse> searchByCpf(String cpf) {
        accountValidationService.validateCpf(cpf);
        String normalizedCpf = Formatters.normalize(cpf);

        List<Account> accounts = accountRepository.findByCustomerCpf(normalizedCpf);
        if (accounts.isEmpty()) {
            throw new EntityNotFoundException("Nenhuma conta encontrada para o CPF: " + normalizedCpf);
        }

        return accounts.stream()
                .map(AccountMappers::toAccountWithCustomerResponse)
                .toList();
    }

    public AccountWithCustomerResponse searchByAccountNumberAndAgency(String accountNumber,
                                                                      String agencyNumber) {
        accountValidationService.validateAccountAndAgency(accountNumber, agencyNumber);
        Account account = findAccount(accountNumber, agencyNumber);
        return AccountMappers.toAccountWithCustomerResponse(account);
    }

    public String getBalance(String accountNumber, String agencyNumber) {
        Account account = findAccount(accountNumber, agencyNumber);
        return String.format("Saldo R$%s. Agência: %s Conta: %s",
                account.getBalance(),
                account.getAgencyNumber(),
                account.getAccountNumber());
    }

    private Account findAccount(String accountNumber, String agencyNumber) {
        return accountRepository.findByAccountNumberAndAgencyNumber(accountNumber, agencyNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));
    }

}
