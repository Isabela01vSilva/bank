package com.Isabela01vSilva.bank_isabela.domain.history;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {
    List<History> findByCustomerId(Long customerId);
    List<History> findByCustomerIdAndHistoryTypeIn(Long customerId, List<HistoryType> historyType);
    List<History> findByAccountIdAndHistoryTypeIn(Long accountId, List<HistoryType> historyType);
    List<History> findByAccountIdAndTransactionDateBetween(Long accountId, LocalDateTime startDate, LocalDateTime endDate);
    boolean existsByCustomerId(Long customerId);
}
