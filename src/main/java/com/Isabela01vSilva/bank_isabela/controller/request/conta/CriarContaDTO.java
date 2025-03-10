package com.Isabela01vSilva.bank_isabela.controller.request.conta;

import com.Isabela01vSilva.bank_isabela.domain.cliente.Cliente;
import com.Isabela01vSilva.bank_isabela.domain.conta.TipoConta;

public record CriarContaDTO(
        String numeroAgencia,
        TipoConta tipoConta,
        Cliente cliente
) {}
