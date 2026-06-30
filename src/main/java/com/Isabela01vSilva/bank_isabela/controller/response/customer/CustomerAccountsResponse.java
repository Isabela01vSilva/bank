package com.Isabela01vSilva.bank_isabela.controller.response.customer;

import com.Isabela01vSilva.bank_isabela.controller.response.account.AccountResponse;

import java.util.List;

public record CustomerAccountsResponse(CustomerResponse cliente,
                                       List<AccountResponse> conta) {
}
