package com.Isabela01vSilva.bank_isabela.controller.request.historico;

import com.Isabela01vSilva.bank_isabela.domain.customer.Customer;
import com.Isabela01vSilva.bank_isabela.domain.conta.Conta;
import com.Isabela01vSilva.bank_isabela.domain.historico.TipoOperacao;

public record CadastroHistoricoRequest(
        Conta conta,
        Customer cliente,
        TipoOperacao tipoOperacao,
        String descricao,
        Double valor
) {
}
