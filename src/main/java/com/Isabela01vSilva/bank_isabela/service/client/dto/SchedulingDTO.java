package com.Isabela01vSilva.bank_isabela.service.client.dto;

import java.time.LocalDate;

public class SchedulingDTO {

    private Long id;
    private LocalDate executionDate;
    private PayloadDTO payload;
    private Status status;

    public SchedulingDTO(LocalDate executionDate, PayloadDTO payload, Status status) {
        this.executionDate = executionDate;
        this.payload = payload;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(LocalDate executionDate) {
        this.executionDate = executionDate;
    }

    public PayloadDTO getPayload() {
        return payload;
    }

    public void setPayload(PayloadDTO payload) {
        this.payload = payload;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
