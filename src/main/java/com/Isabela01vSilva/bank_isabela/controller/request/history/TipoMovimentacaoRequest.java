package com.Isabela01vSilva.bank_isabela.controller.request.history;

import com.Isabela01vSilva.bank_isabela.domain.historico.HistoryType;

public record TipoMovimentacaoRequest(
        Long id,
        HistoryType historyType
) {
}
