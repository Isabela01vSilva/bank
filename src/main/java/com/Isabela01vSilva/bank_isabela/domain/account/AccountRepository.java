package com.Isabela01vSilva.bank_isabela.domain.account;

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

    @Query("SELECT a FROM Conta a WHERE a.accountNumber = :accountNumber AND a.agencyNumber = :agencyNumber")
    Optional<Account> findByAccountNumberAndAgencyNumber(@Param("accountNumber") String accountNumber, @Param("agencyNumber") String agencyNumber);

    List<Account> findByCustomerId(Long customerId);
}
