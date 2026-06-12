package com.Isabela01vSilva.bank_isabela.controller.request.transfer;

import com.Isabela01vSilva.bank_isabela.domain.account.Account;
import com.Isabela01vSilva.bank_isabela.domain.transfer.TransferStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferRequest(Account sourceAccount,
                              Account destinationAccount,
                              BigDecimal amount,
                              LocalDateTime executionDate,
                              TransferStatus transferStatus
                              ) {
}
