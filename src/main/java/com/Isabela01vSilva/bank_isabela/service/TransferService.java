package com.Isabela01vSilva.bank_isabela.service;

import com.Isabela01vSilva.bank_isabela.controller.request.transfer.TransferRequest;
import com.Isabela01vSilva.bank_isabela.domain.transfer.Transfer;
import com.Isabela01vSilva.bank_isabela.domain.transfer.TransferRepository;
import com.Isabela01vSilva.bank_isabela.domain.account.Account;
import com.Isabela01vSilva.bank_isabela.domain.account.AccountRepository;
import com.Isabela01vSilva.bank_isabela.domain.transfer.TransferStatus;
import com.Isabela01vSilva.bank_isabela.service.client.ScheduleClientService;
import com.Isabela01vSilva.bank_isabela.service.client.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransferService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private AccountValidationService accountValidationService;

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ScheduleClientService scheduleClientService;

    @Transactional
    public String transfer(TransferRequest transferRequest) {
        Account sourceAccount = findAccount(
                transferRequest.sourceAgencyNumber(),
                transferRequest.sourceAccountNumber()
        );

        Account destinationAccount = findAccount(
                transferRequest.destinationAgencyNumber(),
                transferRequest.destinationAccountNumber()
        );

        validateTransfer(sourceAccount, destinationAccount, transferRequest.amount());

        executeTransfer(sourceAccount, destinationAccount, transferRequest.amount());

        historyService.registerTransfer(sourceAccount, destinationAccount, transferRequest.amount());

        return "Transferência realizada com sucesso!";
    }

    private void validateTransfer(Account sourceAccount,
                                  Account destinationAccount,
                                  BigDecimal amount) {

        accountValidationService.validateActiveAccount(sourceAccount);

        accountValidationService.validateActiveAccount(destinationAccount);

        accountValidationService.validateAccounts(sourceAccount, destinationAccount);

        accountValidationService.validateSufficientBalance(sourceAccount, amount);
    }

    public Account findAccount(String agencyNumber,
                               String accountNumber) {
        return accountRepository.findByAccountNumberAndAgencyNumber(accountNumber, agencyNumber )
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }


    private Transfer createTransfer(Account sourceAccount,
                                    Account destinationAccount,
                                    BigDecimal amount,
                                    TransferStatus transferStatus
    ) {

        Transfer transfer = new Transfer();

        transfer.setSourceAccount(sourceAccount);
        transfer.setDestinationAccount(destinationAccount);
        transfer.setAmount(amount);
        transfer.setCreatedAt(LocalDateTime.now());
        transfer.setExecutionDate(LocalDateTime.now());
        transfer.setTransferStatus(transferStatus);

        return transfer;
    }

    @Transactional
    private void executeTransfer(Account sourceAccount,
                                 Account destinationAccount,
                                 BigDecimal amount) {

        Transfer transfer = createTransfer(sourceAccount,  destinationAccount, amount, TransferStatus.PROCESSING);

        try {
            transfer = transferRepository.save(transfer);

            balanceService.transfer(sourceAccount, destinationAccount, amount);

            transfer.setTransferStatus(TransferStatus.COMPLETED);
            transferRepository.save(transfer);
        } catch (Exception e) {
            transfer.setTransferStatus(TransferStatus.FAILED);
            transferRepository.save(transfer);

            throw e;
        }
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


}