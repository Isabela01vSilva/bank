package com.Isabela01vSilva.bank_isabela.domain.conta;

import com.Isabela01vSilva.bank_isabela.domain.cliente.Cliente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Table(name = "contas")
@Entity(name = "Conta")
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idConta")
    private Long id;

    @Column(unique = true)
    private Integer numero_conta;

    @Enumerated(EnumType.STRING)
    private TipoConta tipo_conta;

    @Enumerated(EnumType.STRING)
    private StatusConta status_conta;

    @OneToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    private Double saldo;
    private Date data_criacao;

    public void atualizarInformacoes(Conta dados) {
        if (dados.getNumero_conta() != null) {
            this.numero_conta = dados.getNumero_conta();
        }
        if (dados.getTipo_conta() != null) {
            this.tipo_conta = dados.getTipo_conta();
        }
        if (dados.getSaldo() != null) {
            this.saldo = dados.getSaldo();
        }
        if (dados.getStatus_conta() != null) {
           this.status_conta = dados.getStatus_conta();
        }
        if (dados.getData_criacao() != null) {
            this.data_criacao = dados.getData_criacao();
        }
        if (dados.getCliente() != null) {
            this.cliente = dados.getCliente();
        }
    }

    public void desativar(){
        status_conta = StatusConta.DESATIVADA;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumero_conta() {
        return numero_conta;
    }

    public void setNumero_conta(Integer numero_conta) {
        this.numero_conta = numero_conta;
    }

    public TipoConta getTipo_conta() {
        return tipo_conta;
    }

    public void setTipo_conta(TipoConta tipo_conta) {
        this.tipo_conta = tipo_conta;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public StatusConta getStatus_conta() {
        return status_conta;
    }

    public void setStatus_conta(StatusConta status_conta) {
        this.status_conta = status_conta;
    }

    public Date getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(Date data_criacao) {
        this.data_criacao = data_criacao;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
