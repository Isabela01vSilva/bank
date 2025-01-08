package com.Isabela01vSilva.bank_isabela.domain.cliente;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "clientes")
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCliente")
    private Long id;

    private String nome;

    @Column(unique = true)
    private String cpf;
    private String email;
    private String telefone;

}
