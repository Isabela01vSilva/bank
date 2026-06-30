package com.Isabela01vSilva.bank_isabela.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.Isabela01vSilva.bank_isabela.controller.request.account.CreateAccountDTO;
import com.Isabela01vSilva.bank_isabela.domain.customer.Customer;
import com.Isabela01vSilva.bank_isabela.domain.account.Account;
import com.Isabela01vSilva.bank_isabela.domain.account.AccountRepository;
import com.Isabela01vSilva.bank_isabela.domain.account.AccountStatus;
import com.Isabela01vSilva.bank_isabela.domain.account.AccountType;
import com.Isabela01vSilva.bank_isabela.service.account.AccountService;
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
    private AccountRepository contaRepository;

    @InjectMocks
    private AccountService contaService;
    private CreateAccountDTO criarContaDTO;
    private Account contaMock;

    @BeforeEach
    void setUp() {

        Customer clienteMock = new Customer();
        clienteMock.setId(1L);
        clienteMock.setFullName("Nome do Cliente");

        criarContaDTO = new CreateAccountDTO("123", AccountType.CORRENTE, clienteMock);
        contaMock = new Account();
        contaMock.setId(1L);
        contaMock.setAccountNumber("12345-6");
        contaMock.setAccountStatus(AccountStatus.ATIVO);
        contaMock.setDataCriacao(LocalDate.now());
    }

    @Test
    void deveCadastrarConta() {
        when(contaRepository.existsByAccountNumber(anyString())).thenReturn(true, false);
        when(contaRepository.save(any(Account.class))).thenReturn(contaMock);

        Account contaCriada = contaService.cadastrar(criarContaDTO);

        assertNotNull(contaCriada);
        verify(contaRepository, atLeastOnce()).existsByAccountNumber(anyString());
        verify(contaRepository).save(any(Account.class));
    }

    @Test
    void deveRetornarContaPorId() {
        when(contaRepository.findById(1L)).thenReturn(Optional.of(contaMock));

        Account contaEncontrada = contaService.exibirContaPorId(1L);

        assertNotNull(contaEncontrada);
        assertEquals(1L, contaEncontrada.getId());
    }

    @Test
    void deveLancarExcecaoSeContaNaoExistir() {
        when(contaRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> contaService.exibirContaPorId(2L));
    }
}
