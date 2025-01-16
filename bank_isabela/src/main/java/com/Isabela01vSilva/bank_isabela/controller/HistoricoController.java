package com.Isabela01vSilva.bank_isabela.controller;

import com.Isabela01vSilva.bank_isabela.controller.response.HistoricoResponse;
import com.Isabela01vSilva.bank_isabela.domain.historico.Historico;
import com.Isabela01vSilva.bank_isabela.service.HistoricoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("historicos")
public class HistoricoController {

    @Autowired
    private HistoricoService historicoService;

    @GetMapping
    public ResponseEntity<List<Historico>> listarHistoricos(){
        List<Historico> listar =  historicoService.exibirTodosHistoricos();
        return ResponseEntity.ok(listar);
    }

    @GetMapping("/{id}/cliente")
    public ResponseEntity<List<HistoricoResponse>> exibirHistoricoPorCliente(@PathVariable Long id){
        List<HistoricoResponse> historico = historicoService.exibirHistoricoPorCliente(id);
        return ResponseEntity.ok(historico);
    }

    @GetMapping("/{id}/conta")
    public ResponseEntity<List<HistoricoResponse>> exibirHistoricoPorConta(@PathVariable Long id){
        List<HistoricoResponse> historico = historicoService.exibirHistoricoPorConta(id);
        return ResponseEntity.ok(historico);
    }

}