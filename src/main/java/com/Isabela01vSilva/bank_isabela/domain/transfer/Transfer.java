package com.Isabela01vSilva.bank_isabela.domain.transfer;

import com.Isabela01vSilva.bank_isabela.domain.account.Account;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transferencia")
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
}
