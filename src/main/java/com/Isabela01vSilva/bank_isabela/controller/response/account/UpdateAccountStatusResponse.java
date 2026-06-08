package com.Isabela01vSilva.bank_isabela.controller.response.account;

import com.Isabela01vSilva.bank_isabela.domain.account.AccountStatus;
import com.Isabela01vSilva.bank_isabela.domain.account.AccountType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateAccountStatusResponse(
        String accountNumber,
        String agencyNumber,
        AccountType accountType,
        AccountStatus accountStatus,
        BigDecimal balance,
        LocalDate creationDate,
        LocalDate statusChangeDate,
        String statusChangeReason
) {
}
