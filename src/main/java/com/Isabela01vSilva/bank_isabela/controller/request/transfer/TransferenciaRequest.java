package com.Isabela01vSilva.bank_isabela.controller.request.transfer;

import com.Isabela01vSilva.bank_isabela.domain.transfer.TransferStatus;

import java.time.LocalDate;

public record TransferenciaRequest(LocalDate executionDate,
                                   Double valor,
                                   String numeroContaOrigem,
                                   String numeroContaDestino,
                                   TransferStatus status,
                                   String executionPath) {
}
