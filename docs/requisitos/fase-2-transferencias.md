
# RF018 - Realizar Transferência

## Descrição

Permitir que o cliente realize transferências financeiras entre contas bancárias.

---

## Dados de Entrada

| Campo | Obrigatório |
|---------|---------|
| Agência de origem | Sim |
| Conta de origem | Sim |
| Agência de destino | Sim |
| Conta de destino | Sim |
| Valor da transferência | Sim |

---

## Informações Retornadas

| Campo |
|---------|
| Conta de origem |
| Conta de destino |
| Valor transferido |
| Data e hora da transferência |
| Status da transferência |
| Saldo atualizado da conta de origem |

### Exemplo

```text
Transferência realizada com sucesso.

Valor: R$ 500,00
Conta Origem: 12345-6
Agência Origem: 0001

Conta Destino: 65432-1
Agência Destino: 0002

Saldo Atual: R$ 1.500,00
Status: CONCLUIDA
```

---

## Regras de Negócio

- A conta de origem deve existir e ativa.
- A conta de destino deve existir e ativa.
- O valor da transferência é obrigatório.
- O valor da transferência deve ser maior que zero.
- A conta de origem deve possuir saldo suficiente para realizar a transferência.
- Não é permitido transferir para a mesma conta, mas é permitido transferir entre contas.
- Após transferencia, o valor deve ser debitado da conta de origem.
- Após transferencia, o valor deve ser creditado na conta de destino.
- A atualização dos saldos deve ocorrer de forma imediata e atômica.
- Em caso de falha, nenhuma movimentação financeira deverá ser realizada.
- Toda transferência deve possuir um identificador único para rastreabilidade e auditoria.

### RN013.01

O sistema deve registrar a transferência para fins de auditoria.

Devem ser armazenados:

- Data
- Hora
- Conta de origem
- Conta de destino
- Valor
- Status

### RN013.02

O sistema deve registrar movimentações no histórico das contas envolvidas.

- Histórico da conta de origem
- Histórico da conta de destino

---

## Status da Transferência

| Status      | Descrição |
|-------------|----------|
| PROCESSANDO | Transferência em execução |
| CONCLUIDA   | Transferência realizada com sucesso |
| FALHOU      | Erro durante a operação |
| CANCELADA   | Operação cancelada |

---

# RF019 - Consultar Histórico de Transferências

## Descrição

Permitir que o cliente visualize as transferências relacionadas às suas contas.

---

## Informações Exibidas

| Campo |
|---------|
| Conta de origem |
| Conta de destino |
| Valor |
| Data e hora |
| Status |

---

## Regras de Negócio

- O cliente pode visualizar apenas transferências associadas às suas contas.
- O histórico deve ser exibido da mais recente para a mais antiga.
- O histórico é somente leitura.