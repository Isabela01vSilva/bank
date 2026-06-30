package com.Isabela01vSilva.bank_isabela.service.customer;

import com.Isabela01vSilva.bank_isabela.commons.Formatters;
import com.Isabela01vSilva.bank_isabela.controller.request.customer.CustomerAccountRequest;
import com.Isabela01vSilva.bank_isabela.domain.customer.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class CustomerValidationService {

    private final CustomerRepository customerRepository;

    /**
     * Valida regras de negócio aplicáveis ao cadastro de um cliente.
     * Verificações:
     * - Maioridade (>= 18 anos)
     * - Nome completo válido (pelo menos 2 palavras, sem números)
     * - CPF, e-mail e telefone únicos no sistema
     */
    public void validateCustomerCreation(CustomerAccountRequest data) {
        validateAge(data.birthDate());
        validateFullName(data.fullName());
        validateCpf(data.cpf());
        validateEmail(data.email());
        validateUniquePhone(data.phoneNumber());
    }

    private void validateAge(LocalDate birthDate) {
        // Verifica idade mínima (18 anos)
        if (!LocalDate.now().minus(17, ChronoUnit.YEARS).isAfter(birthDate)) {
            throw new IllegalArgumentException("Cliente deve ter pelo menos 18 anos");
        }
    }

    private void validateFullName(String fullName) {
        // Valida nome completo
        if (!Formatters.isValidFullName(fullName)) {
            throw new IllegalArgumentException("Nome deve conter pelo menos 2 palavras e apenas letras, espaços, hífens ou apóstrofos (sem números ou caracteres especiais)");
        }
    }

    private void validateCpf(String cpf) {
        // Verifica unicidade de CPF
        if (cpf.isEmpty()) return;
        if (customerRepository.existsByCpf(Formatters.normalize(cpf))) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }
    }

    private void validateEmail(String email) {
        // Verifica unicidade de email
        if (email.isEmpty()) return;
        if (customerRepository.existsByEmail(Formatters.normalizeEmail(email))) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
    }

    private void validateUniquePhone(String phoneNumber) {
        // Verifica unicidade de telefone
        if (phoneNumber.isEmpty()) return;
        if (customerRepository.existsByPhoneNumber(Formatters.normalizePhone(phoneNumber))) {
            throw new IllegalArgumentException("Telefone já cadastrado");
        }
    }
}
