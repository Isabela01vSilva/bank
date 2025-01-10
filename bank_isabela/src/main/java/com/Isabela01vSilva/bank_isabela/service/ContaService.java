package com.Isabela01vSilva.bank_isabela.service;

import com.Isabela01vSilva.bank_isabela.controller.request.AlterarStatusContaRequest;
import com.Isabela01vSilva.bank_isabela.controller.request.DepositoRequest;
import com.Isabela01vSilva.bank_isabela.controller.request.SaqueRequest;
import com.Isabela01vSilva.bank_isabela.controller.request.TransferenciaRequest;
import com.Isabela01vSilva.bank_isabela.domain.cliente.Cliente;
import com.Isabela01vSilva.bank_isabela.domain.cliente.ClienteRepository;
import com.Isabela01vSilva.bank_isabela.domain.conta.Conta;
import com.Isabela01vSilva.bank_isabela.domain.conta.ContaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    //CRUD
    public Conta cadastrar(Conta dados) {
        Cliente cliente = clienteRepository.findById(dados.getCliente().getId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        dados.setCliente(cliente);
        return contaRepository.save(dados);
    }

    public List<Conta> exibirTodasAsContas() {
        return contaRepository.findAll();
    }

    public Conta exibirContaPorId(Long id) {
        return contaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));
    }

    public Conta atualizarConta(Long id, Conta dados) {
        Conta conta = contaRepository.getReferenceById(id);
        conta.statusDaConta();
        conta.atualizarInformacoes(dados);
        return contaRepository.save(conta);
    }

    public Conta atualizarSttsConta(AlterarStatusContaRequest alterarStatus) {
        Conta conta = contaRepository.getReferenceById(alterarStatus.id());
        conta.atualizarStatusConta(alterarStatus.statusConta());
        return contaRepository.save(conta);
    }

    //
    public String realizarTransferencia(TransferenciaRequest transferenciaRequest) {
        Conta contaOrigem = contaRepository.findByNumero(transferenciaRequest.numeroContaOrigem())
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));
        Conta contaDestino = contaRepository.findByNumero(transferenciaRequest.numeroContaDestino())
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));

        contaOrigem.statusDaConta();
        contaDestino.statusDaConta();

        contaOrigem.sacar(transferenciaRequest.valor());
        contaDestino.depositar(transferenciaRequest.valor());

        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);

        return "Transferência realizada com sucesso";
    }

    public String depositar(DepositoRequest deposito) {
        Conta conta = contaRepository.findById(deposito.id())
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));

        conta.statusDaConta();
        conta.depositar(deposito.valor());

        return "Valor depositado: R$" + deposito.valor();
    }

    public String saque(SaqueRequest saque) {
        Conta conta = contaRepository.findById(saque.id())
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));

        conta.statusDaConta();
        conta.sacar(saque.valor());

        return "Valor sacado: R$" + saque.valor();
    }
}
