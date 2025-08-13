package com.Isabela01vSilva.bank_isabela.service.client.dto;

import java.math.BigDecimal;

public record PayloadDTO(Double valor,
                         String numeroContaOrigem,
                         String numeroContaDestino) {
}
