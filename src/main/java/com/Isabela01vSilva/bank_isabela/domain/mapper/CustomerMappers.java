package com.Isabela01vSilva.bank_isabela.domain.mapper;

import com.Isabela01vSilva.bank_isabela.commons.Formatters;
import com.Isabela01vSilva.bank_isabela.controller.request.CustomerAccountRequest;
import com.Isabela01vSilva.bank_isabela.domain.customer.Customer;
import com.Isabela01vSilva.bank_isabela.domain.customer.CustomerStatus;

public class CustomerMappers {
    public static Customer fromRequestToCustomer(CustomerAccountRequest data) {
        Customer customer = new Customer();
        customer.setFullName(data.fullName());
        customer.setBirthDate(data.birthDate());
        customer.setCpf(Formatters.normalize(data.cpf()));
        customer.setEmail(Formatters.normalizeEmail(data.email()));
        customer.setPhoneNumber(Formatters.normalizePhone(data.phoneNumber()));
        customer.setCustomerStatus(CustomerStatus.ATIVO);
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
}
