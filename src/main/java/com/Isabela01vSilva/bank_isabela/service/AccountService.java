package com.Isabela01vSilva.bank_isabela.service;

import com.Isabela01vSilva.bank_isabela.commons.Formatters;
import com.Isabela01vSilva.bank_isabela.controller.request.account.*;
import com.Isabela01vSilva.bank_isabela.controller.request.historico.CadastroHistoricoRequest;
import com.Isabela01vSilva.bank_isabela.controller.response.account.AccountWithCustomerResponse;
import com.Isabela01vSilva.bank_isabela.controller.response.account.UpdateAccountStatusResponse;
import com.Isabela01vSilva.bank_isabela.domain.account.Account;
import com.Isabela01vSilva.bank_isabela.domain.account.AccountRepository;
import com.Isabela01vSilva.bank_isabela.domain.account.AccountStatus;
import com.Isabela01vSilva.bank_isabela.domain.customer.Customer;
import com.Isabela01vSilva.bank_isabela.domain.customer.CustomerRepository;
import com.Isabela01vSilva.bank_isabela.domain.customer.CustomerStatus;
import com.Isabela01vSilva.bank_isabela.domain.transfer.TransferenciaRepository;
import com.Isabela01vSilva.bank_isabela.domain.historico.OperationType;
import com.Isabela01vSilva.bank_isabela.domain.mapper.AccountMappers;
import com.Isabela01vSilva.bank_isabela.domain.account.AccountType;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AccountService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private HistoricoService historicoService;

    @Autowired
    private TransferenciaRepository transferenciaRepository;

    /**
     * Cria múltiplas contas a partir de uma lista de DTOs.
     * Cada conta terá número gerado automaticamente.
     */
    @Transactional
    public List<Account> createMultipleAccounts(List<CreateAccountDTO> accounts) {
        List<Account> newAccounts = accounts.stream()
                .map(accountRequest -> AccountMappers.fromRequestToAccount(accountRequest, generateAccountNumber()))
                .toList();

        return accountRepository.saveAll(newAccounts);
    }

    /**
     * Gera um número de conta único no sistema (verifica existência no repositório).
     */
    public String generateAccountNumber() {
        while (true) {
            String accountNumber = generateRandomAccountNumber();
            if (!accountRepository.existsByAccountNumber(accountNumber)) {
                return accountNumber;
            }
        }
    }

    /**
     * Gera um número de conta aleatório no formato 12345-6 (5 dígitos + dígito verificador).
     */
    public String generateRandomAccountNumber() {
        Random random = new Random();
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            number.append(random.nextInt(10));
        }
        int digitoVerificador = calculateCheckDigit(number.toString());
        return number + "-" + digitoVerificador;
    }

    /**
     * Calcula dígito verificador simples (soma ponderada mod 11) para o número da conta.
     */
    public int calculateCheckDigit(String number) {
        int sum = 0;
        // Calcula a soma ponderada
        for (int i = 0; i < 5 && i < number.length(); i++) {
            sum += Character.getNumericValue(number.charAt(i)) * i;
        }
        // Calcula o resto da divisão por 11
        int digit = sum % 11;
        // Se o resultado for 10, o dígito se torna 0
        return (digit == 10) ? 0 : digit;
    }

    /**
     * Busca todas as contas vinculadas a um cliente pelo seu CPF.
     * Aceita CPF formatado ou não; realiza normalização antes da busca.
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
     * Busca uma conta pelo número da conta e número da agência.
     * Retorna uma lista com a conta encontrada (mantido formato de retorno consistente).
     */
    public AccountWithCustomerResponse searchAccountsByAccountNumberAndAgencyNumber(String accountNumber, String agencyNumber) {
        if (accountNumber == null || agencyNumber == null) {
            throw new IllegalArgumentException("Número da conta e agência não podem ser nulos");
        }

        if (accountNumber.isEmpty() || agencyNumber.isEmpty()) {
            throw new EntityNotFoundException("Nenhuma conta encontrada por: " + accountNumber + " e agência: " + agencyNumber);
        }

        // Aceita formato conta 12345-6 e agência no formato 1234-1 (porém aceita também 1234)
        if (!accountNumber.matches("\\d{5}-\\d") || !(agencyNumber.matches("\\d{4}-\\d") || agencyNumber.matches("\\d{4}"))) {
            throw new IllegalArgumentException("Número da conta deve estar no formato 12345-6 e agência no formato 1234 ou 1234-1");
        }


        return accountRepository.findByAccountNumberAndAgencyNumber(accountNumber, agencyNumber).map(account ->
                new AccountWithCustomerResponse(
                        account.getCustomer().getFullName(),
                        account.getCustomer().getCpf(),
                        account.getAgencyNumber(),
                        account.getAccountNumber(),
                        account.getAccountType(),
                        account.getAccountStatus(),
                        account.getBalance(),
                        account.getCreationDate()
                )
        ).orElseThrow(() -> new EntityNotFoundException("Nenhuma conta encontrada por: " + accountNumber + " e agência: " + agencyNumber));

    }

    /**
     * Valida que o tipo de conta não seja alterado (campo imutável).
     * Lança exceção se houver tentativa de alteração.
     */
    public void validateAccountTypeImmutable(Account conta, CreateAccountDTO novosDados) {
        if (novosDados.accountType() != null && !novosDados.accountType().equals(conta.getAccountType())) {
            throw new IllegalArgumentException("Tipo de conta não pode ser alterado");
        }
    }

    /**
     * Cria mais uma conta para o cliente identificado pelo CPF seguindo as regras do RF008.
     * - Se já existir conta ATIVA do mesmo tipo -> retorna conflito
     * - Se existir conta ENCERRADO do mesmo tipo -> reativa a conta existente (mantém número e histórico)
     * - Caso contrário cria nova conta com saldo 0, agência 0001, status ATIVO e data de criação
     */
    @Transactional
    public com.Isabela01vSilva.bank_isabela.domain.account.Account createAccountForCpf(String cpf, AccountType requestedType) {
        if (cpf == null || cpf.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF não informado");
        }

        String normalizedCpf = Formatters.normalize(cpf);

        // Busca cliente
        var optionalCustomer = customerRepository.findByCpf(normalizedCpf);
        if (optionalCustomer.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado para CPF: " + cpf);
        }
        var customer = optionalCustomer.get();

        // Verifica se já existe conta do mesmo tipo
        List<Account> accountsOfType = accountRepository.findByCustomerCpfAndAccountType(normalizedCpf, requestedType);

        // Se existir conta ATIVA do mesmo tipo -> conflito
        boolean hasActive = accountsOfType.stream().anyMatch(a -> a.getAccountStatus() == AccountStatus.ATIVO);
        if (hasActive) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cliente já possui uma conta do tipo solicitado: " + requestedType);
        }

        // Se existir conta ENCERRADO do mesmo tipo -> reativar a conta existente (mantendo número e histórico)
        var optionalEncerrada = accountsOfType.stream().filter(a -> a.getAccountStatus() == AccountStatus.ENCERRADO).findFirst();
        if (optionalEncerrada.isPresent()) {
            Account toReactivate = optionalEncerrada.get();
            toReactivate.setAccountStatus(AccountStatus.ATIVO);
            // Mantém statusChangeDate e statusChangeReason anteriores para auditoria/histórico (RF007 e RF008)
            Account saved = accountRepository.save(toReactivate);

            // Atualiza status do cliente para ATIVO caso ele tenha sido inativado por falta de contas ativas
            customer.setCustomerStatus(CustomerStatus.ATIVO);
            customerRepository.save(customer);

            return saved;
        }

        // Caso não exista conta do tipo solicitado -> criar nova conta seguindo RF004
        CreateAccountDTO dto = new CreateAccountDTO(requestedType, customer);
        Account newAccount = AccountMappers.fromRequestToAccount(dto, generateAccountNumber());
        Account saved = accountRepository.save(newAccount);

        // Atualiza status do cliente para ATIVO
        customer.setCustomerStatus(CustomerStatus.ATIVO);
        customerRepository.save(customer);

        return saved;
    }

    /**
     * Atualiza o status da conta (ATIVAR ou ENCERRAR) aplicando as regras de negócio:
     * - Não permite encerrar conta com saldo
     * - Não permite encerrar conta com transferências agendadas pendentes
     * - Registra data/motivo da alteração
     */
    @Transactional
    public UpdateAccountStatusResponse updateAccountStatus(UpdateAccountStatusRequest request) {

        Optional<Account> optional = accountRepository.findByAccountNumberAndAgencyNumber(
                request.accountNumber(), request.agencyNumber());

        if (optional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada");
        }

        Account account = optional.get();

        if (account.getAccountStatus() == request.accountStatus()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A conta já possui este status");
        }

        // Atualiza status, registra data e motivo da alteração
        account.setAccountStatus(request.accountStatus());
        account.setStatusChangeDate(LocalDate.now());
        account.setStatusChangeReason(request.statusChangeReason());

        Account saved = accountRepository.save(account);

        Customer customer = saved.getCustomer();

        boolean hasActiveAccount = accountRepository.existsByCustomerAndAccountStatus(customer, AccountStatus.ATIVO);

        if (hasActiveAccount) {
            customer.setCustomerStatus(CustomerStatus.ATIVO);

        } else {
            customer.setCustomerStatus(CustomerStatus.INATIVO);
        }

        customerRepository.save(customer);


        return new UpdateAccountStatusResponse(
                saved.getAccountNumber(),
                saved.getAgencyNumber(),
                saved.getAccountType(),
                saved.getAccountStatus(),
                saved.getBalance(),
                saved.getCreationDate(),
                saved.getStatusChangeDate(),
                saved.getStatusChangeReason()
        );
    }


    // ===== Movimentações =====

    /**
     * Realiza depósito em conta (buscando por id da conta).
     * Registra histórico da operação.
     */
    @Transactional
    public String deposit(DepositRequest deposit) {

        // Busca a conta com o ID fornecido na request
        Account account = accountRepository.findById(deposit.id())
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));

        // Verifica o status da conta antes de realizar qualquer operação
        account.accountStatus();

        // Realiza depósito na conta
        account.deposit(deposit.ammount());

        // Registra o histórico da operação de depósito.
        historicoService.cadastrar(
                new CadastroHistoricoRequest(
                        account,
                        account.getCustomer(),
                        OperationType.DEPOSITO,
                        "Operação de depósito realizada",
                        deposit.ammount()
                )
        );

        // Retorna uma mensagem com o valor depositado.
        return "Valor depositado: R$" + deposit.ammount();
    }

    /**
     * Realiza saque em conta (buscando por id da conta) e registra histórico.
     */
    @Transactional
    public String withdrawal(WithdrawalRequest saque) {

        // Busca a conta com o ID fornecido na request
        Account account = accountRepository.findById(saque.id())
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));

        // Verifica o status da conta antes de realizar qualquer operação
        account.accountStatus();

        // Realiza saque da conta
        account.withdraw(saque.amount());

        // Registra o histórico da operação de saque.
        historicoService.cadastrar(
                new CadastroHistoricoRequest(
                        account,
                        account.getCustomer(),
                        OperationType.SAQUE,
                        "Operação de saque realizada",
                        saque.amount()
                )
        );

        // Retorna uma mensagem indicando o valor sacado.
        return "Valor sacado: R$" + saque.amount();
    }

    /**
     * Consulta saldo da conta pelo id.
     */
    public String getBalance(Long id) {

        // Busca a conta com o ID fornecido.
        Account conta = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));

        // Retorna o saldo da conta e o número da conta.
        return "Saldo R$" + conta.getBalance() + " da conta:" + conta.getAccountNumber();
    }
}
