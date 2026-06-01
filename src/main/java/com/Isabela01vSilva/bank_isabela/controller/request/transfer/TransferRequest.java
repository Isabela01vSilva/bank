package com.Isabela01vSilva.bank_isabela.controller.request.transfer;

public record TransferRequest(Double valor,
                              String numeroContaOrigem,
                              String numeroContaDestino) {
}
