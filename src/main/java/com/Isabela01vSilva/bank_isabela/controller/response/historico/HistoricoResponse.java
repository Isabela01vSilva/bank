package com.Isabela01vSilva.bank_isabela.controller.response.historico;

import java.time.LocalDate;

public record HistoricoResponse(
        Long id,
        String cliente,
        Double valor,
        String descricao,
        LocalDate dataTransacao
) {
}
