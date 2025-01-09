package com.Isabela01vSilva.bank_isabela.domain.historico;

import com.Isabela01vSilva.bank_isabela.domain.cliente.Cliente;
import com.Isabela01vSilva.bank_isabela.domain.conta.Conta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

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
    private Conta conta;

    @ManyToOne
    @JoinColumn(name = "idCliente")
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    private TipoOperacao tipo_operacao;

    private Double valor;
    private String descricao;
    private Date data_transicao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public TipoOperacao getTipo_operacao() {
        return tipo_operacao;
    }

    public void setTipo_operacao(TipoOperacao tipo_operacao) {
        this.tipo_operacao = tipo_operacao;
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

    public Date getData_transicao() {
        return data_transicao;
    }

    public void setData_transicao(Date data_transicao) {
        this.data_transicao = data_transicao;
    }
}
