package com.Isabela01vSilva.bank_isabela.service.history;

import com.Isabela01vSilva.bank_isabela.controller.response.history.CustomerHistoryResponse;
import com.Isabela01vSilva.bank_isabela.controller.response.history.TransactionHistoryResponse;
import com.Isabela01vSilva.bank_isabela.domain.account.AccountType;
import com.Isabela01vSilva.bank_isabela.domain.customer.CustomerRepository;
import com.Isabela01vSilva.bank_isabela.domain.history.History;
import com.Isabela01vSilva.bank_isabela.domain.history.HistoryRepository;
import com.Isabela01vSilva.bank_isabela.domain.history.HistoryType;
import com.Isabela01vSilva.bank_isabela.mapper.HistoryMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryQueryService {
    private final HistoryRepository historyRepository;
    private final CustomerRepository customerRepository;

    public List<TransactionHistoryResponse> getAccountType(Long customerId, AccountType accountType) {
        return historyRepository.findByCustomerId(customerId).stream()
                .filter(History::hasAccount)
                .filter(h -> h.getAccount().getAccountType().equals(accountType))
                .map(HistoryMapper::toTransactionResponse)
                .toList();
    }

    public List<CustomerHistoryResponse> getCustomerHistoryByTypes(Long customerId,
                                                                   List<HistoryType> types) {
        validateCustomerExists(customerId);

        return historyRepository.findByCustomerIdAndHistoryTypeIn(customerId, types).stream()
                .filter(History::hasAccount)
                .map(HistoryMapper::toCustomerResponse)
                .toList();
    }

    public List<TransactionHistoryResponse> getAccountHistoryByTypes(Long accountId,
                                                                     List<HistoryType> types) {
        return historyRepository.findByAccountIdAndHistoryTypeIn(accountId, types).stream()
                .filter(History::hasAccount)
                .map(HistoryMapper::toTransactionResponse)
                .toList();
    }

    public List<TransactionHistoryResponse> getByDateRange(Long accountId,
                                                           LocalDateTime start,
                                                           LocalDateTime end) {
        return historyRepository
                .findByAccountIdAndTransactionDateBetween(accountId, start, end).stream()
                .map(HistoryMapper::toTransactionResponse)
                .toList();
    }

    public List<TransactionHistoryResponse> getCustomerTransactions(Long customerId,
                                                                    AccountType accountType) {
        return historyRepository.findByCustomerId(customerId).stream()
                .filter(History::hasAccount)
                .filter(h -> accountType == null ||
                        h.getAccount().getAccountType().equals(accountType))
                .map(HistoryMapper::toTransactionResponse)
                .toList();
    }

    private void validateCustomerExists(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new EntityNotFoundException("Cliente não encontrado");
        }
    }
}
