package com.Isabela01vSilva.bank_isabela.controller;


import com.Isabela01vSilva.bank_isabela.controller.request.conta.TransferenciaRequest;
import com.Isabela01vSilva.bank_isabela.controller.response.conta.MensagemResponse;
import com.Isabela01vSilva.bank_isabela.service.TransferenciaService;
import com.Isabela01vSilva.bank_isabela.service.client.dto.PayloadDTO;
import com.Isabela01vSilva.bank_isabela.service.client.dto.SchedulingDTO;
import com.Isabela01vSilva.bank_isabela.service.client.dto.Status;
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

    @PostMapping("/tt")
    public ResponseEntity<MensagemResponse> realizarTransferencia(@RequestBody TransferenciaRequest transferenciaRequest) {

        SchedulingDTO transferenciaDTO = new SchedulingDTO(
                transferenciaRequest.executionDate(),
                new PayloadDTO(
                        transferenciaRequest.valor(),
                        transferenciaRequest.numeroContaOrigem(),
                        transferenciaRequest.numeroContaDestino()
                ),
                Status.AGENDADO
        );

        service.realizarTransferencia(transferenciaDTO);

        return ResponseEntity.ok(new MensagemResponse("Transferencia realizada com sucesso!"));
    }
}
