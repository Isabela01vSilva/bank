package com.Isabela01vSilva.bank_isabela.controller.request.historico;

import com.Isabela01vSilva.bank_isabela.domain.customer.Customer;
import com.Isabela01vSilva.bank_isabela.domain.account.Account;
import com.Isabela01vSilva.bank_isabela.domain.historico.HistoryType;

import java.math.BigDecimal;

public record RegisterHistoryRequest(
        Account account,
        Customer customer,
        HistoryType historyType,
        String description,
        BigDecimal amount
) {
}
