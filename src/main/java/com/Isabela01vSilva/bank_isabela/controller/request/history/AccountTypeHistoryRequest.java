package com.Isabela01vSilva.bank_isabela.controller.request.history;

import com.Isabela01vSilva.bank_isabela.domain.account.AccountType;

public record AccountTypeHistoryRequest(
        Long id,
        AccountType accountType
) {
}
