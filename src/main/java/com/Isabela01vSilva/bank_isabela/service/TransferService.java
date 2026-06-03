package com.Isabela01vSilva.bank_isabela.service;

import com.Isabela01vSilva.bank_isabela.controller.request.transfer.TransferRequest;
import com.Isabela01vSilva.bank_isabela.controller.request.transfer.TransferenciaRequest;
import com.Isabela01vSilva.bank_isabela.controller.request.historico.CadastroHistoricoRequest;
import com.Isabela01vSilva.bank_isabela.domain.historico.OperationType;
import com.Isabela01vSilva.bank_isabela.domain.transfer.Transferencia;
import com.Isabela01vSilva.bank_isabela.domain.transfer.TransferenciaRepository;
import com.Isabela01vSilva.bank_isabela.domain.account.Account;
import com.Isabela01vSilva.bank_isabela.domain.account.AccountRepository;
import com.Isabela01vSilva.bank_isabela.service.client.ScheduleClientService;
import com.Isabela01vSilva.bank_isabela.service.client.dto.*;
import com.Isabela01vSilva.bank_isabela.service.data.request.CreateAppointmentScheduleRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TransferService {

    @Autowired
    private AccountRepository contaRepository;

    @Autowired
    private HistoricoService historicoService;

    @Autowired
    private ScheduleClientService scheduleClientService;

    @Autowired
    private TransferenciaRepository transferenciaRepository;

    //Fluxo Principal de agendamento
    @Transactional
    public void agendarTransferencia(TransferenciaRequest request){
        Account contaOrigem = buscarContas(request.numeroContaOrigem());
        Account contaDestino = buscarContas(request.numeroContaDestino());

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
                                            Account contaOrigem,
                                            Account contaDestino){
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
                executarOperacoesFinanceiras(contaOrigem, contaDestino, valor);
            }
            return dtoAgendamento;

        } catch (Exception e){
            System.err.println("Error ao criar agendamento: " + e.getMessage());
            return null;
        }
    }

    //Validações
    private Account buscarContas(String numeroConta){
        return contaRepository.findByAccountNumber(numeroConta)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));
    }

    private void validarContaAtiva(Account conta){
        if (!"ATIVADA".equalsIgnoreCase(conta.getAccountStatus() != null ? conta.getAccountStatus().toString() : "")) {
            throw new IllegalArgumentException(
                    String.format("Conta %s não está ativada", conta.getAccountNumber())
            );
        }
    }

    private boolean validarSaldoSuficiente(Account conta, Double valor, CreateAppointmentScheduleRequest create){
        if(valor.compareTo(conta.getBalance()) > 0){
            falhaTransferencia(create);
            return false;
        }
        return true;
    }

    private void falhaTransferencia(CreateAppointmentScheduleRequest create) {

        UpdateAppointmentDTO atualizado = new UpdateAppointmentDTO(
                create.executionDate(),
                create.payload(),
                Status.ERRO
        );

        scheduleClientService.updateAppointment(atualizado);
    }


    public boolean transferir(TransferRequest request) {
        try {
            Account contaOrigem = contaRepository.findByAccountNumber(request.numeroContaOrigem())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta origem não encontrada"));

            Account contaDestino = contaRepository.findByAccountNumber(request.numeroContaDestino())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta destino não encontrada"));

            executarOperacoesFinanceiras(contaOrigem, contaDestino, request.valor());
            return true; // sucesso
        } catch (Exception e) {
            System.err.println("Falha na transferência: " + e.getMessage());
            return false; // falha
        }
    }


    @Transactional
    public void executarOperacoesFinanceiras(Account contaOrigem, Account contaDestino, Double valor){
        if (contaOrigem.getBalance() < valor) {
            throw new RuntimeException("Saldo insuficiente");
        }

        contaOrigem.withdraw(valor);
        contaDestino.deposit(valor);

        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);

        registrarHistorico(contaOrigem, contaDestino, valor);
    }

    //Histórico
    private void registrarHistorico(Account contaOrigem, Account contaDestino, Double valor){
        historicoService.cadastrar(
                new CadastroHistoricoRequest(
                        contaOrigem,
                        contaOrigem.getCustomer(),
                        OperationType.TRANSFERENCIA,
                        "TRANSFERENCIA enviado para a conta: " + contaDestino.getAccountNumber(),
                        valor
                )
        );

       historicoService.cadastrar(
                new CadastroHistoricoRequest(
                        contaDestino,
                        contaDestino.getCustomer(),
                        OperationType.TRANSFERENCIA,
                        "TRANSFERENCIA recebida da conta: " + contaOrigem.getAccountNumber(),
                        valor
                )
        );
    }
}