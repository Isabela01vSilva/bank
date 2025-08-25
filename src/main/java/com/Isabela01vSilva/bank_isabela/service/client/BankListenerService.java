package com.Isabela01vSilva.bank_isabela.service.client;

import com.Isabela01vSilva.bank_isabela.domain.conta.Conta;
import com.Isabela01vSilva.bank_isabela.domain.conta.ContaRepository;
import com.Isabela01vSilva.bank_isabela.service.TransferenciaService;
import com.Isabela01vSilva.bank_isabela.service.client.dto.CallbackDTO;
import com.Isabela01vSilva.bank_isabela.service.client.dto.PayloadDTO;
import com.Isabela01vSilva.bank_isabela.service.client.dto.Status;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
public class BankListenerService {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private TransferenciaService transferenciaService;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private WebClient webClient;

    @Autowired
    private ScheduleClientService scheduleClientService;

    @SqsListener("Bank")
    public void lerMensagem(Message<String> message) throws JsonProcessingException {
        System.out.println("Mensagem recebida do SQS: " + message);
        Long appointmentId = Long.valueOf(Objects.requireNonNull(message.getHeaders().get("appointmentId", String.class)));

        try {
            PayloadDTO payloadDTO = objectMapper.readValue(message.getPayload(), PayloadDTO.class);

            Conta contaOrigem = contaRepository.findByNumero(payloadDTO.getNumeroContaOrigem())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta origem não encontrada"));

            Conta contaDestino = contaRepository.findByNumero(payloadDTO.getNumeroContaDestino())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta destino não encontrada"));

            transferenciaService.transferir(contaOrigem, contaDestino, payloadDTO.getValor());

            scheduleClientService.enviarCallback(new CallbackDTO(appointmentId, Status.CONCLUIDO));

        } catch (Exception ex) {
            System.err.println("Falha na leitura ou execução da mensagem: " + ex.getMessage());
            scheduleClientService.enviarCallback(new CallbackDTO(appointmentId, Status.RETENTAR));
            throw ex;
        }
    }
}