package com.Isabela01vSilva.bank_isabela.service.schedule.client.dto;

import com.Isabela01vSilva.bank_isabela.domain.transfer.TransferStatus;

public class CallbackDTO {

    private Long appointmentId;
    private TransferStatus status;

    public CallbackDTO() {
    }

    public CallbackDTO(Long appointmentId, TransferStatus status) {
        this.appointmentId = appointmentId;
        this.status = status;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public TransferStatus getStatus() {
        return status;
    }

    public void setStatus(TransferStatus status) {
        this.status = status;
    }
}
