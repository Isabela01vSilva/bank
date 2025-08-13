package com.Isabela01vSilva.bank_isabela.service;

import com.Isabela01vSilva.bank_isabela.controller.request.historico.CadastroHistoricoRequest;
import com.Isabela01vSilva.bank_isabela.domain.conta.Conta;
import com.Isabela01vSilva.bank_isabela.domain.conta.ContaRepository;
import com.Isabela01vSilva.bank_isabela.domain.historico.TipoOperacao;
import com.Isabela01vSilva.bank_isabela.service.client.ScheduleClientService;
import com.Isabela01vSilva.bank_isabela.service.client.dto.*;
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


    @Transactional
    public void realizarTransferencia(SchedulingDTO transferencia){
        Conta contaOrigem = buscarContas(transferencia.getPayload().numeroContaOrigem());
        Conta contaDestino = buscarContas(transferencia.getPayload().numeroContaDestino());

        validarConta(contaOrigem, contaDestino);

        CreateScheduleDTO criarAgendamentoDTO = new CreateScheduleDTO(
                transferencia.getExecutionDate(),
                new PayloadDTO(
                        transferencia.getPayload().valor(),
                        transferencia.getPayload().numeroContaOrigem(),
                        transferencia.getPayload().numeroContaDestino()
                ),
                "BANK",
                Status.AGENDADO
        );

        criarAgendamento(criarAgendamentoDTO, contaOrigem, contaDestino);
        registrarHistorico(contaOrigem, contaDestino, transferencia);

    }

    private Conta buscarContas(String numeroConta){
        return contaRepository.findByNumero(numeroConta)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));
    }

    private void validarConta(Conta contaOrigem, Conta contaDestino){
        contaOrigem.statusDaConta();
        contaDestino.statusDaConta();
    }

    private void executarOperacoesFinanceiras(Conta contaOrigem, Conta contaDestino, Double valor){
        contaOrigem.sacar(valor);
        contaDestino.depositar(valor);

        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);
    }

    private void criarAgendamento(CreateScheduleDTO criarAgendamentoDTO, Conta contaOrigem, Conta contaDestino){
       try {
           SchedulingDTO dto = scheduleClientService.createAppointment(criarAgendamentoDTO);

           UpdateAppointmentDTO updateAppointmentDTO = new UpdateAppointmentDTO(
                   dto.getExecutionDate(),
                   dto.getPayload(),
                   "BANK",
                   Status.AGENDADO
           );

           Double valor = dto.getPayload().valor();
           Double saldoConta = contaOrigem.getSaldo();

           if (valor.compareTo(saldoConta) > 0) {
               falhaTransferencia(updateAppointmentDTO);
               return;
           }
           transferir(updateAppointmentDTO, contaOrigem, contaDestino, valor);

       } catch (Exception e){
           System.err.println("Error ao criar agendamento: " + e.getMessage());
       }
    }

    private void transferir(UpdateAppointmentDTO dto, Conta contaOrigem, Conta contaDestino, Double valor){
        dto.setStatus(Status.CONCLUIDO);
        scheduleClientService.updateAppointment(dto);
        executarOperacoesFinanceiras(contaOrigem, contaDestino, valor);
    }

    private void falhaTransferencia(UpdateAppointmentDTO dto){
        dto.setStatus(Status.FALHOU);
        scheduleClientService.updateAppointment(dto);
    }

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
    }
}