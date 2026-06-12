package com.Isabela01vSilva.bank_isabela.service;

import com.Isabela01vSilva.bank_isabela.controller.request.transfer.TransferRequest;
import com.Isabela01vSilva.bank_isabela.controller.request.transfer.TransferenciaRequest;
import com.Isabela01vSilva.bank_isabela.domain.account.AccountStatus;
import com.Isabela01vSilva.bank_isabela.domain.transfer.Transfer;
import com.Isabela01vSilva.bank_isabela.domain.transfer.TransferRepository;
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

import java.math.BigDecimal;

@Service
public class TransferService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ScheduleClientService scheduleClientService;

    public Account findAccount(String agencyNumber,
                                String accountNumber) {
        return accountRepository.findByAccountNumberAndAgencyNumber(agencyNumber, accountNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private void validateActiveAccount(Account account) {
        if (account.getAccountStatus() != AccountStatus.ATIVO) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    String.format("Account %s is not active", account.getAccountNumber()
                    )
            );
        }
    }

    private void validateSufficientBalance(Account sourceAccount,
                                           BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "O valor deve ser maior que Zero");
        }

        if(amount.compareTo(sourceAccount.getBalance()) > 0){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Saldo insuficiente");
        }
    }

    private void validateAccounts(Account sourceAccount,
                                  Account destinationAccount) {
        if (sourceAccount.getId().equals(destinationAccount.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "Não é permitido transferir para a mesma conta"
            );
        }
    }

    private Transfer createTransfer(TransferRequest transferRequest) {
        Transfer transfer = new Transfer();

        transfer.setSourceAccount(transferRequest.sourceAccount());
        transfer.setDestinationAccount(transferRequest.destinationAccount());
        transfer.setAmount(transferRequest.amount());
        transfer.setExecutionDate(transferRequest.executionDate());
        transfer.setTransferStatus(transferRequest.transferStatus());

        return transferRepository.save(transfer);
    }


    //Fluxo Principal de agendamento
    /*@Transactional
    public void agendarTransferencia(TransferenciaRequest request){
        Account contaOrigem = buscarContas(request.numeroContaOrigem());
        Account contaDestino = buscarContas(request.numeroContaDestino());

        validarContaAtiva(contaOrigem);
        validarContaAtiva(contaDestino);

        // Cria objeto de Transferência no banco
        Transfer transferencia = new Transfer();
        transferencia.setExecutionDate(request.executionDate());
        transferencia.setAmount(request.valor());
        transferencia.setSourceAccount(contaOrigem);
        transferencia.setDestinationAccount(contaDestino);
        transferencia.setStatus(Status.AGENDADO);

        // Salva a transferência
        transferRepository.save(transferencia);

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
    }*/

    //Agendamento
   /* private SchedulingDTO  criarAgendamento(CreateAppointmentScheduleRequest request,
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
    }*/




    /*private void falhaTransferencia(CreateAppointmentScheduleRequest create) {

        UpdateAppointmentDTO atualizado = new UpdateAppointmentDTO(
                create.executionDate(),
                create.payload(),
                Status.ERRO
        );

        scheduleClientService.updateAppointment(atualizado);
    }*/


   /*public boolean transferir(TransferRequest request) {
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
    }*/


   /* @Transactional
    public void executarOperacoesFinanceiras(Account contaOrigem, Account contaDestino, Double valor){
        if (contaOrigem.getBalance() < valor) {
            throw new RuntimeException("Saldo insuficiente");
        }

        contaOrigem.withdraw(valor);
        contaDestino.deposit(valor);

        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);

    }*/

    //Histórico
    /*private void registrarHistorico(Account contaOrigem, Account contaDestino, Double valor){
        historicoService.register(
                new RegisterHistoryRequest(
                        contaOrigem,
                        contaOrigem.getCustomer(),
                        HistoryType.TRANSFERENCIA,
                        "TRANSFERENCIA enviado para a conta: " + contaDestino.getAccountNumber(),
                        valor
                )
        );

       historicoService.register(
                new RegisterHistoryRequest(
                        contaDestino,
                        contaDestino.getCustomer(),
                        HistoryType.TRANSFERENCIA,
                        "TRANSFERENCIA recebida da conta: " + contaOrigem.getAccountNumber(),
                        valor
                )
        );
    }*/
}