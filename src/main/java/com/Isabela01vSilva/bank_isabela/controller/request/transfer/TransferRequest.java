package com.Isabela01vSilva.bank_isabela.controller.request.transfer;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferRequest(String sourceAgencyNumber,
                              String sourceAccountNumber,
                              String destinationAgencyNumber,
                              String destinationAccountNumber,
                              BigDecimal amount,
                              LocalDateTime executionDate
) {
}
