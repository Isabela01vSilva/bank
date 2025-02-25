package com.Isabela01vSilva.bank_isabela.controller.response;

import com.Isabela01vSilva.bank_isabela.domain.cliente.Cliente;

import java.time.LocalDate;

public record HistoricoResponse(
        Long id,
        String cliente,
        Double valor,
        String descricao,
        LocalDate dataTransicao
) {
}
