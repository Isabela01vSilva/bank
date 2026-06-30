package com.Isabela01vSilva.bank_isabela.controller.request.customer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = false)
public record UpdateCustomerRequest(
        String fullName,
        String email,
        String phoneNumber
) {
}
