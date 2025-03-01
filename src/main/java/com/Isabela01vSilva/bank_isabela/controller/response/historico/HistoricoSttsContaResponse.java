package com.Isabela01vSilva.bank_isabela.controller.response.historico;

import com.Isabela01vSilva.bank_isabela.domain.historico.TipoOperacao;

public record HistoricoSttsContaResponse(
        Long id,
        TipoOperacao tipoOperacao,
        String descricao
) {
}
