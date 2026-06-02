package com.Isabela01vSilva.bank_isabela.controller.response.customer;

import com.Isabela01vSilva.bank_isabela.domain.conta.TipoConta;

import java.time.LocalDate;
import java.util.List;

public record CustomerResponse(Long id,
                               String fullName,
                               LocalDate birthDate,
                               String cpf,
                               String email,
                               String phoneNumber
                               ) {
}
