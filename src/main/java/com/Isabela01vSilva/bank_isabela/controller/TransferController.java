package com.Isabela01vSilva.bank_isabela.controller;

import com.Isabela01vSilva.bank_isabela.controller.request.transfer.TransferRequest;
import com.Isabela01vSilva.bank_isabela.controller.response.account.MessageResponse;
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
    public ResponseEntity<MessageResponse> realizarTransferencia(@RequestBody TransferRequest transferenciaRequest) {
        service.transfer(transferenciaRequest);
        return ResponseEntity.ok(new MessageResponse("Transferencia realizada com sucesso!"));
    }
}
