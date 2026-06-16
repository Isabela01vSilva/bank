package com.Isabela01vSilva.bank_isabela.controller.response;

import com.Isabela01vSilva.bank_isabela.controller.response.account.AccountResponse;
import com.Isabela01vSilva.bank_isabela.controller.response.customer.CustomerResponse;

import java.util.List;

public record CustomerAccountsResponse(CustomerResponse cliente,
                                       List<AccountResponse> conta) {
}
