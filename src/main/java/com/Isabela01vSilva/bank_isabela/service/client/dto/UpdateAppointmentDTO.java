package com.Isabela01vSilva.bank_isabela.service.client.dto;

import java.time.LocalDate;

public class UpdateAppointmentDTO {

    private LocalDate executionDate;
    private PayloadDTO payload;
    private String appName;
    private Status status;

    public UpdateAppointmentDTO() {
        // Construtor vazio para frameworks (Jackson, etc)
    }

    public UpdateAppointmentDTO(LocalDate executionDate, PayloadDTO payload, String appName, Status status) {
        this.executionDate = executionDate;
        this.payload = payload;
        this.appName = appName;
        this.status = status;
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

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
