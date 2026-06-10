package com.Isabela01vSilva.bank_isabela.domain.historico;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {
/*
    List<History> findByContaId(Long conta);

    List<History> findByContaIdAndDataTransacaoBetween(Long id, LocalDate dataInicio, LocalDate dataFim);*/

    List<History> findByCustomerId(Long customerId);

    List<History> findByCustomerIdAndHistoryTypeIn(Long customerId, List<HistoryType> historyType);

}
