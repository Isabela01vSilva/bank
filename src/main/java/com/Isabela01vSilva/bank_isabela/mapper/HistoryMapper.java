package com.Isabela01vSilva.bank_isabela.mapper;

import com.Isabela01vSilva.bank_isabela.controller.response.history.CustomerHistoryResponse;
import com.Isabela01vSilva.bank_isabela.controller.response.history.HistoryResponse;
import com.Isabela01vSilva.bank_isabela.controller.response.history.TransactionHistoryResponse;
import com.Isabela01vSilva.bank_isabela.domain.historico.History;

public class HistoryMapper {
    public static TransactionHistoryResponse toTransactionResponse(History history) {
        return new TransactionHistoryResponse(
                history.getCustomer().getCpf(),
                history.getAccount().getAccountNumber(),
                history.getAccount().getAgencyNumber(),
                history.getAmount(),
                history.getDescription()
        );
    }

    public static CustomerHistoryResponse toCustomerResponse(History history) {
        return new CustomerHistoryResponse(
                history.getId(),
                history.getHistoryType(),
                history.getDescription()
        );
    }

    public static HistoryResponse toResponse(History history) {
        return new HistoryResponse(
                history.getId(),
                history.getCustomer().getCpf(),
                history.getAccount().getAgencyNumber(),
                history.getAccount().getAccountNumber(),
                history.getHistoryType(),
                history.getDescription(),
                history.getAmount(),
                history.getTransactionDate()
        );
    }
}
