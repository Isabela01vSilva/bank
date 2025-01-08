package com.Isabela01vSilva.bank_isabela.controller;

import com.Isabela01vSilva.bank_isabela.domain.cliente.Cliente;
import com.Isabela01vSilva.bank_isabela.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    @Transactional
    public ResponseEntity<Cliente> cadastrarCliente(@RequestBody Cliente dados) {
        Cliente novoCliente = clienteService.cadastrar(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listarTodosOsClientes() {
        List<Cliente> listar = clienteService.exibirTodosOsClients();
        return ResponseEntity.ok(listar);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> listarClientePorId(@PathVariable Long id) {
        Cliente buscarCliente = clienteService.exibirClientePorId(id);
        return ResponseEntity.ok(buscarCliente);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Cliente> atualizarCliente(@PathVariable Long id, @RequestBody Cliente dados) {
        Cliente atualizar = clienteService.atualizarCliente(id, dados);
        return ResponseEntity.ok(atualizar);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirCliente(@PathVariable Long id) {
        clienteService.excluirCliente(id);
        return ResponseEntity.noContent().build();
    }
}
