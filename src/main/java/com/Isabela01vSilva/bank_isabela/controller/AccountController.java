package com.Isabela01vSilva.bank_isabela.controller;

import com.Isabela01vSilva.bank_isabela.controller.request.account.*;
import com.Isabela01vSilva.bank_isabela.controller.request.account.SecondAccountRequest;
import com.Isabela01vSilva.bank_isabela.controller.response.account.AccountWithCustomerResponse;
import com.Isabela01vSilva.bank_isabela.controller.response.account.MessageResponse;
import com.Isabela01vSilva.bank_isabela.controller.response.account.UpdateAccountStatusResponse;
import com.Isabela01vSilva.bank_isabela.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("contas")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PatchMapping("/atualizarStatus")
    public ResponseEntity<UpdateAccountStatusResponse> updateAccountStatus(@RequestBody UpdateAccountStatusRequest request) {
        UpdateAccountStatusResponse response = accountService.updateAccountStatus(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/depositar")
    public ResponseEntity<MessageResponse> deposit(@RequestBody DepositRequest depositRequest) {
        String message = accountService.deposit(depositRequest);
        return ResponseEntity.ok(new MessageResponse(message));
    }

    @PostMapping("/saque")
    public ResponseEntity<MessageResponse> withdraw(@RequestBody WithdrawalRequest withdrawalRequest) {
        String message = accountService.withdrawal(withdrawalRequest);
        return ResponseEntity.ok(new MessageResponse(message));
    }

    @GetMapping("/{id}/saldo")
    public ResponseEntity<MessageResponse> getBalance(@PathVariable Long id) {
        String message = accountService.getBalance(id);
        return ResponseEntity.ok(new MessageResponse(message));
    }

    /**
     * Busca todas as contas de um cliente pelo CPF.
     *
     * @param cpf O CPF do cliente (pode estar formatado ou não)
     * @return Lista de contas com informações do cliente
     */
    @GetMapping("/buscar/cpf")
    public ResponseEntity<List<AccountWithCustomerResponse>> searchByCpf(@RequestParam String cpf) {
        List<AccountWithCustomerResponse> accounts = accountService.searchAccountsByCpf(cpf);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/buscar/conta")
    public ResponseEntity<AccountWithCustomerResponse> searchByAccuntNumberAndAgencyNumber(@RequestParam String accountNumber, @RequestParam String agencyNumber) {
        AccountWithCustomerResponse account = accountService.searchAccountsByAccountNumberAndAgencyNumber(accountNumber, agencyNumber);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/abrir")
    public ResponseEntity<AccountWithCustomerResponse> openAccountByCpf(@RequestBody SecondAccountRequest request) {
        var account = accountService.createAccountForCpf(request.cpf(), request.accountType());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new AccountWithCustomerResponse(
                        account.getCustomer().getFullName(),
                        account.getCustomer().getCpf(),
                        account.getAgencyNumber(),
                        account.getAccountNumber(),
                        account.getAccountType(),
                        account.getAccountStatus(),
                        account.getBalance(),
                        account.getCreationDate()
                ));
    }
}