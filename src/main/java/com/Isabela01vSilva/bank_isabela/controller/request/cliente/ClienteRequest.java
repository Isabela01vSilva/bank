package com.Isabela01vSilva.bank_isabela.controller.request.cliente;

public record ClienteRequest(
        String nome,
        String cpf,
        String email,
        String telefone
) {
}
