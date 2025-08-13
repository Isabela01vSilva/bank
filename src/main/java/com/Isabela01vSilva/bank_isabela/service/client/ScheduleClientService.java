package com.Isabela01vSilva.bank_isabela.service.client;

import com.Isabela01vSilva.bank_isabela.controller.request.CreateAppointmentRequest;
import com.Isabela01vSilva.bank_isabela.exception.SchedulingNotFoundException;
import com.Isabela01vSilva.bank_isabela.service.client.dto.SchedulingDTO;
import com.Isabela01vSilva.bank_isabela.service.client.dto.CreateScheduleDTO;
import com.Isabela01vSilva.bank_isabela.service.client.dto.StatusDTO;
import com.Isabela01vSilva.bank_isabela.service.client.dto.UpdateAppointmentDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Component
public class ScheduleClientService {

    private final WebClient webClient;

    @Value("${schedule-path.by-id}")
    private String getAppointmentByIdPath;

    @Value("${schedule-path.all}")
    private String getAllAppointmentsPath;

    @Value("${appointments-path}")
    private String createAppointmentPath;

    @Value("${cancel-appointment-path}")
    private String cancelAppointmentPath;

    public ScheduleClientService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8081").build();
    }

    public SchedulingDTO getAppointmentById(Long id) {
        try {
            return webClient
                    .get()
                    .uri(getAppointmentByIdPath, id)
                    .retrieve()
                    .bodyToMono(SchedulingDTO.class)
                    .block();
        } catch (WebClientResponseException.NotFound e) {
            throw new SchedulingNotFoundException("Agendamento não encontrado com id " + id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar o agendamento: " + e.getMessage());
        }
    }

    public List<SchedulingDTO> getAllAppointments() {
        return webClient
                .get()
                .uri(getAllAppointmentsPath)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<SchedulingDTO>>() {
                })
                .block();
    }

    public SchedulingDTO createAppointment(CreateScheduleDTO request) {

        CreateAppointmentRequest schenduleRequest = new CreateAppointmentRequest(
                request.executionDate(),
                request.payloadDTO(),
                "BANK",
                request.status()
        );

        return webClient
                .post()
                .uri(createAppointmentPath)
                .bodyValue(schenduleRequest)
                .retrieve()
                .bodyToMono(SchedulingDTO.class)
                .block();
    }

    public SchedulingDTO updateAppointment(UpdateAppointmentDTO dto) {
        UpdateAppointmentDTO scheduleRequest = new UpdateAppointmentDTO(
                dto.executionDate(),
                dto.payload(),
                "BANK",
                dto.status()
        );

        return webClient
                .put()
                .uri(updateAppointmentPath)
                .bodyValue(scheduleRequest)
                .retrieve()
                .bodyToMono(SchedulingDTO.class)
                .block();
    }

    public StatusDTO cancelAppointment(Long id){
        try {
            return webClient
                    .put()
                    .uri(cancelAppointmentPath, id)
                    .retrieve()
                    .bodyToMono(StatusDTO.class)
                    .block();
        } catch (WebClientResponseException.NotFound e) {
            throw new SchedulingNotFoundException("Agendamento não encontrado com id " + id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar o agendamento: " + e.getMessage());
        }
    }
}
