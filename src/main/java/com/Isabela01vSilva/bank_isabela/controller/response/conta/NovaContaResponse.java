package com.Isabela01vSilva.bank_isabela.controller.response.conta;

import com.Isabela01vSilva.bank_isabela.domain.conta.TipoConta;

public record NovaContaResponse(
        String numero,
        TipoConta tipoConta,
        Long idCliente
) {
}
