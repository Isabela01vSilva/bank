package com.Isabela01vSilva.bank_isabela.service;

import com.Isabela01vSilva.bank_isabela.controller.request.conta.*;
import com.Isabela01vSilva.bank_isabela.controller.request.historico.CadastroHistoricoRequest;
import com.Isabela01vSilva.bank_isabela.domain.conta.Conta;
import com.Isabela01vSilva.bank_isabela.domain.conta.ContaRepository;
import com.Isabela01vSilva.bank_isabela.domain.conta.StatusConta;
import com.Isabela01vSilva.bank_isabela.domain.historico.TipoOperacao;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private HistoricoService historicoService;

    //CRUD
    @Transactional
    public Conta cadastrar(CriarContaDTO dados) {
        Conta novaConta = new Conta();
        novaConta.setStatusConta(StatusConta.ATIVADA);
        novaConta.setDataCriacao(LocalDate.now());
        novaConta.setNumero(novaConta.gerarNumeroConta());
        novaConta.setTipoConta(dados.tipoConta());
        novaConta.setCliente(dados.cliente());

        String numeroConta;
        do {
            numeroConta = novaConta.gerarNumeroConta();
        } while (contaRepository.existsByNumero(numeroConta));

        novaConta.setNumeroAgencia(dados.numeroAgencia());
        novaConta.setSaldo(0.00);

        return contaRepository.save(novaConta);
    }

    public List<Conta> exibirTodasAsContas() {
        return contaRepository.findAll();

    }

    public Conta exibirContaPorId(Long id) {
        return contaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));
    }

    @Transactional
    public Conta atualizarSttsConta(Long id, AlterarStatusContaRequest alterarStatus) {

        //Busca contas
        Conta conta = contaRepository.getReferenceById(id);

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
}
