package com.Isabela01vSilva.bank_isabela.domain.transfer;

import com.Isabela01vSilva.bank_isabela.domain.account.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "transferencia")
@NoArgsConstructor
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "conta_origem_id", nullable = false)
    private Account sourceAccount;

    @ManyToOne
    @JoinColumn(name = "conta_destino_id", nullable = false)
    private Account destinationAccount;

    @Column(name = "valor", nullable = false)
    private BigDecimal amount;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "data_execucao", nullable = false)
    private LocalDateTime executionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_transferencia", nullable = false)
    private TransferStatus transferStatus;

    public static Transfer create(Account sourceAccount, Account destinationAccount, BigDecimal amount) {
        Transfer transfer = new Transfer();
        transfer.sourceAccount = sourceAccount;
        transfer.destinationAccount = destinationAccount;
        transfer.amount = amount;
        transfer.createdAt = LocalDateTime.now();
        transfer.transferStatus = TransferStatus.PROCESSING;
        return transfer;
    }

    public void complete() {
        this.transferStatus  = TransferStatus.COMPLETED;
        this.executionDate   = LocalDateTime.now();
    }

    public void fail() {
        this.transferStatus  = TransferStatus.FAILED;
        this.executionDate   = LocalDateTime.now();
    }
}
