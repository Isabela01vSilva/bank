package com.Isabela01vSilva.bank_isabela.mapper;

import com.Isabela01vSilva.bank_isabela.controller.response.history.CustomerHistoryResponse;
import com.Isabela01vSilva.bank_isabela.controller.response.history.HistoryResponse;
import com.Isabela01vSilva.bank_isabela.controller.response.history.TransactionHistoryResponse;
import com.Isabela01vSilva.bank_isabela.domain.account.Account;
import com.Isabela01vSilva.bank_isabela.domain.historico.History;
import com.Isabela01vSilva.bank_isabela.domain.historico.HistoryType;

import java.math.BigDecimal;

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

    public static History createTransferOut(
            Account source,
            Account destination,
            BigDecimal amount
    ) {
        History history = new History();

        history.setAccount(source);
        history.setCustomer(source.getCustomer());
        history.setHistoryType(HistoryType.TRANSFER);
        history.setAmount(amount);
        history.setDescription(
                String.format(
                        "Transferência enviada para agência %s conta %s no valor de R$ %s",
                        destination.getAgencyNumber(),
                        destination.getAccountNumber(),
                        amount
                )
        );

        return history;
    }

    public static History createTransferIn(
            Account source,
            Account destination,
            BigDecimal amount
    ) {
        History history = new History();

        history.setAccount(destination);
        history.setCustomer(destination.getCustomer());
        history.setHistoryType(HistoryType.TRANSFER);
        history.setAmount(amount);
        history.setDescription(
                String.format(
                        "Transferência recebida da agência %s conta %s no valor de R$ %s",
                        source.getAgencyNumber(),
                        source.getAccountNumber(),
                        amount
                )
        );

        return history;
    }
}
