package com.Isabela01vSilva.bank_isabela.domain.conta;

import com.Isabela01vSilva.bank_isabela.domain.cliente.Cliente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "contas")
@Entity(name = "Conta")
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idConta")
    private Long id;

    private Integer numero_conta;
    private TipoConta tipo_conta;
    private Double saldo;
    private Boolean status_conta;
    private Date data_criacao;

    @OneToOne
    @JoinColumn(name = "idCliente")
    private Cliente cliente;
}
