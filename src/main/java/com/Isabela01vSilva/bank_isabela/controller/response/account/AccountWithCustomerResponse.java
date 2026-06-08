package com.Isabela01vSilva.bank_isabela.controller.response.account;

import com.Isabela01vSilva.bank_isabela.domain.account.AccountStatus;
import com.Isabela01vSilva.bank_isabela.domain.account.AccountType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AccountWithCustomerResponse(
        String nomeCompleto,
        String cpf,
        String agencia,
        String numeroConta,
        AccountType tipoConta,
        AccountStatus statusConta,
        BigDecimal saldo,
        LocalDate dataCriacao
) {
}

