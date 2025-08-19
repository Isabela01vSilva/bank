package com.Isabela01vSilva.bank_isabela.service;

import com.Isabela01vSilva.bank_isabela.controller.request.conta.TransferenciaRequest;
import com.Isabela01vSilva.bank_isabela.domain.Transferencia;
import com.Isabela01vSilva.bank_isabela.domain.TransferenciaRepository;
import com.Isabela01vSilva.bank_isabela.domain.conta.Conta;
import com.Isabela01vSilva.bank_isabela.domain.conta.ContaRepository;
import com.Isabela01vSilva.bank_isabela.service.client.ScheduleClientService;
import com.Isabela01vSilva.bank_isabela.service.client.dto.*;
import com.Isabela01vSilva.bank_isabela.service.data.request.CreateAppointmentScheduleRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransferenciaService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private HistoricoService historicoService;

    @Autowired
    private ScheduleClientService scheduleClientService;

    @Autowired
    private TransferenciaRepository transferenciaRepository;

    //Fluxo Principal de agendamento
    @Transactional
    public void agendarTransferencia(TransferenciaRequest request){
        Conta contaOrigem = buscarContas(request.numeroContaOrigem());
        Conta contaDestino = buscarContas(request.numeroContaDestino());

        validarContaAtiva(contaOrigem);
        validarContaAtiva(contaDestino);

        // Cria objeto de Transferência no banco
        Transferencia transferencia = new Transferencia();
        transferencia.setDataExecucao(request.executionDate());
        transferencia.setValor(request.valor());
        transferencia.setContaOrigem(contaOrigem);
        transferencia.setContaDestino(contaDestino);
        transferencia.setStatus(Status.AGENDADO);

        // Salva a transferência
        transferenciaRepository.save(transferencia);

        // Cria agendamento no Schendulo
        CreateAppointmentScheduleRequest criarAgendamento = new CreateAppointmentScheduleRequest(
                request.executionDate(),
                new PayloadDTO(request.valor(), request.numeroContaOrigem(), request.numeroContaDestino()),
                "BANK",
                Status.AGENDADO
        );

        // Cria o agendamento no Schendulo
        SchedulingDTO dtoAgendamento = scheduleClientService.createAppointment(criarAgendamento);

        // Atualiza a transferência com o ID do agendamento, se disponível
        if(dtoAgendamento != null){
            transferencia.setAgendamentoId(dtoAgendamento.getId());
            transferenciaRepository.save(transferencia);
        }

        // registrarHistorico
    }

    //Agendamento
    private SchedulingDTO  criarAgendamento(CreateAppointmentScheduleRequest request,
                                            Conta contaOrigem,
                                            Conta contaDestino){
        try {
            SchedulingDTO dtoAgendamento = scheduleClientService.createAppointment(request);

            Double valor = dtoAgendamento.getPayload().getValor();

            boolean temSaldo = validarSaldoSuficiente(contaOrigem, valor, request);

            if(temSaldo){
                UpdateAppointmentDTO updateAppointmentDTO = new UpdateAppointmentDTO(
                        dtoAgendamento.getExecutionDate(),
                        dtoAgendamento.getPayload(),
                        Status.CONCLUIDO
                );
                transferir(contaOrigem, contaDestino, valor);
            }
            return dtoAgendamento;

        } catch (Exception e){
            System.err.println("Error ao criar agendamento: " + e.getMessage());
            return null;
        }
    }

    //Validações
    private Conta buscarContas(String numeroConta){
        return contaRepository.findByNumero(numeroConta)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));
    }

    private void validarContaAtiva(Conta conta){
        if (!"ATIVADA".equalsIgnoreCase(conta.getStatusConta() != null ? conta.getStatusConta().toString() : "")) {
            throw new IllegalArgumentException(
                    String.format("Conta %s não está ativada", conta.getNumero())
            );
        }
    }

    private boolean validarSaldoSuficiente(Conta conta, Double valor, CreateAppointmentScheduleRequest create){
        if(valor.compareTo(conta.getSaldo()) > 0){
            falhaTransferencia(create);
            return false;
        }
        return true;
    }

    private void falhaTransferencia(CreateAppointmentScheduleRequest create) {

        UpdateAppointmentDTO atualizado = new UpdateAppointmentDTO(
                create.executionDate(),
                create.payload(),
                Status.FALHOU
        );

        scheduleClientService.updateAppointment(atualizado);
    }


    //Transferências
    @Transactional
    public boolean transferir(Conta contaOrigem, Conta contaDestino, Double valor){
        try {
            executarOperacoesFinanceiras(contaOrigem, contaDestino, valor);
            return true; // sucesso
        } catch (Exception e) {
            System.err.println("Falha na transferência: " + e.getMessage());
            return false; // falha
        }
    }

    private void executarOperacoesFinanceiras(Conta contaOrigem, Conta contaDestino, Double valor){
        if (contaOrigem.getSaldo() < valor) {
            throw new RuntimeException("Saldo insuficiente");
        }

        contaOrigem.sacar(valor);
        contaDestino.depositar(valor);

        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);
    }
/*
    //Histórico
    private void registrarHistorico(Conta contaOrigem, Conta contaDestino, SchedulingDTO transferencia){
        historicoService.cadastrar(
                new CadastroHistoricoRequest(
                        contaOrigem,
                        contaOrigem.getCliente(),
                        TipoOperacao.TRANSFERENCIA,
                        "TRANSFERENCIA enviado para a conta: " + transferencia.getPayload().numeroContaDestino(),
                        transferencia.getPayload().valor()
                )
        );

       historicoService.cadastrar(
                new CadastroHistoricoRequest(
                        contaDestino,
                        contaDestino.getCliente(),
                        TipoOperacao.TRANSFERENCIA,
                        "TRANSFERENCIA recebido da conta: " + transferencia.getPayload().numeroContaOrigem(),
                        transferencia.getPayload().valor()
                )
        );
    }*/
}