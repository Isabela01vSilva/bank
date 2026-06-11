package com.Isabela01vSilva.bank_isabela.service;

import com.Isabela01vSilva.bank_isabela.controller.request.history.AccountTypeHistoryRequest;
import com.Isabela01vSilva.bank_isabela.controller.request.history.HistoricoEntreDatasResquest;
import com.Isabela01vSilva.bank_isabela.controller.request.history.RegisterHistoryRequest;
import com.Isabela01vSilva.bank_isabela.controller.response.history.CustomerHistoryResponse;
import com.Isabela01vSilva.bank_isabela.controller.response.history.TransactionHistoryResponse;
import com.Isabela01vSilva.bank_isabela.domain.historico.History;
import com.Isabela01vSilva.bank_isabela.domain.historico.HistoryRepository;
import com.Isabela01vSilva.bank_isabela.domain.historico.HistoryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class HistoryService {

    @Autowired
    private HistoryRepository historyRepository;

    public History register(RegisterHistoryRequest request) {

        // Criação de uma nova instância de Historico
        History history = new History();

        // Atribui os dados do historico para seu cadastro
        history.setAccount(request.account());
        history.setCustomer(request.customer());
        history.setHistoryType(request.historyType());
        history.setDescription(request.description());
        history.setAmount(request.amount());
        history.setTransactionDate(LocalDateTime.now());

        // Salva o histórico no banco de dados e retorna a entidade salva
        return historyRepository.save(history);
    }

    public List<CustomerHistoryResponse> getCustomerHistory(Long customerId) {
        List<History> histories = historyRepository.findByCustomerId(customerId);

        return histories.stream()
                .filter(history -> CUSTOMER_HISTORY_TYPES.contains(history.getHistoryType()))
                .map(history -> new CustomerHistoryResponse(
                        history.getId(),
                        history.getHistoryType(),
                        history.getDescription()
                ))
                .toList();
    }

    private static final Set<HistoryType> CUSTOMER_HISTORY_TYPES = Set.of(
            HistoryType.ACCOUNT_CREATED,
            HistoryType.ACCOUNT_REACTIVATED,
            HistoryType.ACCOUNT_CLOSED,
            HistoryType.CUSTOMER_UPDATED,
            HistoryType.CUSTOMER_REACTIVATED,
            HistoryType.CUSTOMER_INACTIVATED
    );

    public List<TransactionHistoryResponse> getAccountHistoryByAccountType(AccountTypeHistoryRequest request) {
        List<History> histories = historyRepository.findByCustomerId(request.id());

        return histories.stream()
                .filter(history -> history.getAccount().getAccountType().equals(request.accountType()))
                .map(history -> new TransactionHistoryResponse(
                        history.getCustomer().getCpf(),
                        history.getAccount().getAccountNumber(),
                        history.getAccount().getAgencyNumber(),
                        history.getAmount(),
                        history.getDescription()
                ))
                .toList();
    }

    public List<TransactionHistoryResponse> getAccountHistoryByTipodeMovimentacao(Long id,
                                                                                  List<HistoryType> historyTypes) {
        return historyRepository.findByCustomerIdAndHistoryTypeIn(id, historyTypes)
                .stream()
                .map(history -> new TransactionHistoryResponse(
                        history.getCustomer().getCpf(),
                        history.getAccount().getAccountNumber(),
                        history.getAccount().getAgencyNumber(),
                        history.getAmount(),
                        history.getDescription()
                )).toList();
    }

    public List<TransactionHistoryResponse> exibirHistoricoPorCliente(Long id) {
        // Busca os históricos relacionados a um cliente pelo id
        List<History> historico = historyRepository.findByCustomerId(id);

        return historico
                .stream()
                .map(history -> new TransactionHistoryResponse(
                        history.getCustomer().getCpf(),
                        history.getAccount().getAccountNumber(),
                        history.getAccount().getAgencyNumber(),
                        history.getAmount(),
                        history.getDescription()
        )).toList();
    }

    public List<TransactionHistoryResponse> exibirHistoricoEntreDatas(HistoricoEntreDatasResquest datasResquest) {

        // Busca os históricos de uma conta entre duas datas
        List<History> historicos = historyRepository.findByAccountIdAndTransactionDateBetween(datasResquest.id(), datasResquest.dataInicio(), datasResquest.dataFim());

        return historicos.stream()
                .map(history -> new TransactionHistoryResponse(
                        history.getCustomer().getCpf(),
                        history.getAccount().getAccountNumber(),
                        history.getAccount().getAgencyNumber(),
                        history.getAmount(),
                        history.getDescription()
                )).toList();
    }
}