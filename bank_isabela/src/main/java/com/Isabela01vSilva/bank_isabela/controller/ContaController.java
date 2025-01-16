package com.Isabela01vSilva.bank_isabela.controller;

import com.Isabela01vSilva.bank_isabela.controller.request.AlterarStatusContaRequest;
import com.Isabela01vSilva.bank_isabela.controller.request.DepositoRequest;
import com.Isabela01vSilva.bank_isabela.controller.request.SaqueRequest;
import com.Isabela01vSilva.bank_isabela.controller.request.TransferenciaRequest;
import com.Isabela01vSilva.bank_isabela.domain.conta.Conta;
import com.Isabela01vSilva.bank_isabela.domain.conta.StatusConta;
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

    @PutMapping("/status")
    @Transactional
    public ResponseEntity<Conta> atualizarSttsConta(@RequestBody AlterarStatusContaRequest alterarStatus) {
        Conta conta = contaService.atualizarSttsConta(alterarStatus);
        return ResponseEntity.ok(conta);
    }

    //
    @PutMapping("/transferir")
    @Transactional
    public String realizarTransferencia(@RequestBody TransferenciaRequest transferenciaRequest) {
        String mensagem = contaService.realizarTransferencia(transferenciaRequest);
        return mensagem;
    }

    @PutMapping("/depositar")
    @Transactional
    public String depositar(@RequestBody DepositoRequest depositoRequest) {
        String mensagem = contaService.depositar(depositoRequest);
        return mensagem;
    }

    @PutMapping("/saque")
    @Transactional
    public String saque(@RequestBody SaqueRequest saqueRequest) {
        String mensagem = contaService.saque(saqueRequest);
        return mensagem;
    }

    @GetMapping("/{id}/saldo")
    public String saldo(@PathVariable Long id) {
        String mensagem = contaService.consultaSaldo(id);
        return mensagem;
    }

    @GetMapping("/{id}/stts")
    public List<StatusConta> exibir(@PathVariable Long id){
        return contaService.exibirSttsConta(id);
    }
}
