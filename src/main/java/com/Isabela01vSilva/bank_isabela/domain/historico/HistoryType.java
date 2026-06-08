package com.Isabela01vSilva.bank_isabela.domain.historico;

public enum HistoryType {

    // Eventos da conta
    ACCOUNT_CREATED,
    ACCOUNT_REACTIVATED,
    ACCOUNT_INACTIVATED,
    ACCOUNT_CLOSED,

    //Evento do cliente
    CUSTOMER_UPDATED,

    // Movimentações financeiras
    DEPOSIT,
    WITHDRAWAL,
    TRANSFER
}
