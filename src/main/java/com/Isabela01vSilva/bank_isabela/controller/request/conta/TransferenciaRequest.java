package com.Isabela01vSilva.bank_isabela.controller.request.conta;

import java.time.LocalDate;

public record TransferenciaRequest(LocalDate executionDate,
                                   Double valor,
                                   String numeroContaOrigem,
                                   String numeroContaDestino) {
}
