package com.Isabela01vSilva.bank_isabela.controller.response.history;

import com.Isabela01vSilva.bank_isabela.domain.historico.HistoryType;

public record HistoricoSttsContaResponse(
        Long id,
        HistoryType tipoOperacao,
        String descricao
) {
}
