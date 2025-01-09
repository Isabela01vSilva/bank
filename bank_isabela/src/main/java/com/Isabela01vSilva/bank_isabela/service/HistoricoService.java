package com.Isabela01vSilva.bank_isabela.service;

import com.Isabela01vSilva.bank_isabela.domain.historico.Historico;
import com.Isabela01vSilva.bank_isabela.domain.historico.HistoricoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoricoService {

    @Autowired
    private HistoricoRepository historicoRepository;

    public Historico cadastrar(Historico dados) {
        return historicoRepository.save(dados);
    }

    public List<Historico> exibirTodosHistoricos() {
        return historicoRepository.findAll();
    }

    public Historico exibirHistoricoPorId(Long id) {
        return historicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Não há historico"));
    }

}
