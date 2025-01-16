package com.Isabela01vSilva.bank_isabela.domain.historico;

import com.Isabela01vSilva.bank_isabela.domain.conta.Conta;
import com.Isabela01vSilva.bank_isabela.domain.conta.StatusConta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoricoRepository extends JpaRepository<Historico, Long> {

    List<Historico> findByClienteId(Long cliente);

    List<Historico> findByContaId(Long conta);

}
