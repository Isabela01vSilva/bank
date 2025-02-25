package com.Isabela01vSilva.bank_isabela.service;

import com.Isabela01vSilva.bank_isabela.controller.request.*;
import com.Isabela01vSilva.bank_isabela.domain.cliente.Cliente;
import com.Isabela01vSilva.bank_isabela.domain.cliente.ClienteRepository;
import com.Isabela01vSilva.bank_isabela.domain.conta.Conta;
import com.Isabela01vSilva.bank_isabela.domain.conta.ContaRepository;
import com.Isabela01vSilva.bank_isabela.domain.conta.StatusConta;
import com.Isabela01vSilva.bank_isabela.domain.historico.TipoOperacao;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private HistoricoService historicoService;

    //CRUD
    @Transactional
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

    @Transactional
    public Conta atualizarConta(Long id, Conta dados) {
        Conta conta = contaRepository.getReferenceById(id);
        conta.statusDaConta();
        conta.atualizarInformacoes(dados);
        return contaRepository.save(conta);
    }

    @Transactional
    public Conta atualizarSttsConta(Long id, AlterarStatusContaRequest alterarStatus) {

        //Busca contas
        Conta conta = contaRepository.getReferenceById(alterarStatus.id());

        //Atualiza o stts da conta
        conta.atualizarStatusConta(alterarStatus.statusConta());

        //Registra a atualização
        historicoService.cadastrar(new CadastroHistoricoRequest(
                conta,
                conta.getCliente(),
                TipoOperacao.ATUALIZACAO_STTS_CONTA,
                "Atualizando stts conta para " + alterarStatus.statusConta(),
                null
        ));

        //Salva a conta após alteração e retorna a conta atualizada
        return contaRepository.save(conta);
    }

    //
    @Transactional
    public String realizarTransferencia(TransferenciaRequest transferenciaRequest) {

        //Busca a conta de origem
        Conta contaOrigem = contaRepository.findByNumero(transferenciaRequest.numeroContaOrigem())
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));

        //Busca a conta de destino
        Conta contaDestino = contaRepository.findByNumero(transferenciaRequest.numeroContaDestino())
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));

        //Verifica o stts de ambas as contas antes de realizar qualquer operação
        contaOrigem.statusDaConta();
        contaDestino.statusDaConta();

        //Realiza o saque na conta de origem e o deposito na conta de destino
        contaOrigem.sacar(transferenciaRequest.valor());
        contaDestino.depositar(transferenciaRequest.valor());

        //Salva as mudanças nas duas contas
        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);

        //Registra o histórico da transferência para a conta de origem.
        historicoService.cadastrar(
                new CadastroHistoricoRequest(
                        contaOrigem,
                        contaOrigem.getCliente(),
                        TipoOperacao.PIX,
                        "Pix enviado para a conta: " + transferenciaRequest.numeroContaDestino(),
                        transferenciaRequest.valor()
                )
        );

        //Registra o histórico da transferência para a conta de destino.
        historicoService.cadastrar(
                new CadastroHistoricoRequest(
                        contaDestino,
                        contaDestino.getCliente(),
                        TipoOperacao.PIX,
                        "Pix recebido da conta: " + transferenciaRequest.numeroContaOrigem(),
                        transferenciaRequest.valor()
                )
        );

        //Retorna uma mensagem indicando que a transferência foi realizada com sucesso.
        return "Transferência realizada com sucesso";
    }

    @Transactional
    public String depositar(DepositoRequest deposito) {

        // Busca a conta com o ID fornecido na request
        Conta conta = contaRepository.findById(deposito.id())
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));

        // Verifica o stts da conta antes de realizar qualquer operação
        conta.statusDaConta();

        // Realiza deposito na conta
        conta.depositar(deposito.valor());

        // Registra o histórico da operação de depósito.
        historicoService.cadastrar(
                new CadastroHistoricoRequest(
                        conta,
                        conta.getCliente(),
                        TipoOperacao.DEPOSITO,
                        "Operação de deposito realizada",
                        deposito.valor()
                )
        );

        // Retorna uma mensagem indicando o valor depositado.
        return "Valor depositado: R$" + deposito.valor();
    }

    @Transactional
    public String saque(SaqueRequest saque) {

        // Busca a conta com o ID fornecido na request
        Conta conta = contaRepository.findById(saque.id())
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));

        // Verifica o stts da conta antes de realizar qualquer operação
        conta.statusDaConta();

        // Realiza saque da conta
        conta.sacar(saque.valor());

        // Registra o histórico da operação de depósito.
        historicoService.cadastrar(
                new CadastroHistoricoRequest(
                        conta,
                        conta.getCliente(),
                        TipoOperacao.SAQUE,
                        "Operação de saque realizada",
                        saque.valor()
                )
        );

        // Retorna uma mensagem indicando o valor depositado.
        return "Valor sacado: R$" + saque.valor();
    }

    public String consultaSaldo(Long id) {

        // Busca a conta com o ID fornecido.
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));

        // Retorna o saldo da conta e o número da conta.
        return "Saldo R$" + conta.getSaldo() + " da conta:" + conta.getNumero();
    }

    public List<StatusConta> exibirSttsConta(Long id) {

        // Busca a conta com o ID fornecido.
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));

        // Retorna o status da conta.
        return List.of(conta.getStatusConta());
    }
}
