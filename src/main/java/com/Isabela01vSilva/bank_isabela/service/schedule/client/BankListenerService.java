package com.Isabela01vSilva.bank_isabela.service.schedule.client;

import com.Isabela01vSilva.bank_isabela.domain.account.AccountRepository;
import com.Isabela01vSilva.bank_isabela.service.transfer.TransferService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Service
public class BankListenerService {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private TransferService transferenciaService;

    @Autowired
    private AccountRepository contaRepository;

    @Autowired
    private WebClient webClient;

    @Autowired
    private ScheduleClientService scheduleClientService;

    @SqsListener("Bank")
    public void lerMensagem(Message<String> message) throws JsonProcessingException {
        System.out.println("Mensagem recebida do SQS: " + message);
        Long appointmentId = Long.valueOf(Objects.requireNonNull(message.getHeaders().get("appointmentId", String.class)));

      /*  try {
            PayloadDTO payloadDTO = objectMapper.readValue(message.getPayload(), PayloadDTO.class);

            Account contaOrigem = contaRepository.findByAccountNumber(payloadDTO.getNumeroContaOrigem())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta origem não encontrada"));

            Account contaDestino = contaRepository.findByAccountNumber(payloadDTO.getNumeroContaDestino())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta destino não encontrada"));
            AGENDADO,
                    PROCESSANDO,
                    CONCLUIDO,
                    CANCELADO,
                    FALHA
            transferenciaService.executarOperacoesFinanceiras(contaOrigem, contaDestino, payloadDTO.getValor());

            scheduleClientService.enviarCallback(new CallbackDTO(appointmentId, TransferStatus.CONCLUIDO));

        } catch (Exception ex) {
            System.err.println("Falha na leitura ou execução da mensagem: " + ex.getMessage());
            scheduleClientService.enviarCallback(new CallbackDTO(appointmentId, TransferStatus.RETENTAR));
            throw ex;
        }*/
    }
}