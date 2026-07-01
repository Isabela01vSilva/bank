package com.Isabela01vSilva.bank_isabela.controller.response.history;

import com.Isabela01vSilva.bank_isabela.domain.history.HistoryType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record HistoryResponse(Long id,
                              String cpf,
                              String agencyNumber,
                              String accountNumber,
                              HistoryType historyType,
                              String description,
                              BigDecimal amount,
                              LocalDateTime transactionDate) {
}
