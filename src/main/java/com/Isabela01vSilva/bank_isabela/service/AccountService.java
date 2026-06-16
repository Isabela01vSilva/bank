package com.Isabela01vSilva.bank_isabela.service;

import com.Isabela01vSilva.bank_isabela.commons.Formatters;
import com.Isabela01vSilva.bank_isabela.controller.request.account.*;
import com.Isabela01vSilva.bank_isabela.controller.request.history.RegisterHistoryRequest;
import com.Isabela01vSilva.bank_isabela.controller.response.account.AccountWithCustomerResponse;
import com.Isabela01vSilva.bank_isabela.controller.response.account.UpdateAccountStatusResponse;
import com.Isabela01vSilva.bank_isabela.domain.account.Account;
import com.Isabela01vSilva.bank_isabela.domain.account.AccountRepository;
import com.Isabela01vSilva.bank_isabela.domain.account.AccountStatus;
import com.Isabela01vSilva.bank_isabela.domain.customer.Customer;
import com.Isabela01vSilva.bank_isabela.domain.customer.CustomerRepository;
import com.Isabela01vSilva.bank_isabela.domain.customer.CustomerStatus;
import com.Isabela01vSilva.bank_isabela.domain.historico.HistoryType;
import com.Isabela01vSilva.bank_isabela.domain.mapper.AccountMappers;
import com.Isabela01vSilva.bank_isabela.domain.account.AccountType;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AccountService {

    //
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private HistoryService historyService;

    /*@Autowired
    private TransferenciaRepository transferenciaRepository;*/

    /**
     * Cria múltiplas contas a partir de uma lista de DTOs.
     * Cada conta terá número gerado automaticamente.
     */
    @Transactional
    public List<Account> createMultipleAccounts(List<CreateAccountDTO> accounts) {
        List<Account> newAccounts = accounts.stream()
                .map(this::createAccount)
                .toList();

        List<Account> savedAccounts = accountRepository.saveAll(newAccounts);

        savedAccounts.forEach(this::registerAccountCreatedHistory);
        return savedAccounts;
    }

    private Account createAccount(CreateAccountDTO dto) {
        return AccountMappers.fromRequestToAccount(dto, generateAccountNumber());
    }

    private void registerAccountCreatedHistory(Account account) {
        historyService.register(
                new RegisterHistoryRequest(
                        account,
                        account.getCustomer(),
                        HistoryType.ACCOUNT_CREATED,
                        "Conta Criada com sucesso!",
                        null
                )
        );
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
        validateCpf(cpf);

        String normalizedCpf = Formatters.normalize(cpf);

        List<Account> accounts = findAccountsByCpf(normalizedCpf);

        return accounts.stream()
                .map(AccountMappers::fromAccountToResponse)
                .toList();
    }

    private void validateCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException("CPF não pode ser vazio");
        }
    }

    private List<Account> findAccountsByCpf(String cpf) {
        List<Account> accounts = accountRepository.findByCustomerCpf(cpf);

        if (accounts.isEmpty()) {
            throw new EntityNotFoundException("Nenhuma conta encontrada para o CPF: " + cpf);
        }

        return accounts;
    }

    /**
     * Busca uma conta pelo número da conta e número da agência.
     * Retorna uma lista com a conta encontrada (mantido formato de retorno consistente).
     */
    public AccountWithCustomerResponse searchAccountsByAccountNumberAndAgencyNumber(String accountNumber, String agencyNumber) {

        validateAccountAndAgency(accountNumber, agencyNumber);

        Account account = findByAccountNumberAndAgencyNumber(accountNumber, agencyNumber);

        return AccountMappers.fromAccountToResponse(account);

    }

    private void validateAccountAndAgency(String accountNumber, String agencyNumber) {
        if (accountNumber == null || agencyNumber == null) {
            throw new IllegalArgumentException("Número da conta e agência não podem ser nulos");
        }

        if (accountNumber.isEmpty() || agencyNumber.isEmpty()) {
            throw new EntityNotFoundException("Número da conta e agência são obrigatórios");
        }

        boolean validAccount = accountNumber.matches("\\d{5}-\\d");
        boolean validAgency = agencyNumber.matches("\\d{4}") || agencyNumber.matches("\\d{4}-\\d");

        // Aceita formato conta 12345-6 e agência no formato 1234-1 (porém aceita também 1234)
        if (!validAccount || !validAgency) {
            throw new IllegalArgumentException("Número da conta deve estar no formato 12345-6 e agência no formato 1234 ou 1234-1");
        }
    }

    /**
     * Cria mais uma conta para o cliente identificado pelo CPF seguindo as regras do RF008.
     * - Se já existir conta ATIVA do mesmo tipo -> retorna conflito
     * - Se existir conta ENCERRADO do mesmo tipo -> reativa a conta existente (mantém número e histórico)
     * - Caso contrário cria nova conta com saldo 0, agência 0001, status ATIVO e data de criação
     */
    @Transactional
    public Account createAccountForCpf(String cpf, AccountType requestedType) {
        validateCpf(cpf);

        String normalizedCpf = Formatters.normalize(cpf);

        Customer customer = findCustomerByCpf(normalizedCpf);

        List<Account> accountsOfType = findAccountsByType(normalizedCpf, requestedType);

        validateAccountsWithoutBalance(accountsOfType);

        validateNoActiveAccount(accountsOfType, requestedType);

        Optional<Account> closedAccount = findClosedAccount(accountsOfType);

        if (closedAccount.isPresent()) {
            return reactivateAccount(closedAccount.get(), customer);
        }

        return createNewAccount(customer, requestedType);

    }

    private Account createNewAccount(Customer customer, AccountType accountType) {
        CreateAccountDTO dto = new CreateAccountDTO(accountType, customer);

        Account account = AccountMappers.fromRequestToAccount(dto, generateAccountNumber());
        Account saved = accountRepository.save(account);

        activateCustomer(customer);
        registerAccountCreatedHistory(saved);

        return saved;
    }

    private void activateCustomer(Customer customer) {
        customer.setCustomerStatus(CustomerStatus.ATIVO);
        customerRepository.save(customer);
    }

    private Optional<Account> findClosedAccount(List<Account> accounts) {

        return accounts.stream()
                .filter(account ->
                        account.getAccountStatus() == AccountStatus.ENCERRADO)
                .findFirst();
    }

    private Account reactivateAccount(Account account, Customer customer) {

        account.setAccountStatus(AccountStatus.ATIVO);

        Account saved = accountRepository.save(account);

        reactivateCustomerIfNecessary(customer, saved);
        registerAccountReactivatedHistory(saved);

        return saved;
    }

    private void reactivateCustomerIfNecessary(Customer customer, Account account) {
        if(customer.getCustomerStatus() == CustomerStatus.INATIVO) {
            customer.setCustomerStatus(CustomerStatus.ATIVO);
            customerRepository.save(customer);

            registerCustomerReactivatedHistory(account, customer);
        }
    }

    private List<Account> findAccountsByType(String cpf, AccountType accountType) {
        return accountRepository.findByCustomerCpfAndAccountType(cpf, accountType);
    }

    private void validateNoActiveAccount(List<Account> accounts, AccountType accountType) {
        boolean hasActive = accounts.stream()
                .anyMatch(account -> account.getAccountStatus() == AccountStatus.ATIVO);

        if (hasActive) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Cliente já possui uma conta do tipo solicitado: "  + accountType);
        }
    }

    private void registerAccountReactivatedHistory(Account account) {

        historyService.register(
                new RegisterHistoryRequest(
                        account,
                        account.getCustomer(),
                        HistoryType.ACCOUNT_REACTIVATED,
                        "Conta Reativada com sucesso!",
                        null
                )
        );
    }

    private void registerCustomerReactivatedHistory(Account account, Customer customer) {

        historyService.register(
                new RegisterHistoryRequest(
                        account,
                        customer,
                        HistoryType.CUSTOMER_REACTIVATED,
                        "Cliente reativado por possuir conta ativa.",
                        null
                )
        );
    }

    /**
     * Atualiza o status da conta (ATIVAR ou ENCERRAR) aplicando as regras de negócio:
     * - Não permite encerrar conta com saldo
     * - Não permite encerrar conta com transferências agendadas pendentes
     * - Registra data/motivo da alteração
     */
    @Transactional
    public UpdateAccountStatusResponse updateAccountStatus(UpdateAccountStatusRequest request) {

        Account account = findByAccountNumberAndAgencyNumber(request.accountNumber(), request.agencyNumber());


        if (account.getAccountStatus() == request.accountStatus()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A conta já possui este status");
        }

        if (request.accountStatus() == AccountStatus.ENCERRADO) {

            if (account.getBalance().compareTo(BigDecimal.ZERO) > 0) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Não é possível encerrar conta com saldo");
            }

            historyService.register(
                    new RegisterHistoryRequest(
                            account,
                            account.getCustomer(),
                            HistoryType.ACCOUNT_CLOSED,
                            request.statusChangeReason(),
                            null
                    )
            );

        } else if (request.accountStatus() == AccountStatus.ATIVO) {

            historyService.register(
                    new RegisterHistoryRequest(
                            account,
                            account.getCustomer(),
                            HistoryType.ACCOUNT_REACTIVATED,
                            request.statusChangeReason(),
                            null
                    )
            );
        }

        // Atualiza status, registra data e motivo da alteração
        account.setAccountStatus(request.accountStatus());
        account.setStatusChangeDate(LocalDate.now());
        account.setStatusChangeReason(request.statusChangeReason());

        Account saved = accountRepository.save(account);

        Customer customer = saved.getCustomer();

        boolean hasActiveAccount = accountRepository.existsByCustomerAndAccountStatus(customer, AccountStatus.ATIVO);
        CustomerStatus newCustomrStatus = hasActiveAccount ? CustomerStatus.ATIVO : CustomerStatus.INATIVO;

        if (customer.getCustomerStatus() != newCustomrStatus) {

            customer.setCustomerStatus(newCustomrStatus);

            if (newCustomrStatus == CustomerStatus.ATIVO) {

                historyService.register(
                        new RegisterHistoryRequest(
                                null,
                                customer,
                                HistoryType.CUSTOMER_REACTIVATED,
                                "Cliente reativado por possuir conta ativa",
                                null
                        )
                );

            } else {

                historyService.register(
                        new RegisterHistoryRequest(
                                null,
                                customer,
                                HistoryType.CUSTOMER_INACTIVATED,
                                "Cliente inativado por não possuir contas ativas",
                                null
                        )
                );
            }
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
     * Realiza SAQUE em conta (buscando por número agência + conta)
     */

    public String withdrawal(AccountTransactionRequest request) {
        Account account = findByAccountNumberAndAgencyNumber(request.accountNumber(), request.agencyNumber());
        try {
            validateAndMakeMoneyMovements(request, account);
        } catch (Exception e) {
            historyService.register(
                    new RegisterHistoryRequest(
                            account,
                            account.getCustomer(),
                            HistoryType.WITHDRAWAL_FAILED,
                            e.getMessage(),
                            request.amount()
                    )
            );

            throw e;
        }

        // Retorna uma mensagem indicando o valor sacado.
        return "Valor sacado: R$" + request.amount();
    }

    @Transactional
    private void validateAndMakeMoneyMovements(AccountTransactionRequest request, Account account) {
        // Busca a conta com o ID fornecido na request
        if (account.getAccountStatus() == AccountStatus.ENCERRADO) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A conta está ENCERRADA");
        }

        if (request.amount().compareTo(BigDecimal.ZERO) <= 0 || account.getBalance().compareTo(request.amount()) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo Insuficiente");
        }

        // Realiza saque da conta
        account.setBalance(account.getBalance().subtract(request.amount()));
        //account.withdraw(request.amount());
        accountRepository.save(account);

        historyService.register(
                new RegisterHistoryRequest(
                        account,
                        account.getCustomer(),
                        HistoryType.WITHDRAWAL,
                        "Saque realizado com sucesso!",
                        request.amount()
                )
        );
    }

    /**
     * Realiza DEPOSITO em conta (buscando por número agência + conta)
     */
    @Transactional
    public String deposit(AccountTransactionRequest request) {

        // Busca a conta por numero agencia de conta
        Account account = findByAccountNumberAndAgencyNumber(request.accountNumber(), request.agencyNumber());

        if (account.getAccountStatus() == AccountStatus.ENCERRADO) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A conta está ENCERRADA");
        }

        // Realiza saque da conta
        account.deposit(request.amount());

        accountRepository.save(account);

        historyService.register(
                new RegisterHistoryRequest(
                        account,
                        account.getCustomer(),
                        HistoryType.DEPOSIT,
                        "Depósito realizado com sucesso!",
                        request.amount()
                )
        );

        // Retorna uma mensagem indicando o valor sacado.
        return "Valor depositado: R$" + request.amount();
    }

    /**
     * Consulta saldo da conta pelo id.
     * retorna um objeto (se quiser ja formatar pro front cria uma controler bff backforfrontend)
     */
    public String getBalance(String accountNumber, String agencyNumber) {

        // Busca a conta com o ID fornecido.
        Account conta = findByAccountNumberAndAgencyNumber(accountNumber, agencyNumber);

        // Retorna o saldo da conta e o número da conta.
        return "Saldo R$" + conta.getBalance() + ". agência: " + conta.getAgencyNumber() + " conta:" + conta.getAccountNumber();
    }


    public Account findByAccountNumberAndAgencyNumber(String accountNumber, String agencyNumber) {
        return accountRepository.findByAccountNumberAndAgencyNumber(accountNumber, agencyNumber)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Conta não encontrada"));
    }

    private void validateAccountsWithoutBalance(List<Account> accounts) {
        boolean hasBalance = accounts.stream()
                .anyMatch(account -> account.getBalance().compareTo(BigDecimal.ZERO) == 0);

        if (hasBalance) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cliente possui saldo na conta");
        }
    }

    private Customer findCustomerByCpf(String cpf) {
        return customerRepository.findByCpf(cpf)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }

}