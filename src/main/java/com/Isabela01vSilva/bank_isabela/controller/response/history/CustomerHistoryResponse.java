package com.Isabela01vSilva.bank_isabela.controller.response.history;

import com.Isabela01vSilva.bank_isabela.domain.history.HistoryType;

import java.time.LocalDateTime;

public record CustomerHistoryResponse(Long historyId,
                                      HistoryType eventType,
                                      String description,
                                      LocalDateTime transactionDate) {
}
