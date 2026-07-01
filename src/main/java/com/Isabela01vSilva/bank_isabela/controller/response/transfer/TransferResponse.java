package com.Isabela01vSilva.bank_isabela.controller.response.transfer;

import com.Isabela01vSilva.bank_isabela.domain.transfer.TransferStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferResponse(Long id,
                               String sourceAccountNumber,
                               String destinationAccountNumber,
                               BigDecimal amount,
                               TransferStatus transferStatus,
                               LocalDateTime createdAt,
                               LocalDateTime executionDate) {
}
