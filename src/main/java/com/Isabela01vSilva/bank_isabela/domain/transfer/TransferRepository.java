package com.Isabela01vSilva.bank_isabela.domain.transfer;

import com.Isabela01vSilva.bank_isabela.domain.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransferRepository extends JpaRepository<Transfer, Long> {


}
