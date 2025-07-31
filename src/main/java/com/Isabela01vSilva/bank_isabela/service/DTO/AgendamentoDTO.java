package com.Isabela01vSilva.bank_isabela.service.DTO;

import java.time.LocalDateTime;

public record AgendamentoDTO(Long id,
                             String descricao,
                             LocalDateTime dataHora) {
}
