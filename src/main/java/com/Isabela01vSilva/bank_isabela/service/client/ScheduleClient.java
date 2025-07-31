package com.Isabela01vSilva.bank_isabela.service.client;

import com.Isabela01vSilva.bank_isabela.service.DTO.AgendamentoDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ScheduleClient {

    private final WebClient webClient;

    public ScheduleClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8081").build();
    }

    public Mono<AgendamentoDTO> buscarAgendamento(Long id){
        return webClient
                .get()
                .uri("/agendamentos/{id}", id)
                .retrieve()
                .bodyToMono(AgendamentoDTO.class);
    }
}
