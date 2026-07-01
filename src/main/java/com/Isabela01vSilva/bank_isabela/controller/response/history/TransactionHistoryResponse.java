package com.Isabela01vSilva.bank_isabela.controller.response.history;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionHistoryResponse(String cpf,
                                         String accountNumber,
                                         String agencyNumber,
                                         BigDecimal amount,
                                         String description,
                                         LocalDateTime transactionDate) {
}
