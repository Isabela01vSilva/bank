package com.Isabela01vSilva.bank_isabela.controller.request.account;

import com.Isabela01vSilva.bank_isabela.domain.account.AccountStatus;

public record UpdateAccountStatusRequest(
        String accountNumber,
        String agencyNumber,
        AccountStatus accountStatus,
        String statusChangeReason) {
}
