package com.Isabela01vSilva.bank_isabela.controller.request;

import com.Isabela01vSilva.bank_isabela.domain.account.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Email;

import java.time.LocalDate;
import java.util.List;

public record CustomerAccountRequest(

        @NotBlank(message = "Nome é obrigatório")
        String fullName,

        @NotNull(message = "Data de nascimento é obrigatória")
        @Past(message = "Data de nascimento deve ser uma data válida")
        LocalDate birthDate,
        @NotBlank(message = "CPF é obrigatório")
        String cpf,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        String email,

        @NotBlank(message = "Telefone é obrigatório")
        String phoneNumber,

        @NotNull(message = "Tipo de conta é obrigatório")
        List<AccountType> accountTypes
) {
}
