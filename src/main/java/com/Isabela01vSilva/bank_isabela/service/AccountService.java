package com.Isabela01vSilva.bank_isabela.service;

import com.Isabela01vSilva.bank_isabela.commons.Formatters;
import com.Isabela01vSilva.bank_isabela.controller.request.account.*;
import com.Isabela01vSilva.bank_isabela.controller.request.historico.CadastroHistoricoRequest;
import com.Isabela01vSilva.bank_isabela.controller.response.account.AccountWithCustomerResponse;
import com.Isabela01vSilva.bank_isabela.domain.account.Account;
import com.Isabela01vSilva.bank_isabela.domain.account.AccountRepository;
import com.Isabela01vSilva.bank_isabela.domain.historico.OperationType;
import com.Isabela01vSilva.bank_isabela.domain.mapper.AccountMappers;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private HistoricoService historicoService;

    @Transactional
    public List<Account> createMultipleAccounts(List<CreateAccountDTO> accounts) {

        List<Account> newAccounts = accounts.stream()
                .map(accountRequest -> AccountMappers.fromRequestToAccount(accountRequest, generateAccountNumber())).toList();

        return accountRepository.saveAll(newAccounts);
    }

    public String generateAccountNumber() {
        do {
            String accountNumber = generateRandomAccountNumber();
            if (!accountRepository.existsByAccountNumber(accountNumber)) {
                return accountNumber;
            }
        } while (true);

    }

    public String generateRandomAccountNumber() {
        Random random = new Random();
        String number = "";
        for (int i = 0; i < 5; i++) {
            number += random.nextInt(10);
        }
        int digitoVerificador = calculateCheckDigit(number);
        return number + "-" + digitoVerificador;
    }

    public int calculateCheckDigit(String number) {
        int sum = 0;
        // Calcula a soma ponderada
        for (int i = 0; i < 5 && i < number.length(); i++) {
            sum += Character.getNumericValue(number.charAt(i)) * i;
        }
        // Calcula o resto da divisão por 11
        int digit = sum % 11;
        // Se o resultado for 10 ou 11, o dígito se torna 0
        return (digit == 10 || digit == 11) ? 0 : digit;
    }


    /**
     * Busca todas as contas vinculadas a um cliente pelo seu CPF.
     * @param cpf O CPF do cliente (pode estar formatado ou não)
     * @return Lista de contas com informações do cliente
     */
    public List<AccountWithCustomerResponse> searchAccountsByCpf(String cpf) {
        if (cpf == null || cpf.isEmpty()) {
            throw new IllegalArgumentException("CPF não pode ser vazio");
        }

        String normalizedCpf = Formatters.normalize(cpf);
        List<Account> accounts = accountRepository.findByCustomerCpf(normalizedCpf);

        if (accounts.isEmpty()) {
            throw new EntityNotFoundException("Nenhuma conta encontrada para o CPF: " + cpf);
        }

        return accounts.stream()
                .map(account -> new AccountWithCustomerResponse(
                        account.getCustomer().getFullName(),
                        account.getCustomer().getCpf(),
                        account.getAgencyNumber(),
                        account.getAccountNumber(),
                        account.getAccountType(),
                        account.getAccountStatus(),
                        account.getBalance(),
                        account.getCreationDate()
                ))
                .toList();
    }

    /**
     * Valida que o tipo de conta não seja alterado (campo imutável).
     * Lança exceção se houver tentativa de alteração.
     */
    public void validateAccountTypeImmutable(Account conta, CreateAccountDTO novosDados) {
        if (novosDados.tipoConta() != null && !novosDados.tipoConta().equals(conta.getAccountType())) {
            throw new IllegalArgumentException("Tipo de conta não pode ser alterado");
        }
    }

    @Transactional
    public Account updateAccountStatus(Long id, UpdateAccountStatusRequest updateAccountStatusRequest) {

        //Busca contas
        Account account = accountRepository.getReferenceById(id);

        //Atualiza o stts da conta
        account.updateAccountStatus(updateAccountStatusRequest.statusConta());

        //Registra a atualização
        historicoService.cadastrar(new CadastroHistoricoRequest(
                account,
                account.getCustomer(),
                OperationType.ATUALIZACAO_STTS_CONTA,
                "Atualizando stts conta para " + updateAccountStatusRequest.statusConta(),
                null
        ));

        //Salva a conta após alteração e retorna a conta atualizada
        return accountRepository.save(account);
    }





    //Movimentações
    @Transactional
    public String deposit(DepositRequest deposit) {

        // Busca a conta com o ID fornecido na request
        Account account = accountRepository.findById(deposit.id())
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));

        // Verifica o stts da conta antes de realizar qualquer operação
        account.accountStatus();

        // Realiza deposito na conta
        account.deposit(deposit.ammount());

        // Registra o histórico da operação de depósito.
        historicoService.cadastrar(
                new CadastroHistoricoRequest(
                        account,
                        account.getCustomer(),
                        OperationType.DEPOSITO,
                        "Operação de deposito realizada",
                        deposit.ammount()
                )
        );

        // Retorna uma mensagem com o valor depositado.
        return "Valor depositado: R$" + deposit.ammount();
    }

    @Transactional
    public String withdrawal(WithdrawalRequest saque) {

        // Busca a conta com o ID fornecido na request
        Account account = accountRepository.findById(saque.id())
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));

        // Verifica o stts da conta antes de realizar qualquer operação
        account.accountStatus();

        // Realiza saque da conta
        account.withdraw(saque.amount());

        // Registra o histórico da operação de depósito.
        historicoService.cadastrar(
                new CadastroHistoricoRequest(
                        account,
                        account.getCustomer(),
                        OperationType.SAQUE,
                        "Operação de saque realizada",
                        saque.amount()
                )
        );

        // Retorna uma mensagem indicando o valor depositado.
        return "Valor sacado: R$" + saque.amount();
    }

    public String getBalance(Long id) {

        // Busca a conta com o ID fornecido.
        Account conta = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));

        // Retorna o saldo da conta e o número da conta.
        return "Saldo R$" + conta.getBalance() + " da conta:" + conta.getAccountNumber();
    }
}
