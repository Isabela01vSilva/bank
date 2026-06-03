package com.Isabela01vSilva.bank_isabela.domain.transfer;

import com.Isabela01vSilva.bank_isabela.domain.account.Account;
import com.Isabela01vSilva.bank_isabela.service.client.dto.Status;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "transferencias")
public class Transferencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "conta_origem_id", nullable = false)
    private Account contaOrigem;

    @ManyToOne
    @JoinColumn(name = "conta_destino_id", nullable = false)
    private Account contaDestino;

    @Column(nullable = false)
    private Double valor;

    @Column(name = "data_execucao", nullable = false)
    private LocalDate dataExecucao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    private Long agendamentoId;

    public Long getAgendamentoId() {
        return agendamentoId;
    }

    public void setAgendamentoId(Long agendamentoId) {
        this.agendamentoId = agendamentoId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getContaOrigem() {
        return contaOrigem;
    }

    public void setContaOrigem(Account contaOrigem) {
        this.contaOrigem = contaOrigem;
    }

    public Account getContaDestino() {
        return contaDestino;
    }

    public void setContaDestino(Account contaDestino) {
        this.contaDestino = contaDestino;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public LocalDate getDataExecucao() {
        return dataExecucao;
    }

    public void setDataExecucao(LocalDate dataExecucao) {
        this.dataExecucao = dataExecucao;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
