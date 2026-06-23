package com.Isabela01vSilva.bank_isabela.controller;

import com.Isabela01vSilva.bank_isabela.controller.response.history.CustomerHistoryResponse;
import com.Isabela01vSilva.bank_isabela.controller.response.history.TransactionHistoryResponse;
import com.Isabela01vSilva.bank_isabela.domain.account.AccountType;
import com.Isabela01vSilva.bank_isabela.domain.historico.HistoryType;
import com.Isabela01vSilva.bank_isabela.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("historicos")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<CustomerHistoryResponse>> getCustomerHistory(@PathVariable Long customerId) {
        var historyTypes = List.of(
                HistoryType.ACCOUNT_CREATED,
                HistoryType.ACCOUNT_REACTIVATED,
                HistoryType.ACCOUNT_CLOSED,
                HistoryType.CUSTOMER_UPDATED,
                HistoryType.CUSTOMER_REACTIVATED,
                HistoryType.CUSTOMER_INACTIVATED
        );

        List<CustomerHistoryResponse> history = historyService.getCustomerHistoryByTransactionType(customerId, historyTypes);

        return history.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(history);
    }

    @GetMapping("/account-type")
    public ResponseEntity<List<TransactionHistoryResponse>> getAccountTypeHistory(@RequestParam Long customerId,
                                                                                  @RequestParam AccountType accountType) {
        List<TransactionHistoryResponse> history = historyService.getAccountHistoryByAccountType(
                customerId,
                accountType);

        return history.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(history);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionHistoryResponse>> getTransactionHistory(@PathVariable Long accountId,
                                                                                  @RequestParam List<HistoryType> historyTypes) {

        List<TransactionHistoryResponse> history = historyService.getAccountHistoryByTransactionType(accountId, historyTypes);

        return history.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(history);
    }


    @GetMapping("/between-dates/{accountId}")
    public ResponseEntity<List<TransactionHistoryResponse>> getHistoryBetweenDates(@PathVariable Long accountId,
                                                                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                                                   @RequestParam LocalDateTime startDate,
                                                                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                                                   @RequestParam LocalDateTime endDate) {
        List<TransactionHistoryResponse> history = historyService.getHistoryBetweenDates(
                accountId,
                startDate,
                endDate);

        return history.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(history);
    }
}