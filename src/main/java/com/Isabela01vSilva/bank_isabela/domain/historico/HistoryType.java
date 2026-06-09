package com.Isabela01vSilva.bank_isabela.domain.historico;

public enum HistoryType {

    // Eventos da conta
    ACCOUNT_CREATED,
    ACCOUNT_REACTIVATED,
    ACCOUNT_CLOSED,

    //Evento do cliente
    CUSTOMER_UPDATED,
    CUSTOMER_REACTIVATED,
    CUSTOMER_INACTIVATED,

    // Movimentações financeiras
    DEPOSIT,
    WITHDRAWAL,
    TRANSFER
}
