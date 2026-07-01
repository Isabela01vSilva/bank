package com.Isabela01vSilva.bank_isabela.mapper;

import com.Isabela01vSilva.bank_isabela.controller.response.history.CustomerHistoryResponse;
import com.Isabela01vSilva.bank_isabela.controller.response.history.HistoryResponse;
import com.Isabela01vSilva.bank_isabela.controller.response.history.TransactionHistoryResponse;
import com.Isabela01vSilva.bank_isabela.domain.history.History;

public class HistoryMapper {

    public static HistoryResponse toResponse(History history) {
        String agencyNumber = history.hasAccount() ? history.getAccount().getAgencyNumber() : null;
        String accountNumber = history.hasAccount() ? history.getAccount().getAccountNumber() : null;

        return new HistoryResponse(
                history.getId(),
                history.getCustomer().getCpf(),
                agencyNumber,
                accountNumber,
                history.getHistoryType(),
                history.getDescription(),
                history.getAmount(),
                history.getTransactionDate()
        );
    }

    public static TransactionHistoryResponse toTransactionResponse(History history) {
        return new TransactionHistoryResponse(
                history.getCustomer().getCpf(),
                history.getAccount().getAccountNumber(),
                history.getAccount().getAgencyNumber(),
                history.getAmount(),
                history.getDescription(),
                history.getTransactionDate()
        );
    }

    public static CustomerHistoryResponse toCustomerResponse(History history) {
        return new CustomerHistoryResponse(
                history.getId(),
                history.getHistoryType(),
                history.getDescription(),
                history.getTransactionDate()
        );
    }
}
