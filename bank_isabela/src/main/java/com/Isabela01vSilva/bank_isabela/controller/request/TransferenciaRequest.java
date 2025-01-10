package com.Isabela01vSilva.bank_isabela.controller.request;

public record TransferenciaRequest(Double valor,
                                   String numeroContaOrigem,
                                   String numeroContaDestino) {
}
