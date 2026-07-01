package com.Isabela01vSilva.bank_isabela.controller;

import com.Isabela01vSilva.bank_isabela.controller.request.account.*;
import com.Isabela01vSilva.bank_isabela.controller.response.account.AccountWithCustomerResponse;
import com.Isabela01vSilva.bank_isabela.controller.response.account.UpdateAccountStatusResponse;
import com.Isabela01vSilva.bank_isabela.domain.account.Account;
import com.Isabela01vSilva.bank_isabela.domain.account.AccountType;
import com.Isabela01vSilva.bank_isabela.service.account.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("contas")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/saque")
    public ResponseEntity<String> withdrawal(@RequestBody @Valid AccountTransactionRequest request) {
        return ResponseEntity.ok(accountService.withdrawal(request));
    }

    @PostMapping("/depositar")
    public ResponseEntity<String> deposit(@RequestBody @Valid AccountTransactionRequest request) {
        return ResponseEntity.ok(accountService.deposit(request));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Account createForCpf(@RequestParam String cpf, @RequestParam AccountType accountType) {
        return accountService.createAccountForCpf(cpf, accountType);
    }

    @PatchMapping("/atualizarStatus")
    public UpdateAccountStatusResponse updateStatus(@RequestBody @Valid UpdateAccountStatusRequest request) {
        return accountService.updateAccountStatus(request);
    }

    @GetMapping("/buscar/cpf")
    public List<AccountWithCustomerResponse> searchByCpf(@PathVariable String cpf) {
        return accountService.searchByCpf(cpf);
    }

    @GetMapping("/buscar/conta")
    public ResponseEntity<AccountWithCustomerResponse> searchByAccuntNumberAndAgencyNumber(@RequestParam String accountNumber,
                                                                                           @RequestParam String agencyNumber) {
        return accountService.searchByAccountNumberAndAgency(accountNumber, agencyNumber);
    }

    @GetMapping("/saldo")
    public String getBalance(@RequestParam String accountNumber, @RequestParam String agencyNumber) {
        return accountService.getBalance(accountNumber, agencyNumber);
    }

}