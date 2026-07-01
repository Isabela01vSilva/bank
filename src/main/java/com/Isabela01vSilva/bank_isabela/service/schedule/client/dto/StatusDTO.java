package com.Isabela01vSilva.bank_isabela.service.schedule.client.dto;

import com.Isabela01vSilva.bank_isabela.domain.transfer.TransferStatus;

public record StatusDTO(Long id,
                        TransferStatus status,
                        String message) {
}
