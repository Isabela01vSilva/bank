package com.Isabela01vSilva.bank_isabela.domain.historico;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HistoricoRepository extends JpaRepository<Historico, Long> {

    List<Historico> findByClienteId(Long cliente);

    List<Historico> findByContaId(Long conta);

    List<Historico> findByContaIdAndDataTransacaoBetween(Long id, LocalDate dataInicio, LocalDate dataFim);


}
