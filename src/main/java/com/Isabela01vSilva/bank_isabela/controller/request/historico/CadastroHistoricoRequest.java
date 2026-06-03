package com.Isabela01vSilva.bank_isabela.controller.request.historico;

import com.Isabela01vSilva.bank_isabela.domain.customer.Customer;
import com.Isabela01vSilva.bank_isabela.domain.account.Account;
import com.Isabela01vSilva.bank_isabela.domain.historico.OperationType;

public record CadastroHistoricoRequest(
        Account conta,
        Customer cliente,
        OperationType tipoOperacao,
        String descricao,
        Double valor
) {
}
