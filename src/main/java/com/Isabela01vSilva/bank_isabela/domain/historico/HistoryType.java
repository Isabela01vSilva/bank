package com.Isabela01vSilva.bank_isabela.domain.historico;

public enum HistoryType {

    // Eventos da conta
    ACCOUNT_CREATED,
    ACCOUNT_REACTIVATED,
    ACCOUNT_CLOSED,

    //Evento do cliente
    CUSTOMER_UPDATED,

    //Registrar CUSTOMER_INACTIVATED somente quando o status do cliente mudar de ATIVO para INATIVO.
    CUSTOMER_REACTIVATED,

    //Registrar CUSTOMER_REACTIVATED somente quando o status do cliente mudar de INATIVO para ATIVO.
    CUSTOMER_INACTIVATED,

    // Movimentações financeiras
    DEPOSIT,
    WITHDRAWAL,
    TRANSFER
}
