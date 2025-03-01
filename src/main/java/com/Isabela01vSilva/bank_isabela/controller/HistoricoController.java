package com.Isabela01vSilva.bank_isabela.controller;

import com.Isabela01vSilva.bank_isabela.controller.request.historico.HistoricoEntreDatasResquest;
import com.Isabela01vSilva.bank_isabela.controller.response.historico.HistoricoResponse;
import com.Isabela01vSilva.bank_isabela.controller.response.historico.HistoricoSttsContaResponse;
import com.Isabela01vSilva.bank_isabela.domain.historico.Historico;
import com.Isabela01vSilva.bank_isabela.service.HistoricoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("historicos")
public class HistoricoController {

    @Autowired
    private HistoricoService historicoService;

    @GetMapping
    public ResponseEntity<List<Historico>> listarHistoricos() {
        List<Historico> listar = historicoService.exibirTodosHistoricos();
        return ResponseEntity.ok(listar);
    }

    @GetMapping("/{id}/cliente")
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
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(historico);
    }

    @GetMapping("/gastos")
    public ResponseEntity<List<String>> gastos(@RequestBody HistoricoEntreDatasResquest datas) {
        String totalGastos = historicoService.calculoGastosPorPeriodo(datas);

        if (totalGastos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(Collections.singletonList(totalGastos));
    }

}