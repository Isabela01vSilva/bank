package com.Isabela01vSilva.bank_isabela.controller;

import com.Isabela01vSilva.bank_isabela.controller.request.conta.TransferenciaRequest;
import com.Isabela01vSilva.bank_isabela.controller.response.conta.MensagemResponse;
import com.Isabela01vSilva.bank_isabela.service.TransferenciaService;
import com.Isabela01vSilva.bank_isabela.service.client.ScheduleClientService;
import com.Isabela01vSilva.bank_isabela.service.client.dto.CreateScheduleDTO;
import com.Isabela01vSilva.bank_isabela.service.data.request.CreateAppointmentScheduleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("transf")
public class PagamentoController {

    @Autowired
    private TransferenciaService service;

    @Autowired
    private ScheduleClientService scheduleClient;

    @PostMapping("/realizar")
    public ResponseEntity<MensagemResponse> realizarTransferencia(@RequestBody TransferenciaRequest transferenciaRequest) {
        service.agendarTransferencia(transferenciaRequest);
        return ResponseEntity.ok(new MensagemResponse("Transferencia agendada com sucesso!"));
    }

    @PostMapping("/agendar")
    public ResponseEntity<String> criarAgendamento(@RequestBody CreateAppointmentScheduleRequest request) {
        scheduleClient.createAppointment(request);
        return ResponseEntity.ok("Agendamento enviado para o Schendulo");
    }
}
