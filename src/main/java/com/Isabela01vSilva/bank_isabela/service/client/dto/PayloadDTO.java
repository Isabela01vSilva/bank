package com.Isabela01vSilva.bank_isabela.service.client.dto;

import java.math.BigDecimal;

public record PayloadDTO(BigDecimal valor,
                         String numeroContaOrigem,
                         String numeroContaDestino) {
}
