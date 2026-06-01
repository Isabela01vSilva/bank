package com.Isabela01vSilva.bank_isabela.controller;

import com.Isabela01vSilva.bank_isabela.controller.request.transfer.TransferRequest;
import com.Isabela01vSilva.bank_isabela.controller.request.transfer.TransferenciaRequest;
import com.Isabela01vSilva.bank_isabela.controller.response.conta.MensagemResponse;
import com.Isabela01vSilva.bank_isabela.domain.conta.Conta;
import com.Isabela01vSilva.bank_isabela.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("transf")
public class TransferController {

    @Autowired
    private TransferService service;

    @PostMapping("/realizar")
    public ResponseEntity<MensagemResponse> realizarTransferencia(@RequestBody TransferRequest transferenciaRequest) {
        service.transferir(transferenciaRequest);
        return ResponseEntity.ok(new MensagemResponse("Transferencia realizada com sucesso!"));
    }
}
