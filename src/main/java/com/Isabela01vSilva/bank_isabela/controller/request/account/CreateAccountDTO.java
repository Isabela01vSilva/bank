package com.Isabela01vSilva.bank_isabela.controller.request.account;

import com.Isabela01vSilva.bank_isabela.domain.customer.Customer;
import com.Isabela01vSilva.bank_isabela.domain.account.AccountType;

public record CreateAccountDTO(
        AccountType tipoConta,
        Customer cliente
) {}
