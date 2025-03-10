package com.Isabela01vSilva.bank_isabela.controller.response.cliente;

public record ClienteResponse(Long id,
                              String nome,
                              String cpf,
                              String email,
                              String telefone
                              ) {
}
