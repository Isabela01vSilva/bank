package com.Isabela01vSilva.bank_isabela.controller.response;

import com.Isabela01vSilva.bank_isabela.controller.response.customer.CustomerResponse;
import com.Isabela01vSilva.bank_isabela.controller.response.conta.ContaResponse;

public record ClienteContaResponse(CustomerResponse cliente,
                                   ContaResponse conta) {
}
