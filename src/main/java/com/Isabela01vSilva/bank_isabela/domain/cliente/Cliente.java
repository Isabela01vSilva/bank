package com.Isabela01vSilva.bank_isabela.domain.cliente;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Table(name = "clientes")
@Entity
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

    public void atualizarInformacoes(Cliente dados) {
        if (dados.getNome() != null) {
            this.nome = dados.getNome();
        }
        if (dados.getCpf() != null) {
            this.cpf = dados.getCpf();
        }
        if (dados.getEmail() != null) {
            this.email = dados.getEmail();
        }
        if (dados.getTelefone() != null) {
            this.telefone = dados.getTelefone();
        }

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
