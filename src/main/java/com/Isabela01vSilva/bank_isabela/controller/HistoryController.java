package com.Isabela01vSilva.bank_isabela.controller;

import com.Isabela01vSilva.bank_isabela.controller.request.history.AccountTypeHistoryRequest;
import com.Isabela01vSilva.bank_isabela.controller.response.history.CustomerHistoryResponse;
import com.Isabela01vSilva.bank_isabela.controller.response.history.TransactionHistoryResponse;
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

    /*@GetMapping
    public ResponseEntity<List<HistoricoResponse>> listarHistoricos() {
        List<Historico> listar = historicoService.exibirTodosHistoricos();
        return ResponseEntity.ok(listar.stream().map(historico -> new HistoricoResponse(historico.getId(),
                historico.getCliente().getFullName(), historico.getValor(), historico.getDescricao(), historico.getDataTransacao())).toList());
    }

    /*@GetMapping("/{id}/cliente")
    public ResponseEntity<List<HistoricoResponse>> exibirHistoricoPorCliente(@PathVariable Long id) {
        List<HistoricoResponse> historico = historicoService.exibirHistoricoPorCliente(id);
        return ResponseEntity.ok(historico);
    }

    @GetMapping("/{id}/conta")
    public ResponseEntity<List<HistoricoResponse>> exibirHistoricoPorConta(@PathVariable Long id) {
        List<HistoricoResponse> historico = historicoService.exibirHistoricoPorConta(id);
        return ResponseEntity.ok(historico);
    }

    @GetMapping("/{id}/stts")
    public ResponseEntity<List<HistoricoSttsContaResponse>> exibirHistoricoSttsConta(@PathVariable Long id) {
        List<HistoricoSttsContaResponse> historico = historicoService.exibirHistoricoStts(id);
        return ResponseEntity.ok(historico);
    }

    @GetMapping("/entreDatas")
    public ResponseEntity<List<HistoricoResponse>> exibirHistoricoEntreDatas(@RequestBody HistoricoEntreDatasResquest datas) {
        List<HistoricoResponse> historico = historicoService.exibirHistoricoEntreDatas(datas);

        if (historico.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(historico);
    }

    @GetMapping("/extrato")
    public ResponseEntity<List<HistoricoResponse>> exibirExtrato(@RequestBody HistoricoEntreDatasResquest datas) {
        List<HistoricoResponse> historico = historicoService.exibirExtrato(datas);

        if (historico.isEmpty()) {


    @GetMapping("/gastos")
    public ResponseEntity<String> gastos(@RequestBody HistoricoEntreDatasResquest datas) {
        String totalGastos = historicoService.calculoGastosPorPeriodo(datas);

        if (totalGastos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(totalGastos);
    }*/

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

}