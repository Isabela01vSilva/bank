package com.Isabela01vSilva.bank_isabela.service;

import com.Isabela01vSilva.bank_isabela.controller.request.historico.CadastroHistoricoRequest;
import com.Isabela01vSilva.bank_isabela.controller.request.historico.HistoricoEntreDatasResquest;
import com.Isabela01vSilva.bank_isabela.controller.response.historico.HistoricoResponse;
import com.Isabela01vSilva.bank_isabela.controller.response.historico.HistoricoSttsContaResponse;
import com.Isabela01vSilva.bank_isabela.domain.historico.Historico;
import com.Isabela01vSilva.bank_isabela.domain.historico.HistoricoRepository;
import com.Isabela01vSilva.bank_isabela.domain.historico.TipoOperacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HistoricoService {

    @Autowired
    private HistoricoRepository historicoRepository;

    public Historico cadastrar(CadastroHistoricoRequest dados) {

        // Criação de uma nova instância de Historico
        Historico historico = new Historico();

        // Atribui os dados do historico para seu cadastro
        historico.setConta(dados.conta());
        historico.setCliente(dados.cliente());
        historico.setTipoOperacao(dados.tipoOperacao());
        historico.setDescricao(dados.descricao());
        historico.setValor(dados.valor());
        historico.setDataTransacao(LocalDate.now());

        // Salva o histórico no banco de dados e retorna a entidade salva
        return historicoRepository.save(historico);
    }

    public List<Historico> exibirTodosHistoricos() {
        // Retorna todos os históricos
        return historicoRepository.findAll();
    }

    public List<HistoricoResponse> exibirHistoricoPorCliente(Long id) {
        // Busca os históricos relacionados a um cliente pelo id
        List<Historico> historico = historicoRepository.findByClienteId(id);
        return historico.stream().map(historico1 -> new HistoricoResponse(
                historico1.getId(), historico1.getCliente().getNome(), historico1.getValor(), historico1.getDescricao(), historico1.getDataTransacao()
        )).toList(); // Converte os históricos para uma lista de HistoricoResponse e retorna
    }

    public List<HistoricoResponse> exibirHistoricoPorConta(Long id) {
        // Busca os históricos relacionados a um conta pelo id
        List<Historico> historico = historicoRepository.findByContaId(id);
        return historico.stream().map(historico1 -> new HistoricoResponse(
                historico1.getId(), historico1.getCliente().getNome(), historico1.getValor(), historico1.getDescricao(), historico1.getDataTransacao()
        )).toList(); // Converte os históricos para uma lista de HistoricoResponse e retorna
    }

    public List<HistoricoSttsContaResponse> exibirHistoricoStts(Long id) {

        // Busca os históricos relacionados a uma conta
        List<Historico> historico = historicoRepository.findByContaId(id);

        return historico.stream()
                .filter(historicos -> historicos.getTipoOperacao().equals(TipoOperacao.ATUALIZACAO_STTS_CONTA)) // Filtra apenas os históricos com operação de atualização de status de conta
                .map(historicos -> new HistoricoSttsContaResponse(
                        historicos.getId(),
                        historicos.getTipoOperacao(),
                        historicos.getDescricao()
                )).toList(); // Converte os históricos filtrados em HistoricoSttsContaResponse e retorna
    }

    public List<HistoricoResponse> exibirHistoricoEntreDatas(HistoricoEntreDatasResquest datasResquest) {

        // Busca os históricos de uma conta entre duas datas
        List<Historico> historicos = historicoRepository.findByContaIdAndDataTransacaoBetween(datasResquest.id(), datasResquest.dataInicio(), datasResquest.dataFim());

        return historicos.stream()
                .map(historico -> new HistoricoResponse(
                        historico.getId(),
                        historico.getCliente().getNome(),
                        historico.getValor(),
                        historico.getDescricao(),
                        historico.getDataTransacao()
                )).toList(); // Converte os históricos encontrados em HistoricoResponse e retorna
    }

    public List<HistoricoResponse> exibirExtrato(HistoricoEntreDatasResquest datasResquest) {

        // Busca históricos entre datas
        List<Historico> historico = historicoRepository.findByContaIdAndDataTransacaoBetween(datasResquest.id(), datasResquest.dataInicio(), datasResquest.dataFim());

        return historico.stream()
                .filter(historicos -> !historicos.getTipoOperacao().equals(TipoOperacao.ATUALIZACAO_STTS_CONTA)) // Filtra para excluir operações de atualização de status da conta
                .map(historicos -> new HistoricoResponse(
                        historicos.getId(),
                        historicos.getCliente().getNome(),
                        historicos.getValor(),
                        historicos.getDescricao(),
                        historicos.getDataTransacao()
                )).toList(); // Converte e retorna os históricos filtrados
    }

    public String calculoGastosPorPeriodo(HistoricoEntreDatasResquest gastosRequest) {
        List<Historico> historico = historicoRepository.findByContaIdAndDataTransacaoBetween(
                gastosRequest.id(),
                gastosRequest.dataInicio(),
                gastosRequest.dataFim()
        ); // Busca os históricos da conta no intervalo de datas especificado

        List<Double> numeros = historico.stream()
                .filter(h -> h.getTipoOperacao().equals(TipoOperacao.SAQUE) || h.getTipoOperacao().equals(TipoOperacao.PIX)) // Filtra apenas saques ou operações de PIX
                .map(Historico::getValor) // Mapeia os valores das operações
                .toList(); // Converte para uma lista de valores

        Double calculado = numeros.stream().reduce(0.0, Double::sum); // Soma todos os valores dos saques e pix

        return String.format("Total de gastos: R$ %.2f da conta de id: %d no período de %s até %s",
                calculado,
                gastosRequest.id(),
                gastosRequest.dataInicio(),
                gastosRequest.dataFim()); // Retorna uma string formatada com o total dos gastos
    }
}