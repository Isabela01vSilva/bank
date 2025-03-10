package com.Isabela01vSilva.bank_isabela.controller.request;

import com.Isabela01vSilva.bank_isabela.domain.conta.TipoConta;

public record ClienteContaRequest(
        String nome,
        String cpf,
        String email,
        String telefone,
        TipoConta tipoConta,
        String numeroAgencia
) {
}
