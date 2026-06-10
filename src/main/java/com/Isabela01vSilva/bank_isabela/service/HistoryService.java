package com.Isabela01vSilva.bank_isabela.service;

import com.Isabela01vSilva.bank_isabela.controller.request.history.AccountTypeHistoryRequest;
import com.Isabela01vSilva.bank_isabela.controller.request.history.RegisterHistoryRequest;
import com.Isabela01vSilva.bank_isabela.controller.request.history.TipoMovimentacaoRequest;
import com.Isabela01vSilva.bank_isabela.controller.response.history.CustomerHistoryResponse;
import com.Isabela01vSilva.bank_isabela.controller.response.history.TransactionHistoryResponse;
import com.Isabela01vSilva.bank_isabela.domain.account.Account;
import com.Isabela01vSilva.bank_isabela.domain.account.AccountType;
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

    public List<TransactionHistoryResponse> getAccountHistoryByTipodeMovimentacao(TipoMovimentacaoRequest request) {
        return historyRepository.findByCustomerIdAndHistoryType(request.id(), request.historyType())
                .stream()
                .map(history -> new TransactionHistoryResponse(
                        history.getCustomer().getCpf(),
                        history.getAccount().getAccountNumber(),
                        history.getAccount().getAgencyNumber(),
                        history.getAmount(),
                        history.getDescription()
                )).toList();
    }

    /*

    public List<HistoricoResponse> exibirHistoricoPorCliente(Long id) {
        // Busca os históricos relacionados a um cliente pelo id
        List<History> historico = historicoRepository.findByClienteId(id);
        return historico.stream().map(historico1 -> new HistoricoResponse(
                historico1.getId(), historico1.getCliente().getFullName(), historico1.getValor(), historico1.getDescricao(), historico1.getDataTransacao()
        )).toList(); // Converte os históricos para uma lista de HistoricoResponse e retorna
    }

    public List<HistoricoResponse> exibirHistoricoPorConta(Long id) {
        // Busca os históricos relacionados a um conta pelo id
        List<History> historico = historicoRepository.findByContaId(id);
        return historico.stream().map(historico1 -> new HistoricoResponse(
                historico1.getId(), historico1.getCliente().getFullName(), historico1.getValor(), historico1.getDescricao(), historico1.getDataTransacao()
        )).toList(); // Converte os históricos para uma lista de HistoricoResponse e retorna
    }

    public List<HistoricoSttsContaResponse> exibirHistoricoStts(Long id) {

        // Busca os históricos relacionados a uma conta
        List<History> historico = historicoRepository.findByContaId(id);

        return historico.stream()
                .filter(historicos -> historicos.getTipoOperacao().equals(HistoryType.ATUALIZACAO_STTS_CONTA)) // Filtra apenas os históricos com operação de atualização de status de conta
                .map(historicos -> new HistoricoSttsContaResponse(
                        historicos.getId(),
                        historicos.getTipoOperacao(),
                        historicos.getDescricao()
                )).toList(); // Converte os históricos filtrados em HistoricoSttsContaResponse e retorna
    }

    public List<HistoricoResponse> exibirHistoricoEntreDatas(HistoricoEntreDatasResquest datasResquest) {

        // Busca os históricos de uma conta entre duas datas
        List<History> historicos = historicoRepository.findByContaIdAndDataTransacaoBetween(datasResquest.id(), datasResquest.dataInicio(), datasResquest.dataFim());

        return historicos.stream()
                .map(historico -> new HistoricoResponse(
                        historico.getId(),
                        historico.getCliente().getFullName(),
                        historico.getValor(),
                        historico.getDescricao(),
                        historico.getDataTransacao()
                )).toList(); // Converte os históricos encontrados em HistoricoResponse e retorna
    }

    public List<HistoricoResponse> exibirExtrato(HistoricoEntreDatasResquest datasResquest) {

        // Busca históricos entre datas
        List<History> historico = historicoRepository.findByContaIdAndDataTransacaoBetween(datasResquest.id(), datasResquest.dataInicio(), datasResquest.dataFim());

        return historico.stream()
                .filter(historicos -> !historicos.getTipoOperacao().equals(HistoryType.ATUALIZACAO_STTS_CONTA)) // Filtra para excluir operações de atualização de status da conta
                .map(historicos -> new HistoricoResponse(
                        historicos.getId(),
                        historicos.getCliente().getFullName(),
                        historicos.getValor(),
                        historicos.getDescricao(),
                        historicos.getDataTransacao()
                )).toList(); // Converte e retorna os históricos filtrados
    }


    public String calculoGastosPorPeriodo(HistoricoEntreDatasResquest gastosRequest) {
        List<History> historico = historicoRepository.findByContaIdAndDataTransacaoBetween(
                gastosRequest.id(),
                gastosRequest.dataInicio(),
                gastosRequest.dataFim()
        ); // Busca os históricos da conta no intervalo de datas especificado

        List<Double> numeros = historico.stream()
                .filter(h -> h.getTipoOperacao().equals(HistoryType.SAQUE) || h.getTipoOperacao().equals(HistoryType.TRANSFERENCIA)) // Filtra apenas saques ou operações de PIX
                .map(History::getValor) // Mapeia os valores das operações
                .toList(); // Converte para uma lista de valores

        Double calculado = numeros.stream().reduce(0.0, Double::sum); // Soma todos os valores dos saques e pix

        return String.format("Total de gastos: R$ %.2f da conta de id: %d no período de %s até %s",
                calculado,
                gastosRequest.id(),
                gastosRequest.dataInicio(),
                gastosRequest.dataFim()); // Retorna uma string formatada com o total dos gastos
    }*/
}