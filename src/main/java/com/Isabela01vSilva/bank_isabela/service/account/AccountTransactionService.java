package com.Isabela01vSilva.bank_isabela.service.account;

import com.Isabela01vSilva.bank_isabela.controller.request.account.AccountTransactionRequest;
import com.Isabela01vSilva.bank_isabela.domain.account.Account;
import com.Isabela01vSilva.bank_isabela.domain.account.AccountRepository;
import com.Isabela01vSilva.bank_isabela.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AccountTransactionService {

    private final AccountRepository accountRepository;
    private final AccountValidationService accountValidationService;
    private final HistoryService historyService;

    @Transactional
    public String withdrawal(AccountTransactionRequest request){
        Account account = findAccount(request.accountNumber(), request.agencyNumber());
        try{
            accountValidationService.validateActiveAccount(account);
            accountValidationService.validateAmount(request.amount());
            accountValidationService.validateSufficientBalance(account, request.amount());

            account.withdraw(request.amount());
            accountRepository.save(account);

            historyService.registerWithdrawal(account, request.amount());
        } catch (Exception e){
            historyService.registerWithdrawalFailed(account, request.amount());
            throw  e;
        }
        return "Valor sacado: R$" + request.amount();
    }

    @Transactional
    public String deposit(AccountTransactionRequest request) {
        Account account = findAccount(request.accountNumber(), request.agencyNumber());
        try {
            accountValidationService.validateActiveAccount(account);
            accountValidationService.validateAmount(request.amount());

            account.deposit(request.amount());
            accountRepository.save(account);

            historyService.registerDeposit(account, request.amount());
        } catch (Exception e) {
            historyService.registerDepositFailed(account, request.amount(), e.getMessage());
            throw e;
        }
        return "Valor depositado: R$" + request.amount();
    }

    private Account findAccount(String accountNumber, String agencyNumber) {
        return accountRepository.findByAccountNumberAndAgencyNumber(accountNumber, agencyNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));
    }
}
