package com.Isabela01vSilva.bank_isabela.service.data.request;

import com.Isabela01vSilva.bank_isabela.service.client.dto.PayloadDTO;
import com.Isabela01vSilva.bank_isabela.service.client.dto.Status;

import java.time.LocalDate;

public record CreateAppointmentScheduleRequest(LocalDate executionDate,
                                               PayloadDTO payload,
                                               String appName,
                                               Status status) {
}
