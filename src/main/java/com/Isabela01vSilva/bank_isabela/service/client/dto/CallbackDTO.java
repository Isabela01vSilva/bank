package com.Isabela01vSilva.bank_isabela.service.client.dto;

public class CallbackDTO {

    private Long appointmentId;
    private Status status;

    public CallbackDTO() {
    }

    public CallbackDTO(Long appointmentId, Status status) {
        this.appointmentId = appointmentId;
        this.status = status;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
