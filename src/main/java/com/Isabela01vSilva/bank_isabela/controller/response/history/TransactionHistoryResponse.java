package com.Isabela01vSilva.bank_isabela.controller.response.history;

import com.Isabela01vSilva.bank_isabela.domain.account.Account;

public record TransactionHistoryResponse(
        String cpf,
        Account account
) {
}
