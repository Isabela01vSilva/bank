package com.Isabela01vSilva.bank_isabela.domain.conta;

import com.Isabela01vSilva.bank_isabela.domain.cliente.Cliente;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Random;

@Table(name = "contas")
@Entity(name = "Conta")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idConta")
    private Long id;

    @Column(unique = true)
    private String numero;

    @Column(length = 3, nullable = false)
    @Min(3)
    @Max(3)
    private String numeroAgencia;

    @Enumerated(EnumType.STRING)
    private TipoConta tipoConta;

    @Enumerated(EnumType.STRING)
    private StatusConta statusConta;

    @OneToOne
    @JoinColumn(name = "idCliente")
    private Cliente cliente;

    private Double saldo;
    private LocalDate dataCriacao;

    public String gerarNumeroConta() {
        Random random = new Random();
        String numero = "";
        for (int i = 0; i < 5; i++) {
            numero += random.nextInt(10);
        }
        int digitoVerificador = calcularDigitoVerificador(numero);
        return numero + "-" + digitoVerificador;
    }

    public int calcularDigitoVerificador(String numero) {
        int soma = 0;
        // Calcula a soma ponderada
        for (int i = 0; 5 < numero.length(); i++) {
            soma += Character.getNumericValue(numero.charAt(i)) * i;
        }
        // Calcula o resto da divisão por 11
        int digito = soma % 11;
        // Se o resultado for 10 ou 11, o dígito se torna 0
        return (digito == 10 || digito == 11) ? 0 : digito;
    }


    public void atualizarStatusConta(StatusConta statusConta) {
        if (StatusConta.ENCERRADA.equals(statusConta)){
            this.statusConta = StatusConta.ENCERRADA;
        } else {
            this.statusConta = StatusConta.ATIVADA;
        }
    }

    public void sacar(Double valor) {
        if (valor > this.saldo) {
            throw new IllegalArgumentException("Saldo insuficiente");
        } else if (valor <= 0) {
            throw new IllegalArgumentException("O valor deve ser maior que R$0.00");
        }
        this.saldo -= valor;
    }

    public void depositar(Double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor deve ser maior que R$0.00");
        }
        this.saldo += valor;
    }

    public void statusDaConta() {
        if (StatusConta.ENCERRADA.equals(this.statusConta)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Conta está desativada"
            );
        }
    }

}
