package com.Isabela01vSilva.bank_isabela.controller;

import com.Isabela01vSilva.bank_isabela.service.DTO.AgendamentoDTO;
import com.Isabela01vSilva.bank_isabela.service.client.ScheduleClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("pagamentos") // rota base
public class PagamentoController {

    @Autowired
    private ScheduleClient scheduleClient;

    @GetMapping("/agendamento/{id}")
    public ResponseEntity<AgendamentoDTO> getAgendamento(@PathVariable Long id) {
        AgendamentoDTO dto = scheduleClient.buscarAgendamento(id).block();
        return ResponseEntity.ok(dto);
    }
}