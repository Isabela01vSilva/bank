package com.Isabela01vSilva.bank_isabela.controller.response.conta;

import com.Isabela01vSilva.bank_isabela.domain.conta.StatusConta;
import com.Isabela01vSilva.bank_isabela.domain.conta.TipoConta;

import java.time.LocalDate;

public record ContaResponse(
        String numero,
        TipoConta tipoConta,
        StatusConta statusConta,
        Long idCliente,
        Double saldo,
        LocalDate dataCriacao
) {
}
