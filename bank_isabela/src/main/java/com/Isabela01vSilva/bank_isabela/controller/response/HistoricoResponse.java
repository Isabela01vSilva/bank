package com.Isabela01vSilva.bank_isabela.controller.response;

import java.time.LocalDate;

public record HistoricoResponse(
        Long id,
        Double valor,
        String descricao,
        LocalDate dataTransicao
) {
}
