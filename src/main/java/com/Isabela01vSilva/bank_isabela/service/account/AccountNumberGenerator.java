package com.Isabela01vSilva.bank_isabela.service.account;

import com.Isabela01vSilva.bank_isabela.domain.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class AccountNumberGenerator {

    private static final int MAX_ACCOUNT_NUMBER = 10;
    private final AccountRepository accountRepository;

    public String generateAccountNumber() {
        while (true) {
            String accountNumber = generateRandomAccountNumber();
            if (!accountRepository.existsByAccountNumber(accountNumber)) {
                return accountNumber;
            }
        }
    }

    private String generateRandomAccountNumber() {
        Random random = new Random();
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            number.append(random.nextInt(10));
        }
        return number + "-" + calculateCheckDigit(number.toString());
    }

    private int calculateCheckDigit(String number) {
        int sum = 0;
        // Calcula a soma ponderada
        for (int i = 0; i < 5 && i < number.length(); i++) {
            sum += Character.getNumericValue(number.charAt(i)) * i;
        }
        // Calcula o resto da divisão por 11
        int digit = sum % 11;
        // Se o resultado for 10, o dígito se torna 0
        return (digit == 10) ? 0 : digit;
    }
}
