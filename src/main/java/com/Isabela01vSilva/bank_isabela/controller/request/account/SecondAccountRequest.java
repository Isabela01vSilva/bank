package com.Isabela01vSilva.bank_isabela.controller.request.account;

import com.Isabela01vSilva.bank_isabela.domain.account.AccountType;

public record SecondAccountRequest(
        String cpf,
        AccountType accountType
) {}

