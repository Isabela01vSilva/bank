package com.Isabela01vSilva.bank_isabela.controller;

import com.Isabela01vSilva.bank_isabela.controller.response.history.CustomerHistoryResponse;
import com.Isabela01vSilva.bank_isabela.controller.response.history.TransactionHistoryResponse;
import com.Isabela01vSilva.bank_isabela.domain.account.AccountType;
import com.Isabela01vSilva.bank_isabela.domain.history.HistoryType;
import com.Isabela01vSilva.bank_isabela.service.history.HistoryQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("historicos")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryQueryService historyQueryService;

    @GetMapping("/conta/tipo")
    public List<TransactionHistoryResponse> getByAccountType(@RequestParam Long customerId,
                                                             @RequestParam AccountType accountType) {
        return historyQueryService.getAccountType(customerId, accountType);
    }

    @GetMapping("/cliente/tipo")
    public List<CustomerHistoryResponse> getCustomerHistoryByTypes(@RequestParam Long customerId,
                                                                   @RequestParam List<HistoryType> historyTypes) {
        return historyQueryService.getCustomerHistoryByTypes(customerId, historyTypes);
    }

    @GetMapping("/conta/tipos")
    public List<TransactionHistoryResponse> getAccountHistoryByTypes(@RequestParam Long accountId,
                                                                     @RequestParam List<HistoryType> historyTypes) {
        return historyQueryService.getAccountHistoryByTypes(accountId, historyTypes);
    }

    @GetMapping("/conta/dates")
    public List<TransactionHistoryResponse> getByDateRange(@RequestParam Long accountId,
                                                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end){
        return historyQueryService.getByDateRange(accountId, start, end);
    }

    @GetMapping("/cliente/transacoes")
    public List<TransactionHistoryResponse> getCustomerTransactions(@RequestParam Long customerId,
                                                                    @RequestParam(required = false) AccountType accountType) {
        return historyQueryService.getCustomerTransactions(customerId, accountType);
    }
}