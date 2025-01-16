package com.Isabela01vSilva.bank_isabela.service;

import com.Isabela01vSilva.bank_isabela.controller.request.CadastroHistoricoRequest;
import com.Isabela01vSilva.bank_isabela.controller.response.HistoricoResponse;
import com.Isabela01vSilva.bank_isabela.controller.response.HistoricoSttsContaResponse;
import com.Isabela01vSilva.bank_isabela.domain.conta.ContaRepository;
import com.Isabela01vSilva.bank_isabela.domain.historico.Historico;
import com.Isabela01vSilva.bank_isabela.domain.historico.HistoricoRepository;
import com.Isabela01vSilva.bank_isabela.domain.historico.TipoOperacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@Service
public class HistoricoService {

    @Autowired
    private HistoricoRepository historicoRepository;

    @Autowired
    private ContaRepository contaRepository;

    public Historico cadastrar(CadastroHistoricoRequest dados) {
        Historico historico = new Historico();
        historico.setConta(dados.conta());
        historico.setCliente(dados.cliente());
        historico.setTipoOperacao(dados.tipoOperacao());
        historico.setDescricao(dados.descricao());
        historico.setValor(dados.valor());
        historico.setDataTransicao(LocalDate.now());

        return historicoRepository.save(historico);
    }

    public List<Historico> exibirTodosHistoricos() {
        return historicoRepository.findAll();
    }

    public List<HistoricoResponse> exibirHistoricoPorCliente(Long id) {
        List<Historico> historico = historicoRepository.findByClienteId(id);
        return historico.stream().map(historico1 -> new HistoricoResponse(
                historico1.getId(), historico1.getValor(), historico1.getDescricao(), historico1.getDataTransicao()
        )).toList();
    }

    public List<HistoricoResponse> exibirHistoricoPorConta(Long id) {
        List<Historico> historico = historicoRepository.findByContaId(id);
        return historico.stream().map(historico1 -> new HistoricoResponse(
                historico1.getId(), historico1.getValor(), historico1.getDescricao(), historico1.getDataTransicao()
        )).toList();
    }

    public List<HistoricoSttsContaResponse> exibirHistoricoStts(Long id) {
        List<Historico> historico = historicoRepository.findByContaId(id);

        return historico.stream()
                .filter(historicos -> historicos.getTipoOperacao().equals(TipoOperacao.ATUALIZACAO_STTS_CONTA))
                .map(historicos -> new HistoricoSttsContaResponse(
                        historicos.getId(),
                        historicos.getTipoOperacao(),
                        historicos.getDescricao()
                )).toList();
    }

}
