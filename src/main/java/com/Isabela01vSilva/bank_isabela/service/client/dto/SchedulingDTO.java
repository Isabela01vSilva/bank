package com.Isabela01vSilva.bank_isabela.service.client.dto;

import com.Isabela01vSilva.bank_isabela.domain.transfer.TransferStatus;

import java.time.LocalDate;

public class SchedulingDTO {

    private Long id;
    private LocalDate executionDate;
    private PayloadDTO payload;
    private TransferStatus status;

    public SchedulingDTO(LocalDate executionDate, PayloadDTO payload, TransferStatus status) {
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

    public TransferStatus getStatus() {
        return status;
    }

    public void setStatus(TransferStatus status) {
        this.status = status;
    }
}
