package com.Isabela01vSilva.bank_isabela.controller.request;

import com.Isabela01vSilva.bank_isabela.domain.conta.TipoConta;

import java.time.LocalDate;

public record CustomerAccountRequest(
        String fullName,
        LocalDate birthDate,
        String cpf,
        String email,
        String phoneNumber,
        TipoConta accountType
) {
}
