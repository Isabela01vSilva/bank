package com.Isabela01vSilva.bank_isabela.controller.response.history;

import java.math.BigDecimal;

public record TransactionHistoryResponse(
        String cpf,
        String accountNumber,
        String agency,
        BigDecimal amount,
        String des
) {
}
