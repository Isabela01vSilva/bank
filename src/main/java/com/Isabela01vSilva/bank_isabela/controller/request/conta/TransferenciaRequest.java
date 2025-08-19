package com.Isabela01vSilva.bank_isabela.controller.request.conta;

import com.Isabela01vSilva.bank_isabela.service.client.dto.Status;

import java.time.LocalDate;

public record TransferenciaRequest(LocalDate executionDate,
                                   Double valor,
                                   String numeroContaOrigem,
                                   String numeroContaDestino,
                                   Status status,
                                   String executionPath) {
}
