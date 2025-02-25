package com.Isabela01vSilva.bank_isabela.controller.request;

import com.Isabela01vSilva.bank_isabela.domain.cliente.Cliente;
import com.Isabela01vSilva.bank_isabela.domain.conta.Conta;
import com.Isabela01vSilva.bank_isabela.domain.historico.TipoOperacao;

public record CadastroHistoricoRequest(
        Conta conta,
        Cliente cliente,
        TipoOperacao tipoOperacao,
        String descricao,
        Double valor
) {
}
