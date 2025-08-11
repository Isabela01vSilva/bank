package com.Isabela01vSilva.bank_isabela.service.dto;

import com.Isabela01vSilva.bank_isabela.domain.cliente.Cliente;
import com.Isabela01vSilva.bank_isabela.domain.conta.Conta;

public record ClienteContaDTO(Conta conta, Cliente cliente) {
}
