package com.Isabela01vSilva.bank_isabela.controller.response.customer;

import com.Isabela01vSilva.bank_isabela.domain.customer.CustomerStatus;

import java.time.LocalDate;

public record CustomerResponse(Long id,
                               String fullName,
                               LocalDate birthDate,
                               String cpf,
                               String email,
                               String phoneNumber,
                               CustomerStatus customertStatus
                               ) {
}
