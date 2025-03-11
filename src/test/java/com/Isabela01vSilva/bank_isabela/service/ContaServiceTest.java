package com.Isabela01vSilva.bank_isabela.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.Isabela01vSilva.bank_isabela.controller.request.conta.CriarContaDTO;
import com.Isabela01vSilva.bank_isabela.domain.cliente.Cliente;
import com.Isabela01vSilva.bank_isabela.domain.conta.Conta;
import com.Isabela01vSilva.bank_isabela.domain.conta.ContaRepository;
import com.Isabela01vSilva.bank_isabela.domain.conta.StatusConta;
import com.Isabela01vSilva.bank_isabela.domain.conta.TipoConta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class ContaServiceTest {


    @Mock
    private ContaRepository contaRepository;

    @InjectMocks
    private ContaService contaService;
    private CriarContaDTO criarContaDTO;
    private Conta contaMock;

    @BeforeEach
    void setUp() {

        Cliente clienteMock = new Cliente();
        clienteMock.setId(1L);
        clienteMock.setNome("Nome do Cliente");

        criarContaDTO = new CriarContaDTO("123", TipoConta.CORRENTE, clienteMock);
        contaMock = new Conta();
        contaMock.setId(1L);
        contaMock.setNumero("12345-6");
        contaMock.setStatusConta(StatusConta.ATIVADA);
        contaMock.setDataCriacao(LocalDate.now());
    }

    @Test
    void deveCadastrarConta() {
        when(contaRepository.existsByNumero(anyString())).thenReturn(true, false);
        when(contaRepository.save(any(Conta.class))).thenReturn(contaMock);

        Conta contaCriada = contaService.cadastrar(criarContaDTO);

        assertNotNull(contaCriada);
        verify(contaRepository, atLeastOnce()).existsByNumero(anyString());
        verify(contaRepository).save(any(Conta.class));
    }

    @Test
    void deveRetornarContaPorId() {
        when(contaRepository.findById(1L)).thenReturn(Optional.of(contaMock));

        Conta contaEncontrada = contaService.exibirContaPorId(1L);

        assertNotNull(contaEncontrada);
        assertEquals(1L, contaEncontrada.getId());
    }

    @Test
    void deveLancarExcecaoSeContaNaoExistir() {
        when(contaRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> contaService.exibirContaPorId(2L));
    }
}
