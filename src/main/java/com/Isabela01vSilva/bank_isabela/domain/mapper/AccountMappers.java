package com.Isabela01vSilva.bank_isabela.domain.mapper;

import com.Isabela01vSilva.bank_isabela.controller.request.account.CreateAccountDTO;
import com.Isabela01vSilva.bank_isabela.domain.account.Account;
import com.Isabela01vSilva.bank_isabela.domain.account.AccountStatus;

import java.time.LocalDate;

public class AccountMappers {
    public static Account fromRequestToAccount(CreateAccountDTO dados, String accountNumber) {

        Account account = new Account();

        account.setAccountStatus(AccountStatus.ATIVO);
        account.setCreationDate(LocalDate.now());
        account.setAccountType(dados.accountType());
        account.setCustomer(dados.customer());
        account.setAgencyNumber("0001");
        account.setBalance(0.00);
        account.setAccountNumber(accountNumber);

        return account;
    }
}


