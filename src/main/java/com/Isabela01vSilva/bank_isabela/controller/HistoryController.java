package com.Isabela01vSilva.bank_isabela.controller;

import com.Isabela01vSilva.bank_isabela.controller.request.history.AccountTypeHistoryRequest;
import com.Isabela01vSilva.bank_isabela.controller.request.history.HistoricoEntreDatasResquest;
import com.Isabela01vSilva.bank_isabela.controller.request.history.TipoMovimentacaoRequest;
import com.Isabela01vSilva.bank_isabela.controller.response.history.CustomerHistoryResponse;
import com.Isabela01vSilva.bank_isabela.controller.response.history.TransactionHistoryResponse;
import com.Isabela01vSilva.bank_isabela.domain.historico.HistoryType;
import com.Isabela01vSilva.bank_isabela.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("historicos")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    @GetMapping("/cliente/{customerId}")
    public ResponseEntity<List<CustomerHistoryResponse>> getCustomerHistory(@PathVariable Long customerId) {
        List<CustomerHistoryResponse> history = historyService.getCustomerHistory(customerId);

        if (history.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(history);
    }

    @GetMapping("/tipoDeConta")
    public ResponseEntity<List<TransactionHistoryResponse>> getAccountTypeHistory(@RequestBody AccountTypeHistoryRequest request) {
        List<TransactionHistoryResponse> history = historyService.getAccountHistoryByAccountType(request);

        if (history.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(history);
    }

    @GetMapping("/conta/{id}")
    public ResponseEntity<List<TransactionHistoryResponse>> getMovimentacao(@PathVariable Long id,
                                                                            @RequestParam List<HistoryType> historyTypes) {
        List<TransactionHistoryResponse> history = historyService.getAccountHistoryByTipodeMovimentacao(id ,historyTypes);

        if (history.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(history);
    }

    @GetMapping("/{id}/cliente")
    public ResponseEntity<List<TransactionHistoryResponse>> getTodoOHistoricoPorCliente(@PathVariable Long id) {
        List<TransactionHistoryResponse> history = historyService.exibirHistoricoPorCliente(id);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/entreDatas")
    public ResponseEntity<List<TransactionHistoryResponse>> exibirHistoricoEntreDatas(@RequestBody HistoricoEntreDatasResquest datas) {
        List<TransactionHistoryResponse> historico = historyService.exibirHistoricoEntreDatas(datas);

        if (historico.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(historico);
    }
}