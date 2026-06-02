package com.Isabela01vSilva.bank_isabela.controller.response;

import com.Isabela01vSilva.bank_isabela.controller.response.conta.ContaResponse;
import com.Isabela01vSilva.bank_isabela.controller.response.customer.CustomerResponse;

import java.util.List;

public record ClienteContasResponse(CustomerResponse cliente,
                                    List<ContaResponse> conta) {
}
