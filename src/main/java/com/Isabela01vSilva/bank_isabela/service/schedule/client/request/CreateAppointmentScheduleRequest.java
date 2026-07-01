package com.Isabela01vSilva.bank_isabela.service.schedule.client.request;

import com.Isabela01vSilva.bank_isabela.service.schedule.client.dto.PayloadDTO;
import com.Isabela01vSilva.bank_isabela.domain.transfer.TransferStatus;

import java.time.LocalDate;

public record CreateAppointmentScheduleRequest(LocalDate executionDate,
                                               PayloadDTO payload,
                                               String appName,
                                               TransferStatus status) {
}
