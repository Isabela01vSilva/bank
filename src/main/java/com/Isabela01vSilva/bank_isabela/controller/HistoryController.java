package com.Isabela01vSilva.bank_isabela.controller;

import com.Isabela01vSilva.bank_isabela.controller.response.history.CustomerHistoryResponse;
import com.Isabela01vSilva.bank_isabela.controller.response.history.TransactionHistoryResponse;
import com.Isabela01vSilva.bank_isabela.domain.account.AccountType;
import com.Isabela01vSilva.bank_isabela.domain.historico.HistoryType;
import com.Isabela01vSilva.bank_isabela.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("historicos")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    @GetMapping("/cliente/{customerId}")
    public ResponseEntity<List<CustomerHistoryResponse>> getCustomerHistory(@PathVariable Long customerId) {
        List<CustomerHistoryResponse> history = historyService.getCustomerHistory(customerId);

        if (history.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(history);
    }

    @GetMapping("/account-type")
    public ResponseEntity<List<TransactionHistoryResponse>> getAccountTypeHistory(@RequestParam Long customerId,
                                                                                  @RequestParam AccountType accountType) {
        List<TransactionHistoryResponse> history = historyService.getAccountHistoryByAccountType(
                customerId,
                accountType);

        if (history.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(history);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionHistoryResponse>> getTransactionHistory(@PathVariable Long accountId,
                                                                                  @RequestParam List<HistoryType> historyTypes) {
        List<TransactionHistoryResponse> history = historyService.getAccountHistoryByTransactionType(accountId, historyTypes);

        if (history.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(history);
    }


    @GetMapping("/between-dates/{accountId}")
    public ResponseEntity<List<TransactionHistoryResponse>> getHistoryBetweenDates(@PathVariable Long accountId,
                                                                                   @RequestParam LocalDateTime startDate,
                                                                                   @RequestParam LocalDateTime endDate) {
        List<TransactionHistoryResponse> history = historyService.getHistoryBetweenDates(
                accountId,
                startDate,
                endDate);

        if (history.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(history);
    }
}