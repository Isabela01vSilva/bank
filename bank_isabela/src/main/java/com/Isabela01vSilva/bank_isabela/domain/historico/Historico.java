package com.Isabela01vSilva.bank_isabela.domain.historico;

import com.Isabela01vSilva.bank_isabela.domain.cliente.Cliente;
import com.Isabela01vSilva.bank_isabela.domain.conta.Conta;
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

}
