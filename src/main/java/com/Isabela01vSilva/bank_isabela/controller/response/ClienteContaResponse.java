package com.Isabela01vSilva.bank_isabela.controller.response;

import com.Isabela01vSilva.bank_isabela.controller.response.cliente.ClienteResponse;
import com.Isabela01vSilva.bank_isabela.controller.response.conta.ContaResponse;

public record ClienteContaResponse(ClienteResponse cliente,
                                   ContaResponse conta) {
}
