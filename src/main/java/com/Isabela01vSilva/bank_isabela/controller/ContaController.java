package com.Isabela01vSilva.bank_isabela.controller;

import com.Isabela01vSilva.bank_isabela.controller.request.conta.*;
import com.Isabela01vSilva.bank_isabela.controller.response.conta.ContaResponse;
import com.Isabela01vSilva.bank_isabela.controller.response.conta.NovaContaResponse;
import com.Isabela01vSilva.bank_isabela.controller.response.conta.MensagemResponse;
import com.Isabela01vSilva.bank_isabela.domain.conta.Conta;
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
    public ResponseEntity<NovaContaResponse> cadastrarConta(@Valid @RequestBody CriarContaRequest dados) {
        Conta novaConta = contaService.cadastrar(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(new NovaContaResponse(novaConta.getNumero(), novaConta.getTipoConta(), novaConta.getCliente().getId()));
    }

    @GetMapping
    public ResponseEntity<List<ContaResponse>> listarContas() {
        List<Conta> listar = contaService.exibirTodasAsContas();
        return ResponseEntity.ok(listar.stream().map(conta -> new ContaResponse(conta.getNumero(), conta.getTipoConta(), conta.getStatusConta(), conta.getCliente().getId(), conta.getSaldo(), conta.getDataCriacao())).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContaResponse> buscarContaPorId(@PathVariable Long id) {
        Conta buscarConta = contaService.exibirContaPorId(id);
        return ResponseEntity.ok(new ContaResponse(buscarConta.getNumero(), buscarConta.getTipoConta(), buscarConta.getStatusConta(), buscarConta.getCliente().getId(), buscarConta.getSaldo(), buscarConta.getDataCriacao()));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ContaResponse> atualizarSttsConta(@PathVariable Long id, @RequestBody AlterarStatusContaRequest alterarStatus) {
        Conta conta = contaService.atualizarSttsConta(id, alterarStatus);
        return ResponseEntity.ok(new ContaResponse(conta.getNumero(), conta.getTipoConta(), conta.getStatusConta(), conta.getCliente().getId(), conta.getSaldo(), conta.getDataCriacao()));
    }

    @PostMapping("/transferir")
    public ResponseEntity<MensagemResponse> realizarTransferencia(@RequestBody TransferenciaRequest transferenciaRequest) {
        contaService.realizarTransferencia(transferenciaRequest);
        return ResponseEntity.ok(new MensagemResponse("Transferencia realizada com sucesso!"));
    }

    @PostMapping("/depositar")
    public ResponseEntity<MensagemResponse> depositar(@RequestBody DepositoRequest depositoRequest) {
        String mensagem = contaService.depositar(depositoRequest);
        return ResponseEntity.ok(new MensagemResponse(mensagem));
    }

    @PostMapping("/saque")
    public ResponseEntity<MensagemResponse> saque(@RequestBody SaqueRequest saqueRequest) {
        String mensagem = contaService.saque(saqueRequest);
        return ResponseEntity.ok(new MensagemResponse(mensagem));
    }

    @GetMapping("/{id}/saldo")
    public ResponseEntity<MensagemResponse> saldo(@PathVariable Long id) {
        String mensagem = contaService.consultaSaldo(id);
        return ResponseEntity.ok(new MensagemResponse(mensagem));
    }

}
