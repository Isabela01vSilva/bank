package com.Isabela01vSilva.bank_isabela.service;

import com.Isabela01vSilva.bank_isabela.controller.request.history.RegisterHistoryRequest;
import com.Isabela01vSilva.bank_isabela.controller.response.history.CustomerHistoryResponse;
import com.Isabela01vSilva.bank_isabela.controller.response.history.HistoryResponse;
import com.Isabela01vSilva.bank_isabela.controller.response.history.TransactionHistoryResponse;
import com.Isabela01vSilva.bank_isabela.domain.account.AccountType;
import com.Isabela01vSilva.bank_isabela.domain.customer.CustomerRepository;
import com.Isabela01vSilva.bank_isabela.domain.historico.History;
import com.Isabela01vSilva.bank_isabela.domain.historico.HistoryRepository;
import com.Isabela01vSilva.bank_isabela.domain.historico.HistoryType;
import com.Isabela01vSilva.bank_isabela.mapper.HistoryMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HistoryService {

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public HistoryResponse register(RegisterHistoryRequest request) {

        // Criação de uma nova instância de Historico
        History history = new History();

        // Atribui os dados do historico para seu cadastro
        history.setAccount(request.account());
        history.setCustomer(request.customer());
        history.setHistoryType(request.historyType());
        history.setDescription(request.description());
        history.setAmount(request.amount());
        history.setTransactionDate(LocalDateTime.now());

        History savedHistory = historyRepository.save(history);
        return HistoryMapper.toResponse(savedHistory);
    }

    public List<TransactionHistoryResponse> getAccountHistoryByAccountType(Long customerId,
                                                                           AccountType accountType) {
        List<History> histories = historyRepository.findByCustomerId(customerId);

        return histories.stream()
                .filter(history -> history.getAccount() != null)
                .filter(history -> history.getAccount().getAccountType().equals(accountType))
                .map(HistoryMapper::toTransactionResponse)
                .toList();
    }

    public List<CustomerHistoryResponse> getCustomerHistoryByTransactionType(Long customerId,
                                                                             List<HistoryType> historyTypes) {
        if (!customerRepository.existsById(customerId)) {
            throw new EntityNotFoundException("Cliente não encontrado");
        }

        return historyRepository.findByCustomerIdAndHistoryTypeIn(customerId, historyTypes)
                .stream()
                .filter(history -> history.getAccount() != null)
                .map(HistoryMapper::toCustomerResponse)
                .toList();
    }

    public List<TransactionHistoryResponse> getAccountHistoryByTransactionType(Long id,
                                                                               List<HistoryType> historyTypes) {
        return historyRepository.findByAccountIdAndHistoryTypeIn(id, historyTypes)
                .stream()
                .filter(history -> history.getAccount() != null)
                .map(HistoryMapper::toTransactionResponse).toList();
    }

    public List<TransactionHistoryResponse> getHistoryBetweenDates(Long accountId, LocalDateTime startDate, LocalDateTime endDate) {

        // Busca os históricos de uma conta entre duas datas
        List<History> histories = historyRepository.findByAccountIdAndTransactionDateBetween(accountId, startDate, endDate);
        return histories.stream()
                .map(HistoryMapper::toTransactionResponse).toList();
    }

    public List<TransactionHistoryResponse> getCustomerTransactions(Long customerId,
                                                                    AccountType accountType) {

        List<History> histories = historyRepository.findByCustomerId(customerId);

        return histories.stream()
                .filter(history -> history.getAccount() != null)
                .filter(history -> accountType == null ||
                        history.getAccount()
                                .getAccountType()
                                .equals(accountType))
                .map(HistoryMapper::toTransactionResponse)
                .toList();
    }
}