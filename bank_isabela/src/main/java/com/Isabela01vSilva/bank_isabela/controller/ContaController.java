package com.Isabela01vSilva.bank_isabela.controller;

import com.Isabela01vSilva.bank_isabela.domain.conta.Conta;
import com.Isabela01vSilva.bank_isabela.service.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("contas")
public class ContaController {

    @Autowired
    private ContaService contaService;

    @PostMapping
    @Transactional
    public ResponseEntity<Conta> cadastrarConta(@RequestBody Conta dados) {
        Conta novaConta = contaService.cadastrar(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaConta);
    }

    @GetMapping
    public ResponseEntity<List<Conta>> listarContas() {
        List<Conta> listar = contaService.exibirTodasAsContas();
        return ResponseEntity.ok(listar);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Conta> listarContaPorId(@PathVariable Long id) {
        Conta buscarConta = contaService.exibirContaPorId(id);
        return ResponseEntity.ok(buscarConta);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Conta> atualizarConta(@PathVariable Long id, @RequestBody Conta dados) {
        Conta atualizar = contaService.atualizarConta(id, dados);
        return ResponseEntity.ok(atualizar);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Conta> desativarConta(@PathVariable Long id) {
        Conta conta =  contaService.desativarConta(id);
        return ResponseEntity.noContent().build();
    }

}
