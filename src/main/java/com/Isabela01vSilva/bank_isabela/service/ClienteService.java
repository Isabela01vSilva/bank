package com.Isabela01vSilva.bank_isabela.service;

import com.Isabela01vSilva.bank_isabela.controller.request.ClienteContaRequest;
import com.Isabela01vSilva.bank_isabela.controller.request.cliente.ClienteRequest;
import com.Isabela01vSilva.bank_isabela.controller.request.conta.CriarContaDTO;
import com.Isabela01vSilva.bank_isabela.domain.cliente.Cliente;
import com.Isabela01vSilva.bank_isabela.domain.cliente.ClienteRepository;
import com.Isabela01vSilva.bank_isabela.domain.conta.Conta;
import com.Isabela01vSilva.bank_isabela.service.dto.ClienteContaDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ContaService contaService;

    @Transactional
    public ClienteContaDTO cadastrar(ClienteContaRequest dados) {
        Cliente cliente = new Cliente();
        cliente.setNome(dados.nome());
        cliente.setCpf(dados.cpf());
        cliente.setEmail(dados.email());
        cliente.setTelefone(dados.telefone());
        Cliente clienteCriado =  clienteRepository.save(cliente);

        Conta contaCriada = contaService.cadastrar(new CriarContaDTO(dados.numeroAgencia(), dados.tipoConta(), clienteCriado));

        return  new ClienteContaDTO(contaCriada, clienteCriado);
    }

    public List<Cliente> exibirTodosOsClients() {
        return clienteRepository.findAll();
    }

    public Cliente exibirClientePorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
    }

    @Transactional
    public Cliente atualizarCliente(Long id, ClienteRequest dados) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        cliente.atualizarInformacoes(dados);
        clienteRepository.save(cliente);

        return cliente;
    }

}
