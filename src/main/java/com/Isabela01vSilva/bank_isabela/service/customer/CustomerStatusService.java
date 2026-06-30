package com.Isabela01vSilva.bank_isabela.service.customer;

import com.Isabela01vSilva.bank_isabela.domain.customer.Customer;
import com.Isabela01vSilva.bank_isabela.domain.customer.CustomerRepository;
import com.Isabela01vSilva.bank_isabela.domain.customer.CustomerStatus;
import com.Isabela01vSilva.bank_isabela.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerStatusService {

    private final CustomerRepository customerRepository;
    private final HistoryService historyService;

    public void updateStatusAccounts(Customer customer, boolean hasActiveAccount){
        CustomerStatus newStatus = hasActiveAccount ? CustomerStatus.ATIVO : CustomerStatus.INATIVO;

        if(customer.getCustomerStatus() == newStatus) return;

        if(hasActiveAccount){
            customer.activate();
        }
        else {
            customer.deactivate();
        }

        customerRepository.save(customer);
        historyService.registerCustomerStatusChange(customer, newStatus);
    }
}