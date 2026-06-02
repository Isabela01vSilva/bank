package com.Isabela01vSilva.bank_isabela.controller.request.conta;

import com.Isabela01vSilva.bank_isabela.domain.customer.Customer;
import com.Isabela01vSilva.bank_isabela.domain.conta.TipoConta;

public record CriarContaDTO(
        TipoConta tipoConta,
        Customer cliente
) {}
