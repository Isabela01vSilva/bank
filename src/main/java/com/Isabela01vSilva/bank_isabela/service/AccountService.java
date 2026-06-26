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
import com.Isabela01vSilva.bank_isabela.mapper.AccountMappers;
import com.Isabela01vSilva.bank_isabela.domain.account.AccountType;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
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
    private HistoryService historyService;


    // ==================== MÉTODOS PÚBLICOS ====================

    /**
     * Realiza um saque da conta.
     * Valida a conta e o valor, executa a operação e registra o histórico.
     * Em caso de erro, registra a falha e relança a exceção.
     *
     * @param request dados da transação (número da conta, agência e valor)
     * @return mensagem confirmando o valor sacado
     * @throws ResponseStatusException se conta não encontrada, encerrada ou saldo insuficiente
     */
    @Transactional
    public String withdrawal(AccountTransactionRequest request) {
        Account account = findByAccountNumberAndAgencyNumber(request.accountNumber(), request.agencyNumber());
        try {
            validatWithdrawal(account, request.amount());
            performWithdrawal(account, request.amount());
            registerWithdrawalHistory(account, request.amount());

        } catch (Exception e) {
            registerWithdrawalFailedHistory(account, request.amount(), e.getMessage());
            throw e;
        }

        return "Valor sacado: R$" + request.amount();
    }

    /**
     * Realiza um depósito na conta.
     * Valida a conta e o valor, executa a operação e registra o histórico.
     * Em caso de erro, registra a falha e relança a exceção.
     *
     * @param request dados da transação (número da conta, agência e valor)
     * @return mensagem confirmando o valor depositado
     * @throws ResponseStatusException se conta não encontrada ou encerrada
     */
    @Transactional
    public String deposit(AccountTransactionRequest request) {
        Account account = findByAccountNumberAndAgencyNumber(request.accountNumber(), request.agencyNumber());
        try {
            validateDeposit(account, request.amount());
            performDeposit(account, request.amount());
            registerDepositHistory(account, request.amount());

        } catch (Exception e) {
            registerDepositFailedHistory(account, request.amount(), e.getMessage());
            throw e;
        }

        return "Valor Depositado: R$" + request.amount();
    }

    /**
     * Cria uma nova conta para um cliente ou reativa uma existente.
     * Segue a regra RF008:
     * - Se existe conta ativa do tipo solicitado: lança erro de conflito
     * - Se existe conta encerrada do tipo solicitado: reativa a conta existente
     * - Caso contrário: cria uma nova conta, do tipo solicitado, com saldo zero
     *
     * @param cpf           CPF do cliente (formatado ou não)
     * @param requestedType tipo de conta desejada
     * @return conta criada ou reativada
     * @throws IllegalArgumentException se CPF é nulo ou vazio
     * @throws ResponseStatusException  se cliente não encontrado ou conflito de contas
     */
    @Transactional
    public Account createAccountForCpf(String cpf, AccountType requestedType) {
        validateCpf(cpf);

        String normalizedCpf = Formatters.normalize(cpf);

        Customer customer = findCustomerByCpf(normalizedCpf);

        List<Account> accountsOfType = findAccountsByType(normalizedCpf, requestedType);

        validateNoActiveAccount(accountsOfType, requestedType);

        Optional<Account> closedAccount = findClosedAccount(accountsOfType);

        if (closedAccount.isPresent()) {
            return reactivateAccount(closedAccount.get(), customer);
        }

        return createNewAccount(customer, requestedType);

    }

    /**
     * Cria múltiplas contas a partir de uma lista de DTOs.
     * Gera número único para cada conta e registra histórico de criação.
     *
     * @param accounts lista de DTOs com dados das contas
     * @return lista de contas criadas e persistidas
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

    /**
     * Atualiza o status da conta (ativo para encerrado ou vice-versa).
     * Valida a mudança de status, registra histórico e atualiza status do cliente.
     *
     * @param request dados com número da conta, agência e novo status
     * @return resposta com a conta atualizada
     * @throws ResponseStatusException se conta já possui o status ou há saldo para encerramento
     */
    @Transactional
    public UpdateAccountStatusResponse updateAccountStatus(UpdateAccountStatusRequest request) {

        Account account = findByAccountNumberAndAgencyNumber(request.accountNumber(), request.agencyNumber());

        validateStatusChange(account, request);
        registerAccountStatusHistory(account, request);

        Account saved = updateAccountsStatus(account, request);

        updateCustomerStatus(saved.getCustomer());

        return AccountMappers.toUpdateAccountStatusResponse(saved);
    }

    /**
     * Busca todas as contas de um cliente pelo CPF.
     * Normaliza o CPF antes da busca e converte para resposta com dados do cliente.
     *
     * @param cpf CPF do cliente (formatado ou não)
     * @return lista de contas com dados do cliente
     * @throws IllegalArgumentException se CPF é nulo ou vazio
     * @throws EntityNotFoundException  se nenhuma conta encontrada
     */
    public List<AccountWithCustomerResponse> searchAccountsByCpf(String cpf) {
        validateCpf(cpf);

        String normalizedCpf = Formatters.normalize(cpf);

        List<Account> accounts = findAccountsByCpf(normalizedCpf);

        return accounts.stream()
                .map(AccountMappers::fromAccountToResponse)
                .toList();
    }

    /**
     * Busca uma conta específica pelo número e agência.
     * Valida formato dos parâmetros, busca a conta e converte para resposta com dados do cliente.
     *
     * @param accountNumber número da conta (formato: 12345-6)
     * @param agencyNumber  número da agência (formato: 1234 ou 1234-1)
     * @return conta encontrada com dados do cliente
     * @throws IllegalArgumentException se parâmetros nulos ou formato inválido
     * @throws ResponseStatusException  se conta não encontrada
     */
    public AccountWithCustomerResponse searchAccountsByAccountNumberAndAgencyNumber(String accountNumber, String agencyNumber) {

        validateAccountAndAgency(accountNumber, agencyNumber);

        Account account = findByAccountNumberAndAgencyNumber(accountNumber, agencyNumber);

        return AccountMappers.fromAccountToResponse(account);

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


    // ==================== BUSCA DE ENTIDADES ====================

    /**
     * Busca uma conta no repositório pelo número e agência.
     * Metodo auxiliar usado internamente para obter a entidade Account.
     *
     * @param accountNumber número da conta
     * @param agencyNumber  número da agência
     * @return conta encontrada
     * @throws ResponseStatusException se conta não encontrada
     */
    private Account findByAccountNumberAndAgencyNumber(String accountNumber, String agencyNumber) {
        return accountRepository.findByAccountNumberAndAgencyNumber(accountNumber, agencyNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));
    }

    /**
     * Busca um cliente no repositório pelo CPF normalizado.
     * Metodo auxiliar para obter customer por CPF.
     *
     * @param cpf CPF normalizado (sem formatação)
     * @return cliente encontrado
     * @throws ResponseStatusException se cliente não encontrado
     */
    private Customer findCustomerByCpf(String cpf) {
        return customerRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }

    /**
     * Busca todas as contas de um cliente com um tipo específico.
     * Utilizado para verificar se existe conta do mesmo tipo.
     *
     * @param cpf         CPF do cliente normalizado
     * @param accountType tipo de conta (ex: CORRENTE, POUPANÇA)
     * @return lista de contas encontradas (pode estar vazia)
     */
    private List<Account> findAccountsByType(String cpf, AccountType accountType) {
        return accountRepository.findByCustomerCpfAndAccountType(cpf, accountType);
    }

    /**
     * Busca a primeira conta com status ENCERRADO na lista.
     * Utilizado para verificar se existe conta fechada para reativação.
     *
     * @param accounts lista de contas
     * @return Optional contendo a primeira conta encerrada ou vazio
     */
    private Optional<Account> findClosedAccount(List<Account> accounts) {
        return accounts.stream()
                .filter(account -> account.getAccountStatus() == AccountStatus.ENCERRADO)
                .findFirst();
    }

    /**
     * Busca todas as contas de um cliente pelo CPF.
     * Método auxiliar que lança exceção se nenhuma conta for encontrada.
     *
     * @param cpf CPF normalizado do cliente
     * @return lista de contas (nunca vazia se não lançar exceção)
     * @throws EntityNotFoundException se nenhuma conta encontrada
     */
    private List<Account> findAccountsByCpf(String cpf) {
        List<Account> accounts = accountRepository.findByCustomerCpf(cpf);

        if (accounts.isEmpty()) {
            throw new EntityNotFoundException("Nenhuma conta encontrada para o CPF: " + cpf);
        }
        return accounts;
    }

    // ==================== VALIDAÇÕES ====================

    /**
     * Valida se o CPF não é nulo nem vazio.
     *
     * @param cpf CPF a validar
     * @throws IllegalArgumentException se CPF nulo ou blank
     */
    private void validateCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException("CPF não pode ser vazio");
        }
    }

    /**
     * Valida se número da conta e agência estão no formato correto.
     * Formatos aceitados:
     * - Conta: 12345-6 (5 dígitos + dígito verificador)
     * - Agência: 1234 ou 1234-1
     *
     * @param accountNumber número da conta
     * @param agencyNumber  número da agência
     * @throws IllegalArgumentException se algum parâmetro é nulo ou formato inválido
     * @throws EntityNotFoundException  se parâmetros vazios
     */
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
     * Valida se já existe uma conta ativa do tipo solicitado.
     * Lançada quando se tenta criar nova conta se já existe ativa.
     *
     * @param accounts    lista de contas a verificar
     * @param accountType tipo de conta solicitada
     * @throws ResponseStatusException se existe conta ativa do tipo na lista
     */
    private void validateNoActiveAccount(List<Account> accounts, AccountType accountType) {
        boolean hasActive = accounts.stream()
                .anyMatch(account -> account.getAccountStatus() == AccountStatus.ATIVO);

        if (hasActive) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cliente já possui uma conta do tipo solicitado: " + accountType);
        }
    }

    /**
     * Valida se a mudança de status é permitida.
     * Regras:
     * - Não pode mudar para status igual ao atual
     * - Não pode encerrar conta com saldo > 0
     *
     * @param account conta sendo atualizada
     * @param request dados com novo status desejado
     * @throws ResponseStatusException se mudança não é permitida
     */
    private void validateStatusChange(Account account, UpdateAccountStatusRequest request) {
        if (account.getAccountStatus() == request.accountStatus()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A conta já possui este status");
        }

        if (request.accountStatus() == AccountStatus.ENCERRADO && account.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Não é possível encerrar conta com saldo");
        }
    }

    /**
     * Valida se o saque pode ser realizado.
     * Regras:
     * - Conta deve estar ativa
     * - Valor deve ser positivo
     * - Saldo deve ser suficiente
     *
     * @param account conta para sacar
     * @param amount  valor a ser sacado
     * @throws ResponseStatusException se validação falha
     */
    private void validatWithdrawal(Account account, BigDecimal amount) {
        validateActiveAccount(account);

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valor inválido");
        }
        if (account.getBalance().compareTo(amount) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente");
        }
    }

    /**
     * Valida se a conta está ativa.
     * Lança exceção se conta está encerrada.
     *
     * @param account conta a validar
     * @throws ResponseStatusException se conta está encerrada
     */
    private void validateActiveAccount(Account account) {
        if (account.getAccountStatus() == AccountStatus.ENCERRADO) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A Conta está ENCERRADA");
        }
    }

    /**
     * Valida se o deposito pode ser realizado.
     * Regras:
     * - Conta deve estar ativa
     * - Valor deve ser positivo
     *
     * @param account conta para deposito
     * @param amount  valor a ser deposito
     * @throws ResponseStatusException se validação falha
     */
    private void validateDeposit(Account account, BigDecimal amount) {
        validateActiveAccount(account);

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valor inválido");
        }
    }

    /**
     * Valida se o cliente possui saldo na conta.
     */
    private void validateAccountsWithoutBalance(List<Account> accounts) {
        boolean hasBalance = accounts.stream()
                .anyMatch(account -> account.getBalance().compareTo(BigDecimal.ZERO) == 0);

        if (hasBalance) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cliente possui saldo na conta");
        }
    }

    // ==================== OPERACOES FINANCEIRAS ====================
    /**
     * Realiza saque
     */
    private void performWithdrawal(Account account, BigDecimal amount) {
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
    }

    /**
     * Realiza Deposito
     */
    private void performDeposit(Account account, BigDecimal amount) {
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
    }

    // ==================== OPERACOES DA CONTA ====================
    private Account createAccount(CreateAccountDTO dto) {
        return AccountMappers.fromRequestToAccount(dto, generateAccountNumber());
    }

    private String generateAccountNumber() {
        while (true) {
            String accountNumber = generateRandomAccountNumber();
            if (!accountRepository.existsByAccountNumber(accountNumber)) {
                return accountNumber;
            }
        }
    }

    private String generateRandomAccountNumber() {
        Random random = new Random();
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            number.append(random.nextInt(10));
        }
        int digitoVerificador = calculateCheckDigit(number.toString());
        return number + "-" + digitoVerificador;
    }

    private int calculateCheckDigit(String number) {
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

    private Account reactivateAccount(Account account, Customer customer) {

        account.setAccountStatus(AccountStatus.ATIVO);

        Account saved = accountRepository.save(account);

        reactivateCustomerIfNecessary(customer, saved);
        registerAccountReactivatedHistory(saved);

        return saved;
    }

    private void reactivateCustomerIfNecessary(Customer customer, Account account) {
        if (customer.getCustomerStatus() == CustomerStatus.INATIVO) {
            customer.setCustomerStatus(CustomerStatus.ATIVO);
            customerRepository.save(customer);

            registerCustomerReactivatedHistory(account, customer);
        }
    }

    private void updateCustomerStatus(Customer customer) {
        boolean hasActiveAccount = accountRepository.existsByCustomerAndAccountStatus(customer, AccountStatus.ATIVO);

        CustomerStatus newStatus = hasActiveAccount
                ? CustomerStatus.ATIVO
                : CustomerStatus.INATIVO;

        if (customer.getCustomerStatus() == newStatus) {
            return;
        }

        customer.setCustomerStatus(newStatus);
        registerCustomerStatusHistory(customer, newStatus);
        customerRepository.save(customer);
    }

    private Account updateAccountsStatus(Account account, UpdateAccountStatusRequest request) {
        account.setAccountStatus(request.accountStatus());
        account.setStatusChangeDate(LocalDate.now());
        account.setStatusChangeReason(request.statusChangeReason());

        return accountRepository.save(account);
    }


    // ==================== HISTORICO ====================
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

    private void registerAccountStatusHistory(Account account, UpdateAccountStatusRequest request) {

        HistoryType historyType = request.accountStatus() == AccountStatus.ENCERRADO
                ? HistoryType.ACCOUNT_CLOSED
                : HistoryType.ACCOUNT_REACTIVATED;

        historyService.register(
                new RegisterHistoryRequest(
                        account,
                        account.getCustomer(),
                        historyType,
                        request.statusChangeReason(),
                        null
                )
        );
    }

    private void registerCustomerStatusHistory(Customer customer, CustomerStatus status) {
        if (status == CustomerStatus.ATIVO) {
            historyService.register(
                    new RegisterHistoryRequest(
                            null,
                            customer,
                            HistoryType.CUSTOMER_REACTIVATED,
                            "Cliente reativado por possuir conta ativa",
                            null
                    )
            );
            return;
        }

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

    private void registerWithdrawalHistory(Account account, BigDecimal amount) {
        historyService.register(
                new RegisterHistoryRequest(
                        account,
                        account.getCustomer(),
                        HistoryType.WITHDRAWAL,
                        "Saque realizado com sucesso!",
                        amount
                )
        );
    }

    private void registerWithdrawalFailedHistory(Account account, BigDecimal amount, String reason) {
        historyService.register(
                new RegisterHistoryRequest(
                        account,
                        account.getCustomer(),
                        HistoryType.WITHDRAWAL_FAILED,
                        reason,
                        amount
                )
        );
    }

    private void registerDepositHistory(Account account, BigDecimal amount) {
        historyService.register(
                new RegisterHistoryRequest(
                        account,
                        account.getCustomer(),
                        HistoryType.DEPOSIT,
                        "Depósito realizado com sucesso!",
                        amount
                )
        );
    }

    private void registerDepositFailedHistory(Account account, BigDecimal amount, String reason) {
        historyService.register(
                new RegisterHistoryRequest(
                        account,
                        account.getCustomer(),
                        HistoryType.DEPOSIT,
                        reason,
                        amount
                )
        );
    }
}