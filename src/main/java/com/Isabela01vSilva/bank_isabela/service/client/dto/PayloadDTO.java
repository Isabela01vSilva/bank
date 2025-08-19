package com.Isabela01vSilva.bank_isabela.service.client.dto;

public class PayloadDTO {

    private Double valor;
    private String numeroContaOrigem;
    private String numeroContaDestino;
    private Long appointmentId;

    public PayloadDTO(Double valor,
                      String numeroContaOrigem,
                      String numeroContaDestino) {
        this.valor = valor;
        this.numeroContaDestino = numeroContaDestino;
        this.numeroContaOrigem = numeroContaOrigem;
    }

    public PayloadDTO() {
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getNumeroContaOrigem() {
        return numeroContaOrigem;
    }

    public void setNumeroContaOrigem(String numeroContaOrigem) {
        this.numeroContaOrigem = numeroContaOrigem;
    }

    public String getNumeroContaDestino() {
        return numeroContaDestino;
    }

    public void setNumeroContaDestino(String numeroContaDestino) {
        this.numeroContaDestino = numeroContaDestino;
    }
}
