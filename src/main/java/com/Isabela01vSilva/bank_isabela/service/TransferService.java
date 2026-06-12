package com.Isabela01vSilva.bank_isabela.service;

import com.Isabela01vSilva.bank_isabela.controller.request.history.RegisterHistoryRequest;
import com.Isabela01vSilva.bank_isabela.controller.request.transfer.TransferRequest;
import com.Isabela01vSilva.bank_isabela.controller.request.transfer.TransferenciaRequest;
import com.Isabela01vSilva.bank_isabela.domain.account.AccountStatus;
import com.Isabela01vSilva.bank_isabela.domain.historico.History;
import com.Isabela01vSilva.bank_isabela.domain.historico.HistoryRepository;
import com.Isabela01vSilva.bank_isabela.domain.historico.HistoryType;
import com.Isabela01vSilva.bank_isabela.domain.transfer.Transfer;
import com.Isabela01vSilva.bank_isabela.domain.transfer.TransferRepository;
import com.Isabela01vSilva.bank_isabela.domain.account.Account;
import com.Isabela01vSilva.bank_isabela.domain.account.AccountRepository;
import com.Isabela01vSilva.bank_isabela.domain.transfer.TransferStatus;
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
    private HistoryRepository historyRepository;

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

        validateActiveAccount(sourceAccount);
        validateActiveAccount(destinationAccount);

        validateAccounts(sourceAccount, destinationAccount);

        validateSufficientBalance(sourceAccount, transferRequest.amount());
        executeTransfer(sourceAccount, destinationAccount, transferRequest.amount());

        registerTransferHistory(sourceAccount, destinationAccount, transferRequest.amount());

        return "Transferência realizada com sucesso!";
    }

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

        if (amount.compareTo(sourceAccount.getBalance()) > 0) {
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

    private Transfer createTransfer(TransferRequest transferRequest,
                                    Account sourceAccount,
                                    Account destinationAccount) {

        Transfer transfer = new Transfer();

        transfer.setSourceAccount(sourceAccount);
        transfer.setDestinationAccount(destinationAccount);
        transfer.setAmount(transferRequest.amount());
        transfer.setExecutionDate(transferRequest.executionDate());
        transfer.setTransferStatus(TransferStatus.AGENDADO);

        return transferRepository.save(transfer);
    }

    private void registerTransferHistory(Account sourceAccount,
                                         Account destinationAccount,
                                         BigDecimal amount) {
        History transferOut = new History();
        transferOut.setAccount(sourceAccount);
        transferOut.setCustomer(sourceAccount.getCustomer());
        transferOut.setHistoryType(HistoryType.TRANSFER);
        transferOut.setAmount(amount);
        transferOut.setDescription(
                "Transferência enviada para a conta: "
                        + destinationAccount.getAgencyNumber()
                        + destinationAccount.getAccountNumber()
                        + " no valor de R$ " + amount
        );

        historyRepository.save(transferOut);

        History transferIn = new History();
        transferIn.setAccount(destinationAccount);
        transferIn.setCustomer(destinationAccount.getCustomer());
        transferIn.setHistoryType(HistoryType.TRANSFER);
        transferIn.setAmount(amount);
        transferIn.setDescription(
                "Transferência recebida da conta: "
                        + sourceAccount.getAgencyNumber()
                        + sourceAccount.getAccountNumber()
                        + " no valor de R$ " + amount
        );

        historyRepository.save(transferIn);
    }

    private void executeTransfer(Transfer transfer,
                                 Account sourceAccount,
                                 Account destinationAccount,
                                 BigDecimal amount) {

        try {
            transfer.setTransferStatus(TransferStatus.PROCESSANDO);
            transferRepository.save(transfer);

            sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
            destinationAccount.setBalance(destinationAccount.getBalance().add(amount));

            accountRepository.save(sourceAccount);
            accountRepository.save(destinationAccount);

            transfer.setTransferStatus(TransferStatus.CONCLUIDO);
            transferRepository.save(transfer);
        } catch (Exception e) {
            transfer.setTransferStatus(TransferStatus.FALHA);
            transferRepository.save(transfer);

            throw e;
        }
    }

    private void updateTransferStatus(Transfer transfer,
                                      TransferStatus transferStatus) {
        transfer.setTransferStatus(transferStatus);
        transferRepository.save(transfer);
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