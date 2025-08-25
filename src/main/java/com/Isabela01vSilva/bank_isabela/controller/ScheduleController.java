package com.Isabela01vSilva.bank_isabela.controller;

import com.Isabela01vSilva.bank_isabela.service.client.dto.CallbackDTO;
import com.Isabela01vSilva.bank_isabela.service.client.dto.SchedulingDTO;
import com.Isabela01vSilva.bank_isabela.service.client.ScheduleClientService;
import com.Isabela01vSilva.bank_isabela.service.client.dto.StatusDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("agendamento")
public class ScheduleController {

    @Autowired
    private ScheduleClientService scheduleClient;

    @GetMapping("/{id}")
    public ResponseEntity<SchedulingDTO> getAppointmentById(@PathVariable Long id) {
        SchedulingDTO dto = scheduleClient.getAppointmentById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping()
    public ResponseEntity<List<SchedulingDTO>> getAllAppointments(){
        List<SchedulingDTO> dtos = scheduleClient.getAllAppointments();
        return  ResponseEntity.ok(dtos);
    }

    @PutMapping("cancel/{id}")
    public ResponseEntity<StatusDTO> cancelAppointment(@PathVariable Long id){
        StatusDTO dto = scheduleClient.cancelAppointment(id);
        return ResponseEntity.ok(dto);
    }
}