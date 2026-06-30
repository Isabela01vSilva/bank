package com.Isabela01vSilva.bank_isabela.service.account;

import com.Isabela01vSilva.bank_isabela.controller.request.account.UpdateAccountStatusRequest;
import com.Isabela01vSilva.bank_isabela.domain.account.Account;
import com.Isabela01vSilva.bank_isabela.domain.account.AccountStatus;
import com.Isabela01vSilva.bank_isabela.domain.account.AccountType;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

public class AccountValidationService {

    public void validateActiveAccount(Account account) {
        if (account.isClosed()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    String.format("Conta: %s está encerrada", account.getAccountNumber()));
        }
    }

    public void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "O valor deve ser maior que zero");
        }
    }

    public void validateSufficientBalance(Account account,
                                          BigDecimal amount) {
        if (account.hasInsufficientBalance(amount)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Saldo insuficiente");
        }
    }

    public void validateDifferentAccounts(Account sourceAccount,
                                          Account destinationAccount) {
        if (sourceAccount.getId().equals(destinationAccount.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, "Não é permitido transferir para a mesma conta");
        }
    }

    public void validateNoActiveAccount(List<Account> accounts, AccountType accountType) {
        boolean hasActive = accounts.stream().anyMatch(Account::isActive);
        if (hasActive) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Cliente já possui uma conta ativa do tipo: " + accountType);
        }
    }

    public void validateStatusChange(Account account, UpdateAccountStatusRequest request) {
        if (account.getAccountStatus() == request.accountStatus()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "A conta já possui este status");
        }
        if (request.accountStatus() == AccountStatus.ENCERRADO && account.hasBalance()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Não é possível encerrar conta com saldo");
        }
    }

    public void validateCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException("CPF não pode ser vazio");
        }
    }

    public void validateAccountAndAgency(String accountNumber, String agencyNumber) {
        if (accountNumber == null || agencyNumber == null) {
            throw new IllegalArgumentException("Número da conta e agência não podem ser nulos");
        }
        if (accountNumber.isEmpty() || agencyNumber.isEmpty()) {
            throw new EntityNotFoundException("Número da conta e agência são obrigatórios");
        }
        boolean validAccount = accountNumber.matches("\\d{5}-\\d");
        boolean validAgency  = agencyNumber.matches("\\d{4}") || agencyNumber.matches("\\d{4}-\\d");
        if (!validAccount || !validAgency) {
            throw new IllegalArgumentException(
                    "Conta no formato 12345-6 e agência no formato 1234 ou 1234-1");
        }
    }
}
