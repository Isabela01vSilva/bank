package com.Isabela01vSilva.bank_isabela.controller.request;

public record TransferenciaRequest(Long id,
                                   Double valor,
                                   String numeroContaOrigem,
                                   String numeroContaDestino) {
}
