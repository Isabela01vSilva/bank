package com.Isabela01vSilva.bank_isabela.service;

import com.Isabela01vSilva.bank_isabela.domain.cliente.Cliente;
import com.Isabela01vSilva.bank_isabela.domain.cliente.ClienteRepository;
import com.Isabela01vSilva.bank_isabela.domain.conta.Conta;
import com.Isabela01vSilva.bank_isabela.domain.conta.ContaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    //CRUD
    public Conta cadastrar(Conta dados) {
        Cliente cliente = clienteRepository.findById(dados.getCliente().getId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        dados.setCliente(cliente);
        return contaRepository.save(dados);
    }

    public List<Conta> exibirTodasAsContas() {
        return contaRepository.findAll();
    }

    public Conta exibirContaPorId(Long id) {
        return contaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));
    }

    public Conta atualizarConta(Long id, Conta dados) {
        Conta conta = contaRepository.getReferenceById(id);

        conta.atualizarInformacoes(dados);
        return contaRepository.save(conta);
    }

    public Conta desativarConta(Long id){
        Conta conta = contaRepository.getReferenceById(id);
        conta.desativar();
        return contaRepository.save(conta);
    }

    //

}
