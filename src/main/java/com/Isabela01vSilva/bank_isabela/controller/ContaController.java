package com.Isabela01vSilva.bank_isabela.controller;

import com.Isabela01vSilva.bank_isabela.controller.request.AlterarStatusContaRequest;
import com.Isabela01vSilva.bank_isabela.controller.request.DepositoRequest;
import com.Isabela01vSilva.bank_isabela.controller.request.SaqueRequest;
import com.Isabela01vSilva.bank_isabela.controller.request.TransferenciaRequest;
import com.Isabela01vSilva.bank_isabela.domain.conta.Conta;
import com.Isabela01vSilva.bank_isabela.domain.conta.StatusConta;
import com.Isabela01vSilva.bank_isabela.service.ContaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("contas")
public class ContaController {

    @Autowired
    private ContaService contaService;

    @PostMapping
    public ResponseEntity<Conta> cadastrarConta(@Valid @RequestBody Conta dados) {
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
    public ResponseEntity<Conta> atualizarConta(@PathVariable Long id, @RequestBody Conta dados) {
        Conta atualizar = contaService.atualizarConta(id, dados);
        return ResponseEntity.ok(atualizar);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Conta> atualizarSttsConta(@PathVariable Long id, @RequestBody AlterarStatusContaRequest alterarStatus) {
        Conta conta = contaService.atualizarSttsConta(id, alterarStatus);
        return ResponseEntity.ok(conta);
    }

    @PutMapping("/transferir")
    public ResponseEntity<String> realizarTransferencia(@RequestBody TransferenciaRequest transferenciaRequest) {
        String mensagem = contaService.realizarTransferencia(transferenciaRequest);
        return ResponseEntity.ok(mensagem);
    }

    @PutMapping("/depositar")
    public ResponseEntity<String>depositar(@RequestBody DepositoRequest depositoRequest) {
        String mensagem = contaService.depositar(depositoRequest);
        return ResponseEntity.ok(mensagem);
    }

    @PutMapping("/saque")
    public ResponseEntity<String> saque(@RequestBody SaqueRequest saqueRequest) {
        String mensagem = contaService.saque(saqueRequest);
        return ResponseEntity.ok(mensagem);
    }

    @GetMapping("/{id}/saldo")
    public ResponseEntity<String> saldo(@PathVariable Long id) {
        String mensagem = contaService.consultaSaldo(id);
        return ResponseEntity.ok(mensagem);
    }

    @GetMapping("/{id}/stts")
    public ResponseEntity<List<StatusConta>> exibir(@PathVariable Long id){
        List<StatusConta> statusContas = contaService.exibirSttsConta(id);
        return ResponseEntity.ok(statusContas);
    }
}
