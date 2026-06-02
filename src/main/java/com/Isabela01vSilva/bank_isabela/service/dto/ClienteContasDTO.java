package com.Isabela01vSilva.bank_isabela.service.dto;

import com.Isabela01vSilva.bank_isabela.domain.conta.Conta;
import com.Isabela01vSilva.bank_isabela.domain.customer.Customer;

import java.util.List;

public record ClienteContasDTO(List<Conta> conta, Customer customer) {
}
