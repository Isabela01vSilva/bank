package com.Isabela01vSilva.bank_isabela.service;

import com.Isabela01vSilva.bank_isabela.domain.conta.Conta;
import com.Isabela01vSilva.bank_isabela.domain.conta.ContaRepository;
import com.Isabela01vSilva.bank_isabela.service.client.dto.CallbackDTO;
import com.Isabela01vSilva.bank_isabela.service.client.dto.PayloadDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

@Service
public class BankListenerService {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private TransferenciaService transferenciaService;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private WebClient webClient;

    @Value("${callback}")
    private String scheduloCallbackUrl;

    @SqsListener("Bank")  // nome da fila SQS
    public void lerMensagem(Message<String> message) {
        System.out.println("Mensagem recebida do SQS: " + message);
        Long appointmentId = null;

        try {
            PayloadDTO payloadDTO = objectMapper.readValue(message.getPayload(), PayloadDTO.class);
            appointmentId = payloadDTO.getAppointmentId(); // pegue do PayloadDTO

            Conta contaOrigem = contaRepository.findByNumero(payloadDTO.getNumeroContaOrigem())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            Conta contaDestino = contaRepository.findByNumero(payloadDTO.getNumeroContaDestino())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

            // Executa a transferência
            boolean sucesso = transferenciaService.transferir(contaOrigem, contaDestino, payloadDTO.getValor());

            // Callback baseado no sucesso real
            if (appointmentId != null) enviarCallback(appointmentId, sucesso);

        } catch (Exception ex) {
            System.err.println("Falha na leitura ou execução da mensagem: " + ex.getMessage());

            if (appointmentId != null) enviarCallback(appointmentId, false);
        }
    }

    private void enviarCallback(Long appointmentId, boolean sucesso) {
        CallbackDTO callback = new CallbackDTO();
        callback.setAppointmentId(appointmentId);
        callback.setSucesso(sucesso);

        webClient.post()
                .uri(scheduloCallbackUrl)
                .bodyValue(callback)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnError(err -> System.err.println("Erro no callback: " + err.getMessage()))
                .subscribe();
    }
}
