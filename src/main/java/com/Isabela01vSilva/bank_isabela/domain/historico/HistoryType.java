package com.Isabela01vSilva.bank_isabela.domain.historico;

public enum HistoryType {
    SAQUE,
    DEPOSITO,
    TRANSFERENCIA,
    ATUALIZACAO_STTS_CONTA,

    // Eventos da conta
    ACCOUNT_CREATED,
    ACCOUNT_REACTIVATED,
    ACCOUNT_INACTIVATED,
    ACCOUNT_CLOSED,

    // Movimentações financeiras
    DEPOSIT,
    WITHDRAWAL,
    TRANSFER
}
