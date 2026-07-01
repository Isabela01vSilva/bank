package com.Isabela01vSilva.bank_isabela.mapper;

import com.Isabela01vSilva.bank_isabela.controller.response.transfer.TransferResponse;
import com.Isabela01vSilva.bank_isabela.domain.transfer.Transfer;

public final class TransferMappers {

    public static TransferResponse toResponse(Transfer transfer) {
        return new TransferResponse(
                transfer.getId(),
                transfer.getSourceAccount().getAccountNumber(),
                transfer.getDestinationAccount().getAccountNumber(),
                transfer.getAmount(),
                transfer.getTransferStatus(),
                transfer.getCreatedAt(),
                transfer.getExecutionDate()
        );
    }
}
