package com.Isabela01vSilva.bank_isabela.commons;

public class Formatters {
    public static String normalize(String cpf) {
        cpf = cpf.replace(".", "");
        cpf = cpf.replace("-", "");
        return cpf;
    }

    public static String normalizePhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return phone;
        }

        phone = phone.replace("(", "")
                     .replace(")", "")
                     .replace(" ", "")
                     .replace("-", "");
        return phone;
    }

    public static String formatCPF(String cpf) {
        if (cpf == null || cpf.isEmpty()) {
            return cpf;
        }

        // Remove pontos e traços se já estiverem formatados
        cpf = normalize(cpf);

        // Valida se tem 11 dígitos
        if (cpf.length() != 11) {
            return cpf;
        }

        // Formata como XXX.XXX.XXX-XX
        return String.format("%s.%s.%s-%s",
            cpf.substring(0, 3),
            cpf.substring(3, 6),
            cpf.substring(6, 9),
            cpf.substring(9, 11)
        );
    }

    public static String formatPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return phone;
        }

        // Remove parênteses, espaços e traços se já estiverem formatados
        phone = normalizePhone(phone);

        // Formata como (XX) XXXXX-XXXX para celular (11 dígitos)
        if (phone.length() == 11) {
            return String.format("(%s) %s-%s",
                phone.substring(0, 2),
                phone.substring(2, 7),
                phone.substring(7, 11)
            );
        }

        // Formata como (XX) XXXX-XXXX para fixo (10 dígitos)
        if (phone.length() == 10) {
            return String.format("(%s) %s-%s",
                phone.substring(0, 2),
                phone.substring(2, 6),
                phone.substring(6, 10)
            );
        }

        return phone;
    }
}


