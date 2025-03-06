package com.Isabela01vSilva.bank_isabela.controller;

import com.Isabela01vSilva.bank_isabela.controller.request.cliente.ClienteRequest;
import com.Isabela01vSilva.bank_isabela.controller.response.cliente.ClienteResponse;
import com.Isabela01vSilva.bank_isabela.domain.cliente.Cliente;
import com.Isabela01vSilva.bank_isabela.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteResponse> cadastrarCliente(@Valid @RequestBody ClienteRequest dados) {
        Cliente novoCliente = clienteService.cadastrar(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ClienteResponse(novoCliente.getId(), novoCliente.getNome(), novoCliente.getCpf(), novoCliente.getEmail(), novoCliente.getTelefone()));
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listarTodosOsClientes() {
        List<Cliente> listar = clienteService.exibirTodosOsClients();
        return ResponseEntity.ok(listar.stream().map( cliente -> new ClienteResponse(cliente.getId(), cliente.getNome(), cliente.getCpf(), cliente.getEmail(), cliente.getTelefone())).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscarClientePorId(@PathVariable Long id) {
        Cliente buscarCliente = clienteService.exibirClientePorId(id);
        return ResponseEntity.ok(new ClienteResponse(buscarCliente.getId(), buscarCliente.getNome(), buscarCliente.getCpf(), buscarCliente.getEmail(), buscarCliente.getTelefone()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> atualizarCliente(@Valid @PathVariable Long id, @RequestBody ClienteRequest dados) {
        Cliente atualizar = clienteService.atualizarCliente(id, dados);
        return ResponseEntity.ok(new ClienteResponse(atualizar.getId(), atualizar.getNome(), atualizar.getCpf(), atualizar.getEmail(), atualizar.getTelefone()));
    }

}