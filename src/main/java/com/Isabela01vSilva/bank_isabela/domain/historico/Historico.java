package com.Isabela01vSilva.bank_isabela.domain.historico;

import com.Isabela01vSilva.bank_isabela.domain.customer.Customer;
import com.Isabela01vSilva.bank_isabela.domain.account.Account;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Table(name = "historicos")
@Entity(name = "Historico")

public class Historico {

    @Id
    @Column(name = "idHistorico")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idConta")
    private Account conta;

    @ManyToOne
    @JoinColumn(name = "idCliente")
    private Customer cliente;

    @Enumerated(EnumType.STRING)
    private OperationType tipoOperacao;

    private Double valor;
    private String descricao;
    private LocalDate dataTransacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getConta() {
        return conta;
    }

    public void setConta(Account conta) {
        this.conta = conta;
    }

    public Customer getCliente() {
        return cliente;
    }

    public void setCliente(Customer cliente) {
        this.cliente = cliente;
    }

    public OperationType getTipoOperacao() {
        return tipoOperacao;
    }

    public void setTipoOperacao(OperationType tipoOperacao) {
        this.tipoOperacao = tipoOperacao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataTransacao() {
        return dataTransacao;
    }

    public void setDataTransacao(LocalDate dataTransacao) {
        this.dataTransacao = dataTransacao;
    }
}
