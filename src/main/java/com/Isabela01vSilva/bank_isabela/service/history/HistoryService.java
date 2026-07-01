package com.Isabela01vSilva.bank_isabela.service.history;

import com.Isabela01vSilva.bank_isabela.domain.account.Account;
import com.Isabela01vSilva.bank_isabela.domain.customer.Customer;
import com.Isabela01vSilva.bank_isabela.domain.customer.CustomerStatus;
import com.Isabela01vSilva.bank_isabela.domain.history.History;
import com.Isabela01vSilva.bank_isabela.domain.history.HistoryRepository;
import com.Isabela01vSilva.bank_isabela.domain.history.HistoryType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;

    // ==================== CONTA ====================
    public void registerAccountCreated(Account account) {
        save(account, account.getCustomer(), HistoryType.ACCOUNT_CREATED,
                "Conta criada com sucesso!", null);
    }

    public void registerAccountReactivated(Account account) {
        save(account, account.getCustomer(), HistoryType.ACCOUNT_REACTIVATED,
                "Conta reativada com sucesso!", null);
    }

    public void registerAccountClosed(Account account) {
        save(account, account.getCustomer(), HistoryType.ACCOUNT_CLOSED,
                account.getStatusChangeReason(), null);
    }

    // ==================== TRANSAÇÕES ====================
    public void registerWithdrawal(Account account, BigDecimal amount) {
        save(account, account.getCustomer(), HistoryType.WITHDRAWAL,
                "Saque realizado com sucesso!", amount);
    }

    public void registerWithdrawalFailed(Account account, BigDecimal amount, String reason) {
        save(account, account.getCustomer(), HistoryType.WITHDRAWAL_FAILED, reason, amount);
    }

    public void registerDeposit(Account account, BigDecimal amount) {
        save(account, account.getCustomer(), HistoryType.DEPOSIT,
                "Depósito realizado com sucesso!", amount);
    }

    public void registerDepositFailed(Account account, BigDecimal amount, String reason) {
        save(account, account.getCustomer(), HistoryType.DEPOSIT_FAILED, reason, amount);  // bug corrigido
    }

    public void registerTransfer(Account source, Account destination, BigDecimal amount) {
        historyRepository.saveAll(List.of(
                createTransferOut(source, destination, amount),
                createTransferIn(source, destination, amount)
        ));
    }

    // ==================== CLIENTE ====================
    public void registerCustomerUpdate(Customer customer, String description) {
        save(null, customer, HistoryType.CUSTOMER_UPDATED, description, null);
    }

    public void registerCustomerStatusChange(Customer customer, CustomerStatus status) {
        HistoryType type = status == CustomerStatus.ATIVO
                ? HistoryType.CUSTOMER_REACTIVATED
                : HistoryType.CUSTOMER_INACTIVATED;

        String description = status == CustomerStatus.ATIVO
                ? "Cliente reativado por possuir conta ativa"
                : "Cliente inativado por não possuir contas ativas";

        save(null, customer, type, description, null);
    }

    public void registerCustomerReactivated(Account account, Customer customer) {
        save(account, customer, HistoryType.CUSTOMER_REACTIVATED,
                "Cliente reativado por possuir conta ativa.", null);
    }

    // ==================== PRIVADOS ====================
    private void save(Account account, Customer customer,
                      HistoryType type, String description, BigDecimal amount) {
        History history = new History();
        history.setAccount(account);
        history.setCustomer(customer);
        history.setHistoryType(type);
        history.setDescription(description);
        history.setAmount(amount);
        history.setTransactionDate(LocalDateTime.now());
        historyRepository.save(history);
    }

    private History createTransferOut(Account source, Account destination, BigDecimal amount) {
        History h = new History();
        h.setAccount(source);
        h.setCustomer(source.getCustomer());
        h.setHistoryType(HistoryType.TRANSFER);
        h.setAmount(amount);
        h.setTransactionDate(LocalDateTime.now()); // bug corrigido
        h.setDescription(String.format(
                "Transferência enviada para agência %s conta %s no valor de R$ %s",
                destination.getAgencyNumber(), destination.getAccountNumber(), amount));
        return h;
    }

    private History createTransferIn(Account source, Account destination, BigDecimal amount) {
        History h = new History();
        h.setAccount(destination);
        h.setCustomer(destination.getCustomer());
        h.setHistoryType(HistoryType.TRANSFER);
        h.setAmount(amount);
        h.setTransactionDate(LocalDateTime.now()); // bug corrigido
        h.setDescription(String.format(
                "Transferência recebida da agência %s conta %s no valor de R$ %s",
                source.getAgencyNumber(), source.getAccountNumber(), amount));
        return h;
    }
}