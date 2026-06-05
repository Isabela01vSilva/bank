package com.Isabela01vSilva.bank_isabela.domain.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
	boolean existsByCpf(String cpf);
	boolean existsByEmail(String email);
	boolean existsByPhoneNumber(String phoneNumber);

	Optional<Customer> findByCpf(String cpf);
}
