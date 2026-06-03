package com.Isabela01vSilva.bank_isabela.controller.response.historico;

import com.Isabela01vSilva.bank_isabela.domain.historico.OperationType;

public record HistoricoSttsContaResponse(
        Long id,
        OperationType tipoOperacao,
        String descricao
) {
}
