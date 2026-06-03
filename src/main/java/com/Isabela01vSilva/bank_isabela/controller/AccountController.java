package com.Isabela01vSilva.bank_isabela.controller;

import com.Isabela01vSilva.bank_isabela.controller.request.account.*;
import com.Isabela01vSilva.bank_isabela.controller.response.account.AccountResponse;
import com.Isabela01vSilva.bank_isabela.controller.response.account.MessageResponse;
import com.Isabela01vSilva.bank_isabela.domain.account.Account;
import com.Isabela01vSilva.bank_isabela.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("contas")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PatchMapping("/{id}/status")
    public ResponseEntity<AccountResponse> updateAccountStatus(@PathVariable Long id,
                                                               @RequestBody UpdateAccountStatusRequest updateStatusRequest) {
        Account account = accountService.updateAccountStatus(id, updateStatusRequest);
        return ResponseEntity.ok(new AccountResponse(
                account.getAccountNumber(),
                account.getAccountNumber(),
                account.getAccountType(),
                account.getAccountStatus(),
                account.getBalance(),
                account.getCreationDate()));
    }

    @PostMapping("/depositar")
    public ResponseEntity<MessageResponse> deposit(@RequestBody DepositRequest depositRequest) {
        String message  = accountService.deposit(depositRequest);
        return ResponseEntity.ok(new MessageResponse(message ));
    }

    @PostMapping("/saque")
    public ResponseEntity<MessageResponse> withdraw(@RequestBody WithdrawalRequest withdrawalRequest) {
        String message  = accountService.withdrawal(withdrawalRequest);
        return ResponseEntity.ok(new MessageResponse(message ));
    }

    @GetMapping("/{id}/saldo")
    public ResponseEntity<MessageResponse> getBalance(@PathVariable Long id) {
        String message  = accountService.getBalance(id);
        return ResponseEntity.ok(new MessageResponse(message ));
    }
}