package com.Isabela01vSilva.bank_isabela.domain.account;

import com.Isabela01vSilva.bank_isabela.domain.customer.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Table(name = "contas")
@Entity(name = "Conta")
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conta")
    private Long id;

    @Column(name = "numero_conta", unique = true, nullable = false)
    private String accountNumber;

    @Column(name = "numero_agencia", nullable = false)
    private String agencyNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_conta", nullable = false)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_conta", nullable = false)
    private AccountStatus accountStatus;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Customer customer;

    @Column(name = "saldo", nullable = false)
    private BigDecimal balance;

    @Column(name = "data_criacao", nullable = false)
    private LocalDate creationDate;

    @Column(name = "data_alteracao_status")
    private LocalDate statusChangeDate;

    @Column(name = "motivo_alteracao_status")
    private String statusChangeReason;

    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(this.balance) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente");
        } else if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O saldo deve ser maior que R$0.00");
        }
        this.balance = this.balance.subtract(amount);
    }

    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O valor deve ser maior que R$0.00");
        }
        this.balance = this.balance.add(amount);
    }

    public void accountStatus() {
        if (AccountStatus.ENCERRADO.equals(this.accountStatus)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Conta está desativada"
            );
        }
    }
}
