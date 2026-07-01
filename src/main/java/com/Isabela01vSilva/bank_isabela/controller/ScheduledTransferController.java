package com.Isabela01vSilva.bank_isabela.controller;

import com.Isabela01vSilva.bank_isabela.service.transfer.TransferService;
import com.Isabela01vSilva.bank_isabela.service.client.ScheduleClientService;
import com.Isabela01vSilva.bank_isabela.service.data.request.CreateAppointmentScheduleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("agendamentos-transfer")
public class ScheduledTransferController {

    @Autowired
    private TransferService service;

    @Autowired
    private ScheduleClientService scheduleClient;

    /*@PostMapping("/realizar")
    public ResponseEntity<MessageResponse> realizarTransferenciaAg(@RequestBody TransferenciaRequest transferenciaRequest) {
        service.agendarTransferencia(transferenciaRequest);
        return ResponseEntity.ok(new MessageResponse("Transferencia agendada com sucesso!"));
    }*/

    @PostMapping("/agendar")
    public ResponseEntity<String> criarAgendamento(@RequestBody CreateAppointmentScheduleRequest request) {
        scheduleClient.createAppointment(request);
        return ResponseEntity.ok("Agendamento enviado para o Schendulo");
    }
}
