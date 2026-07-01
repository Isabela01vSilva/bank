package com.Isabela01vSilva.bank_isabela.service.transfer;

import com.Isabela01vSilva.bank_isabela.controller.request.transfer.TransferRequest;
import com.Isabela01vSilva.bank_isabela.controller.response.transfer.TransferResponse;
import com.Isabela01vSilva.bank_isabela.domain.transfer.Transfer;
import com.Isabela01vSilva.bank_isabela.domain.transfer.TransferRepository;
import com.Isabela01vSilva.bank_isabela.domain.account.Account;
import com.Isabela01vSilva.bank_isabela.domain.account.AccountRepository;
import com.Isabela01vSilva.bank_isabela.mapper.TransferMappers;
import com.Isabela01vSilva.bank_isabela.service.HistoryService;
import com.Isabela01vSilva.bank_isabela.service.account.AccountValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;
    private final AccountValidationService accountValidationService;
    private final BalanceService balanceService;
    private final HistoryService historyService;

    @Transactional
    public TransferResponse transfer(TransferRequest transferRequest) {
        Account sourceAccount = findAccount(
                transferRequest.sourceAgencyNumber(),
                transferRequest.sourceAccountNumber()
        );

        Account destinationAccount = findAccount(
                transferRequest.destinationAgencyNumber(),
                transferRequest.destinationAccountNumber()
        );

        validateTransfer(sourceAccount, destinationAccount, transferRequest.amount());

        Transfer transfer = executeTransfer(sourceAccount, destinationAccount, transferRequest.amount());

        historyService.registerTransfer(sourceAccount, destinationAccount, transferRequest.amount());

        return TransferMappers.toResponse(transfer);
    }

    private void validateTransfer(Account sourceAccount, Account destinationAccount, BigDecimal amount) {
        accountValidationService.validateActiveAccount(sourceAccount);
        accountValidationService.validateActiveAccount(destinationAccount);
        accountValidationService.validateDifferentAccounts(sourceAccount, destinationAccount);
        accountValidationService.validateAmount(amount);
        accountValidationService.validateSufficientBalance(sourceAccount, amount);
    }

    private Transfer executeTransfer(Account sourceAccount, Account destinationAccount, BigDecimal amount) {

        Transfer transfer = Transfer.create(sourceAccount, destinationAccount, amount);
        transferRepository.save(transfer);

        try {
            balanceService.transfer(sourceAccount, destinationAccount, amount);
            accountRepository.save(sourceAccount);
            accountRepository.save(destinationAccount);

            transfer.complete();
            return transferRepository.save(transfer);
        } catch (Exception e) {
            transfer.fail();
            transferRepository.save(transfer);
            throw e;
        }
    }

    public Account findAccount(String agencyNumber,String accountNumber) {
        return accountRepository.findByAccountNumberAndAgencyNumber(accountNumber, agencyNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada: " + accountNumber));
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