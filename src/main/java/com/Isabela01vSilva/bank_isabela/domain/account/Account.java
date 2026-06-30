package com.Isabela01vSilva.bank_isabela.domain.account;

import com.Isabela01vSilva.bank_isabela.domain.customer.Customer;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
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

    // Factory method — substitui o mapper com setters em cadeia
    public static Account create(String accountNumber,
                                 String agencyNumber,
                                 AccountType accountType,
                                 Customer customer) {
        Account account = new Account();
        account.accountNumber = accountNumber;
        account.agencyNumber = agencyNumber;
        account.accountType = accountType;
        account.customer = customer;
        account.accountStatus = AccountStatus.ATIVO;
        account.balance = BigDecimal.ZERO;
        account.creationDate = LocalDate.now();
        return account;
    }

    // Operações financeiras
    public void deposit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
    }

    // Consultas de estado
    public boolean isActive() {
        return this.accountStatus.equals(AccountStatus.ATIVO);
    }

    public boolean isClosed() {
        return this.accountStatus.equals(AccountStatus.ENCERRADO);
    }

    public boolean hasInsufficientBalance(BigDecimal amount) {
        return this.balance.compareTo(amount) < 0;
    }

    public boolean hasBalance() {
        return this.balance.compareTo(BigDecimal.ZERO) > 0;
    }

    // Mudança de stts
    public void close(String reason) {
        this.statusChangeReason = reason;
        this.statusChangeDate = LocalDate.now();
        this.accountStatus = AccountStatus.ENCERRADO;
    }

    public void reactivate() {
        this.accountStatus = AccountStatus.ATIVO;
        this.statusChangeDate = LocalDate.now();
    }
}
