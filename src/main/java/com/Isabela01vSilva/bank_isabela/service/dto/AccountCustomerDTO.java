package com.Isabela01vSilva.bank_isabela.service.dto;

import com.Isabela01vSilva.bank_isabela.domain.account.Account;
import com.Isabela01vSilva.bank_isabela.domain.customer.Customer;

import java.util.List;

public record AccountCustomerDTO(List<Account> conta, Customer customer) {
}
