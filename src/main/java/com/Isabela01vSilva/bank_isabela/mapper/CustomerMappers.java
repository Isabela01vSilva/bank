package com.Isabela01vSilva.bank_isabela.mapper;

import com.Isabela01vSilva.bank_isabela.commons.Formatters;
import com.Isabela01vSilva.bank_isabela.controller.request.customer.CustomerAccountRequest;
import com.Isabela01vSilva.bank_isabela.controller.response.customer.CustomerAccountsResponse;
import com.Isabela01vSilva.bank_isabela.controller.response.account.AccountResponse;
import com.Isabela01vSilva.bank_isabela.controller.response.customer.CustomerResponse;
import com.Isabela01vSilva.bank_isabela.domain.account.Account;
import com.Isabela01vSilva.bank_isabela.domain.customer.Customer;
import com.Isabela01vSilva.bank_isabela.service.dto.AccountCustomerDTO;

import java.util.List;

public class CustomerMappers {

    public static Customer fromRequestToCustomer(CustomerAccountRequest data) {
        Customer customer = new Customer();
        customer.setFullName(data.fullName());
        customer.setBirthDate(data.birthDate());
        customer.setCpf(Formatters.normalize(data.cpf()));
        customer.setEmail(Formatters.normalizeEmail(data.email()));
        customer.setPhoneNumber(Formatters.normalizePhone(data.phoneNumber()));
        customer.activate(); // usa o metodo de domínio
        return customer;
    }

    public static CustomerResponse fromCustomerToResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getFullName(),
                customer.getBirthDate(),
                customer.getCpf(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                customer.getCustomerStatus()
        );
    }

    public static AccountResponse fromAccountToResponse(Account account) {
        return new AccountResponse(
                account.getAccountNumber(),
                account.getAgencyNumber(),
                account.getAccountType(),
                account.getAccountStatus(),
                account.getBalance(),
                account.getCreationDate()
        );
    }

    public static CustomerAccountsResponse fromAccountCustomerDTOToResponse(
            AccountCustomerDTO dto) {

        List<AccountResponse> contas = dto.conta().stream()
                .map(CustomerMappers::fromAccountToResponse)
                .toList();

        return new CustomerAccountsResponse(
                fromCustomerToResponse(dto.customer()),
                contas
        );
    }
}
