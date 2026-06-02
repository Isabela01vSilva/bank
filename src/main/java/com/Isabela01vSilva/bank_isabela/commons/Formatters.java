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

    /**
     * Normaliza um email: remove espaços extras e converte para minúsculas.
     * Ex.: " Exemplo@Domínio.COM " -> "exemplo@domínio.com"
     */
    public static String normalizeEmail(String email) {
        if (email == null || email.isEmpty()) {
            return email;
        }

        // Remove espaços em branco (inclui espaços internos acidentais) e converte para lower-case
        email = email.trim().replaceAll("\\s+", "");
        return email.toLowerCase();
    }

    /**
     * Formata um email aplicando a normalização. Retorna o email normalizado.
     * Atualmente a formatação é apenas normalizar (trim e minúsculas).
     */
    public static String formatEmail(String email) {
        return normalizeEmail(email);
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

    /**
     * Valida nome completo: deve ter pelo menos 2 palavras, sem números ou caracteres especiais.
     * Permite apenas letras, espaços, hífens e apóstrofos.
     * Ex.: "João Silva" é válido, "João" é inválido, "João123" é inválido
     */
    public static boolean isValidFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return false;
        }

        String trimmed = fullName.trim();

        // Verifica se contém números
        if (trimmed.matches(".*\\d.*")) {
            return false;
        }

        // Permite apenas letras, espaços, hífens e apóstrofos
        if (!trimmed.matches("[a-zA-ZÀ-ÿ\\s'\\-]+")) {
            return false;
        }

        // Verifica se tem pelo menos 2 palavras
        String[] words = trimmed.split("\\s+");
        return words.length >= 2;
    }
}


