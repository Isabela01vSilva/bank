package com.Isabela01vSilva.bank_isabela.domain.history;

import com.Isabela01vSilva.bank_isabela.domain.customer.Customer;
import com.Isabela01vSilva.bank_isabela.domain.account.Account;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Table(name = "historicos")
@Entity(name = "Historico")
@AllArgsConstructor
@NoArgsConstructor
public class History {

    @Id
    @Column(name = "id_historico")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_conta")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_operacao", nullable = false)
    private HistoryType historyType;

    @Column(name = "valor")
    private BigDecimal amount;

    @Column(name = "descricao")
    private String description;

    @Column(name = "data_transacao", nullable = false)
    private LocalDateTime transactionDate;

    public boolean hasAccount() {
        return this.account != null;
    }
}