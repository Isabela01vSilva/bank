package com.Isabela01vSilva.bank_isabela.service.client.dto;

import java.time.LocalDate;

public record SchedulingDTO(Long id,
                            LocalDate executionDate,
                            PayloadDTO payload,
                            Status status) {
}
