# Fase 3 - Qualidade e Padronização

## Objetivo

Garantir que a API seja robusta, padronizada e autodocumentada,
entregando respostas consistentes, tratamento adequado de erros
e documentação interativa para consumo por times de frontend
e integrações externas.

---

## RF020 - Tratamento Global de Exceções

### Descrição

Implementar um mecanismo centralizado para captura e tratamento
de todas as exceções lançadas pela aplicação, garantindo que
nenhum erro interno seja exposto ao cliente.

### Regras de Negócio

- Toda exceção deve retornar uma resposta padronizada.
- O sistema não deve expor stack traces ou mensagens internas.
- O timestamp deve ser incluído em toda resposta de erro.
- O caminho da requisição deve ser incluído na resposta de erro.
- Cada tipo de exceção deve mapear para um HTTP status específico.

### Mapeamento de Exceções

| Exceção                    | HTTP Status |
|----------------------------|-------------|
| `EntityNotFoundException`  | 404         |
| `ResponseStatusException`  | status da exceção |
| `IllegalArgumentException` | 400         |
| `MethodArgumentNotValidException` | 422  |
| `Exception` (genérica)     | 500         |

### Formato da Resposta de Erro

```json
{
  "timestamp": "2025-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "CPF não pode ser vazio",
  "path": "/accounts"
}
```

---

## RF021 - Padronização das Respostas da API

### Descrição

Garantir que todas as respostas de sucesso e erro sigam
um formato consistente em toda a API.

### Regras de Negócio

- Todas as respostas de sucesso devem seguir o mesmo envelope.
- Respostas de erro devem seguir o formato definido no RF020.
- HTTP status codes devem ser semanticamente corretos:
    - `200 OK` — consultas e atualizações
    - `201 Created` — criação de recursos
    - `204 No Content` — quando não há corpo na resposta
    - `400 Bad Request` — dados inválidos
    - `404 Not Found` — recurso não encontrado
    - `409 Conflict` — conflito de regra de negócio
    - `422 Unprocessable Entity` — validação de negócio
    - `500 Internal Server Error` — erros inesperados

---

## RF022 - Validações Avançadas com Bean Validation

### Descrição

Substituir validações manuais nos services por anotações
declarativas do Bean Validation (`jakarta.validation`) nos
DTOs de entrada, mantendo nos services apenas as validações
de regra de negócio.

### Campos a Validar

#### `CustomerAccountRequest`

| Campo         | Validação                          |
|---------------|------------------------------------|
| `fullName`    | `@NotBlank`                        |
| `birthDate`   | `@NotNull`, `@Past`                |
| `cpf`         | `@NotBlank`, `@CPF`                |
| `email`       | `@NotBlank`, `@Email`              |
| `phoneNumber` | `@NotBlank`                        |
| `accountTypes`| `@NotEmpty`                        |

#### `AccountTransactionRequest`

| Campo           | Validação                        |
|-----------------|----------------------------------|
| `accountNumber` | `@NotBlank`                      |
| `agencyNumber`  | `@NotBlank`                      |
| `amount`        | `@NotNull`, `@DecimalMin("0.01")`|

#### `TransferRequest`

| Campo                       | Validação                         |
|-----------------------------|-----------------------------------|
| `sourceAccountNumber`       | `@NotBlank`                       |
| `sourceAgencyNumber`        | `@NotBlank`                       |
| `destinationAccountNumber`  | `@NotBlank`                       |
| `destinationAgencyNumber`   | `@NotBlank`                       |
| `amount`                    | `@NotNull`, `@DecimalMin("0.01")` |

### Regras de Negócio

- Validações de formato ficam nos DTOs com Bean Validation.
- Validações de regra de negócio permanecem nos services.
- O `GlobalExceptionHandler` deve capturar
  `MethodArgumentNotValidException` e retornar os erros
  de forma legível.

---

## RF023 - Documentação com Swagger / OpenAPI

### Descrição

Implementar documentação interativa da API utilizando
SpringDoc OpenAPI, permitindo visualização e teste
dos endpoints diretamente pelo navegador.

### Regras de Negócio

- Todos os endpoints devem estar documentados.
- Cada endpoint deve conter:
    - Descrição da operação.
    - Parâmetros de entrada com tipos e exemplos.
    - Possíveis respostas com HTTP status e exemplos.
- A documentação deve estar disponível em `/swagger-ui.html`.
- O JSON da spec deve estar disponível em `/v3/api-docs`.
- Agrupar endpoints por módulo (Customer, Account,
  Transfer, History).

### Dependência

```xml
<dependency>
  <groupId>org.springdoc</groupId>
  <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
  <version>2.3.0</version>
</dependency>
```

---

## RF024 - Melhorias de Código

### Descrição

Consolidar as refatorações de clean code e orientação
a objetos aplicadas na Fase 3 de desenvolvimento.

### Itens

- [✅] Separar `AccountService` em `AccountTransactionService`,`AccountLifecycleService` e `AccountQueryService`
- [✅] Extrair `AccountValidationService` centralizando todas as validações de conta
- [✅] Extrair `CustomerValidationService` centralizando todas as validações de cliente
- [✅] Extrair `CustomerStatusService` para gerenciar status do cliente com base nas contas
- [✅] Separar `HistoryService` em `HistoryCommandService` e `HistoryQueryService`
- [✅] Extrair `AccountNumberGenerator` com limite de tentativas e lançamento de exceção
- [✅] Enriquecer domínio `Account` com `isActive()`,`isClosed()`, `hasBalance()`, `close()`, `reactivate()`
- [✅] Enriquecer domínio `Customer` com `activate()`, `deactivate()`, `isActive()`
- [✅] Enriquecer domínio `Transfer` com `create()`, `complete()`, `fail()`
- [✅] Substituir `@Autowired` em campo por injeção por construtor em todos os services
- [✅] Tornar mappers `final` com construtor `private`
- [✅] Corrigir bug `DEPOSIT_FAILED` registrado como `DEPOSIT`
- [✅] Corrigir bug `transactionDate` nulo nas transferências
- [✅] Corrigir `@Transactional` em método `private` no `TransferService`
- [ ] Ordenação decrescente por data em todos os endpoints de histórico
- [ ] Adicionar `TransferStatus.CANCELADA` ao enum
- [ ] Padronizar `TransferStatus` em português conforme RF018