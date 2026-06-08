package com.Isabela01vSilva.bank_isabela.controller.request.account;

import java.math.BigDecimal;

public record AccountTransactionRequest (
        String agencyNumber,
        String accountNumber,
        BigDecimal amount
){
}
