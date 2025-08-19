package com.Isabela01vSilva.bank_isabela.service.client.dto;

public class CallbackDTO {

    private Long appointmentId;
    private boolean sucesso;

    public CallbackDTO() {
    }

    public CallbackDTO(Long appointmentId, boolean sucesso) {
        this.appointmentId = appointmentId;
        this.sucesso = sucesso;
    }

    // getters e setters
    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }

    public boolean isSucesso() { return sucesso; }
    public void setSucesso(boolean sucesso) { this.sucesso = sucesso; }
}
