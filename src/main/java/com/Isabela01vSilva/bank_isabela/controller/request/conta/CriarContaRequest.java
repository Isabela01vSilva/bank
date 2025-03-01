package com.Isabela01vSilva.bank_isabela.controller.request.conta;

import com.Isabela01vSilva.bank_isabela.domain.conta.TipoConta;

public record CriarContaRequest(
        String numero,
        TipoConta tipoConta,
        Long idCliente
) {}
