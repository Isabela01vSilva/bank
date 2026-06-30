package com.Isabela01vSilva.bank_isabela.service.account;

import com.Isabela01vSilva.bank_isabela.controller.request.account.*;
import com.Isabela01vSilva.bank_isabela.controller.response.account.AccountWithCustomerResponse;
import com.Isabela01vSilva.bank_isabela.controller.response.account.UpdateAccountStatusResponse;
import com.Isabela01vSilva.bank_isabela.domain.account.Account;
import com.Isabela01vSilva.bank_isabela.domain.account.AccountType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountTransactionService transactionService;
    private final AccountLifecycleService lifecycleService;
    private final AccountQueryService queryService;

    public String withdrawal(AccountTransactionRequest request) {
        return transactionService.withdrawal(request);
    }

    public String deposit(AccountTransactionRequest request) {
        return transactionService.deposit(request);
    }

    public List<Account> createMultipleAccounts(List<CreateAccountDTO> dtos) {
        return lifecycleService.createMultipleAccounts(dtos);
    }

    public Account createAccountForCpf(String cpf, AccountType type) {
        return lifecycleService.createAccountForCpf(cpf, type);
    }

    public UpdateAccountStatusResponse updateAccountStatus(UpdateAccountStatusRequest request) {
        return lifecycleService.updateAccountStatus(request);
    }

    public List<AccountWithCustomerResponse> searchByCpf(String cpf) {
        return queryService.searchByCpf(cpf);
    }

    public AccountWithCustomerResponse searchByAccountNumberAndAgency(String accountNumber, String agencyNumber) {
        return queryService.searchByAccountNumberAndAgency(accountNumber, agencyNumber);
    }

    public String getBalance(String accountNumber, String agencyNumber) {
        return queryService.getBalance(accountNumber, agencyNumber);
    }
}