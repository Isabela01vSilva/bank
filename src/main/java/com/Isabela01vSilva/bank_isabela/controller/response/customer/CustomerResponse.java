package com.Isabela01vSilva.bank_isabela.controller.response.customer;

import java.time.LocalDate;

public record CustomerResponse(Long id,
                               String fullName,
                               LocalDate birthDate,
                               String cpf,
                               String email,
                               String phoneNumber
                               ) {
}
