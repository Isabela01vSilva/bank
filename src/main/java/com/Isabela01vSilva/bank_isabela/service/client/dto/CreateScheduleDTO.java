package com.Isabela01vSilva.bank_isabela.service.client.dto;

import java.time.LocalDate;

public record CreateScheduleDTO(LocalDate executionDate,
                                PayloadDTO payloadDTO,
                                String appName
) {
}
