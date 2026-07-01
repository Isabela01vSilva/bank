package com.Isabela01vSilva.bank_isabela.service.schedule.client.dto;

import com.Isabela01vSilva.bank_isabela.domain.transfer.TransferStatus;

import java.time.LocalDate;

public record CreateScheduleDTO(LocalDate executionDate,
                                PayloadDTO payloadDTO,
                                String appName,
                                TransferStatus status
) {
}
