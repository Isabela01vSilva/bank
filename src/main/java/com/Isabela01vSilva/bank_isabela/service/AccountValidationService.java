package com.Isabela01vSilva.bank_isabela.service;

import com.Isabela01vSilva.bank_isabela.domain.account.Account;
import com.Isabela01vSilva.bank_isabela.domain.account.AccountStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

public class AccountValidationService {

    public void validateActiveAccount(Account account) {
        if (account.getAccountStatus() != AccountStatus.ATIVO) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    String.format("Conta: %s não está ativa", account.getAccountNumber())
            );
        }
    }

    public void validateSufficientBalance(Account sourceAccount,
                                           BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "O valor deve ser maior que Zero");
        }

        if (amount.compareTo(sourceAccount.getBalance()) > 0) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Saldo insuficiente");
        }
    }

    public void validateAccounts(Account sourceAccount,
                                  Account destinationAccount) {
        if (sourceAccount.getId().equals(destinationAccount.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, "Não é permitido transferir para a mesma conta");
        }
    }
}
