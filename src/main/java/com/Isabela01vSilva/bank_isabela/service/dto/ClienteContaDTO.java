package com.Isabela01vSilva.bank_isabela.service.dto;

import com.Isabela01vSilva.bank_isabela.domain.customer.Customer;
import com.Isabela01vSilva.bank_isabela.domain.conta.Conta;

public record ClienteContaDTO(Conta conta, Customer customer) {
}
