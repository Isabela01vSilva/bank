package com.Isabela01vSilva.bank_isabela.controller;

import com.Isabela01vSilva.bank_isabela.domain.historico.Historico;
import com.Isabela01vSilva.bank_isabela.service.HistoricoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("historicos")
public class HistoricoController {

    @Autowired
    private HistoricoService historicoService;

    @PostMapping
    @Transactional
    public ResponseEntity<Historico> cadastrarHistorico(@RequestBody Historico dados){
        Historico historico = historicoService.cadastrar(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(historico);
    }

    @GetMapping
    public ResponseEntity<List<Historico>> listarHistoricos(){
        List<Historico> listar =  historicoService.exibirTodosHistoricos();
        return ResponseEntity.ok(listar);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Historico> listarHistoricoPorId(@PathVariable Long id){
        Historico buscarHistorico = historicoService.exibirHistoricoPorId(id);
        return ResponseEntity.ok(buscarHistorico);
    }
}
