package com.Isabela01vSilva.bank_isabela.domain.account;

import com.Isabela01vSilva.bank_isabela.domain.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNumber(String accountNumber);

    boolean existsByAccountNumber(String accountNumber);

    @Query("SELECT a FROM Conta a WHERE a.customer.cpf = :cpf")
    List<Account> findByCustomerCpf(@Param("cpf") String cpf);

    // Busca contas de um cliente por CPF e tipo de conta
    @Query("SELECT a FROM Conta a WHERE a.customer.cpf = :cpf AND a.accountType = :type")
    List<Account> findByCustomerCpfAndAccountType(@Param("cpf") String cpf, @Param("type") com.Isabela01vSilva.bank_isabela.domain.account.AccountType type);

    // Busca conta de um cliente por CPF, tipo e status (útil para reativação)
    @Query("SELECT a FROM Conta a WHERE a.customer.cpf = :cpf AND a.accountType = :type AND a.accountStatus = :status")
    List<Account> findByCustomerCpfAndAccountTypeAndAccountStatus(@Param("cpf") String cpf, @Param("type") com.Isabela01vSilva.bank_isabela.domain.account.AccountType type, @Param("status") com.Isabela01vSilva.bank_isabela.domain.account.AccountStatus status);

    @Query("SELECT a FROM Conta a WHERE a.accountNumber = :accountNumber AND a.agencyNumber = :agencyNumber")
    Optional<Account> findByAccountNumberAndAgencyNumber(@Param("accountNumber") String accountNumber, @Param("agencyNumber") String agencyNumber);

    boolean existsByCustomerAndAccountStatus(Customer customer,  AccountStatus accountStatus);

}
