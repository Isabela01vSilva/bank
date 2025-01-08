package com.Isabela01vSilva.bank_isabela.service;

import com.Isabela01vSilva.bank_isabela.domain.cliente.Cliente;
import com.Isabela01vSilva.bank_isabela.domain.cliente.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente cadastrar(Cliente dados) {
        return clienteRepository.save(dados);
    }

    public List<Cliente> exibirTodosOsClients() {
        return clienteRepository.findAll();
    }

    public Cliente exibirClientePorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
    }

    public Cliente atualizarCliente(Long id, Cliente dados) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        cliente.atualizarInformacoes(dados);
        clienteRepository.save(cliente);

        return cliente;
    }

    public void excluirCliente(Long id) {
         clienteRepository.deleteById(id);
    }

}
