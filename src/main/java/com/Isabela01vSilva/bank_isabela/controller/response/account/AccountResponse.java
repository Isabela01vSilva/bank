package com.Isabela01vSilva.bank_isabela.controller.response.account;

import com.Isabela01vSilva.bank_isabela.domain.account.AccountStatus;
import com.Isabela01vSilva.bank_isabela.domain.account.AccountType;

import java.time.LocalDate;

public record AccountResponse(
        String numero,
        String numeroAgencia,
        AccountType tipoConta,
        AccountStatus statusConta,
        Double saldo,
        LocalDate dataCriacao
) {
}
