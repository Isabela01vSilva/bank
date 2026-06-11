---
## RF013 - Transferência

**Descrição:** Permitir a realização de transferências entre contas bancárias.

### Campos Obrigatórios

* Número da agência Origem
* Número da conta Origem
* Valor
* Número da agência Destino
* Número da conta Destino

### Exibir

* Mensagem com "Valor de R$00.00 de transferido da conta: 00000-0 agencia: 0000, para a conta: 00000-0 agencia: 0000, saldo atual: "

### Regras de Negócio

* Aplicar as validações definidas no RF010.
* A transferência deve possuir um valor.
* O valor da transferência deve ser maior que zero.
* A conta de origem deve possuir saldo suficiente para realizar a transferência.
* O cliente pode realizar transferências entre suas próprias contas.
* O cliente pode realizar transferências para contas de outros clientes.
* O valor transferido pode ser debitado tanto de uma conta corrente quanto de uma conta poupança.
* O saldo da conta de origem deve ser atualizado imediatamente após a transferência.
* O saldo da conta de destino deve ser atualizado imediatamente após a transferência.
* A conta de origem e a conta de destino não podem ser a mesma conta.
* Cada conta possui realização de transferências independentes.
* Ambas podem ser utilizadas para movimentações financeiras.

---

# Módulo 6 - Transferências

## RF017 - Realizar Transferência

### Descrição

Permitir que o cliente realize transferências entre contas bancárias.

### Informações exibidas

* Conta de origem
* Conta de destino
* Valor da transferência
* Data e hora da transferência
* Status da transferência

### Regras de negócio

* A conta de origem deve estar ativa.
* A conta de destino deve estar ativa.
* O valor da transferência deve ser maior que zero.
* O cliente deve possuir saldo suficiente na conta de origem.
* O sistema deve atualizar os saldos das contas envolvidas imediatamente após a realização da transferência.
* O sistema deve registrar a movimentação no histórico das contas de origem e destino.
* O sistema deve impedir transferências para a mesma conta de origem.
* O sistema deve exibir uma mensagem de erro caso a transferência não possa ser realizada.
* O sistema deve registrar data, hora, valor, conta de origem, conta de destino e status da transferência para fins de auditoria.

### Status

* PROCESSANDO
* CONCLUIDA
* FALHOU
* CANCELADA

---

## RF018 - Consultar Histórico de Transferências

### Descrição

Permitir que o cliente visualize as transferências realizadas.

### Informações exibidas

* Conta de origem
* Conta de destino
* Valor
* Data e hora
* Status

### Regras de negócio

* O cliente pode visualizar apenas transferências relacionadas às suas contas.
* O histórico deve apresentar as transferências mais recentes primeiro.
* O histórico não pode ser alterado ou excluído pelo cliente.

---