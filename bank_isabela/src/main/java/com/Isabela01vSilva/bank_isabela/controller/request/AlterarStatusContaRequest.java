package com.Isabela01vSilva.bank_isabela.controller.request;

import com.Isabela01vSilva.bank_isabela.domain.conta.StatusConta;

public record AlterarStatusContaRequest(Long id, StatusConta statusConta) {
}
