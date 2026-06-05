package com.Isabela01vSilva.bank_isabela.controller.request.customer;

import com.Isabela01vSilva.bank_isabela.domain.customer.CustomerStatus;

import java.time.LocalDate;

public record CustomerRequest(
        String fullName,
        LocalDate birthDate,
        String cpf,
        String email,
        String phoneNumber,
        CustomerStatus customerStatus
) {
}
