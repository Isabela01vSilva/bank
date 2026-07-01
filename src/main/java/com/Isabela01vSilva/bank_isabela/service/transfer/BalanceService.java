package com.Isabela01vSilva.bank_isabela.service.transfer;

import com.Isabela01vSilva.bank_isabela.domain.account.Account;

import java.math.BigDecimal;

public class BalanceService {

    public void transfer(Account sourceAccount, Account destinationAccount, BigDecimal amount) {
        sourceAccount.withdraw(amount);
        destinationAccount.deposit(amount);
    }
}
