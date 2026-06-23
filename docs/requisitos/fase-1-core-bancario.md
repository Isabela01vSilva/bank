# Fase 1 - Sistema Bancário + Controle de Gastos

# Módulo 1 - Cliente

## RF001 - Cadastrar Cliente

**Descrição:** Permitir o cadastro de um novo cliente. 

### Campos

* Nome Completo
* CPF
* Data de Nascimento
* E-mail
* Telefone
* Tipo de Conta (Poupança e/ou Corrente)
* Status do Cliente (controlado pelo sistema)

### Regras de Negócio

* Nome Completo obrigatório. ✅
* CPF único e obrigatório. ✅
* E-mail único e obrigatório. ✅
* Telefone único e obrigatório. ✅
* O telefone é considerado único pela combinação DDD + número. ✅
* Data de nascimento obrigatória. ✅
* Tipo de Conta obrigatório. ✅
* Não permitir caracteres numéricos ou especiais no nome. ✅
* O CPF deve conter apenas números e ser válido. ✅
* O e-mail deve seguir um formato válido. ✅
* O telefone deve conter apenas números e ser válido. ✅
* O cliente pode escolher:
    * Conta Corrente ✅
    * Conta Poupança ✅
    * Ambas ✅
* O sistema deve validar CPF, e-mail e telefone durante o cadastro. ✅
* O sistema deve exibir mensagens claras para cada validação que falhar. ✅
* O sistema deve impedir o cadastro de menores de 18 anos. ✅
* O cliente inicia com status **ATIVO**. ✅
* O status do cliente é controlado exclusivamente pelo sistema. ✅

---

## RF002 - Consultar Cliente

**Descrição:** Permitir consultar os dados do cliente.

### Exibir

* Nome Completo ✅
* CPF ✅
* E-mail ✅
* Telefone ✅
* Data de Nascimento ✅

---

## RF003 - Atualizar Cliente

**Descrição:** Permitir atualizar dados do cliente.

### Campos Permitidos Alterações

* Nome Completo ✅
* E-mail ✅
* Telefone ✅

### Campos Não Permitidos Alterações

* CPF ✅
* Data de Nascimento ✅
* Status do Cliente ✅

---

# Módulo 2 - Conta

## RF004 - Criar Conta Automaticamente

**Descrição:** Ao cadastrar um cliente, o sistema deve criar automaticamente as contas selecionadas.

### Regras de Negócio

* Gerar número da conta automaticamente. ✅
* O número da conta deve ser único. ✅
* Agência fixa: **0001**. ✅
* Não é permitido criar conta sem cliente associado. ✅
* Saldo inicial: R$ 0,00. ✅
* A conta inicia com status **ATIVA**. ✅

---

## RF005 - Consultar Conta

**Descrição:** Permitir consultar os dados da conta por CPF ou por Agência + Número da Conta.

### Exibir

* Nome do Cliente
* CPF
* Agência
* Número da Conta
* Tipo da Conta
* Status
* Saldo Atual
* Data de Criação

### Regras de Negócio

* Consultar por CPF exibe todas as contas do cliente. ✅
* Consultar por Agência + Número da Conta exibe uma conta específica. ✅
* Permitir consulta de contas ativas e encerradas. ✅

---

## RF006 - Tipos de Contas

**Descrição:** O sistema deve permitir os seguintes tipos:

* Conta Corrente
* Conta Poupança

### Regras de Negócio

* O cliente pode possuir uma conta corrente. ✅
* O cliente pode possuir uma conta poupança. ✅
* O cliente pode possuir ambas. ✅
* Cada conta possui saldo independente. ✅
* O tipo da conta não pode ser alterado após a criação. ✅
* Conta Corrente não pode ser convertida em Poupança. ✅
* Conta Poupança não pode ser convertida em Corrente. ✅
* Apenas o status da conta pode ser alterado. ✅

---

## RF007 - Ativar ou Encerrar Conta

**Descrição:** Permitir alterar o status de uma conta entre **ATIVA** e **ENCERRADA**.

### Campos Obrigatórios

* Número da Conta ✅
* Agência ✅
* Novo Status (ATIVA ou ENCERRADA) ✅

### Campo Opcional

* Motivo da alteração 

### Exibir

* Número da Conta ✅
* Agência ✅
* Tipo da Conta ✅
* Status da Conta ✅
* Saldo Atual ✅
* Data de Criação ✅
* Data da Alteração ✅
* Mensagem de Confirmação ✅

### Regras de Negócio

#### Encerramento

* Não permitir encerramento de conta com saldo diferente de R$ 0,00. ✅
* O encerramento de uma conta não afeta outras contas do cliente. ✅
* Registrar data, hora e motivo do encerramento. ✅

#### Reativação

* O sistema deve permitir reativar uma conta encerrada. ✅
* Ao reativar uma conta, devem ser mantidos:
    * Número da conta; ✅
    * Número da agência; ✅
    * Histórico;
    * Dados da conta. ✅

---

## RF008 - Abertura de Nova Conta

**Descrição:** Permitir que um cliente já cadastrado abra novas contas respeitando as regras de RF004 - Criar Conta Automaticamente e RF006 - Tipos de Conta.

### Campos Obrigatórios

* CPF
* Tipo de Conta

### Regras de Negócio

* Aplicar todas as regras do RF004. ✅
* O cliente não pode possuir duas contas do mesmo tipo. ✅
* Caso já exista uma conta ativa do tipo solicitado, a abertura deve ser rejeitada. ✅
* Caso exista uma conta encerrada do mesmo tipo solicitado:
    * Reativar a conta existente. ✅
    * Manter o número da conta. ✅
    * Manter o número da agência. ✅
    * Manter o histórico.
    * Não criar uma nova conta. ✅
* Caso o cliente não possua conta do tipo solicitado:
    * Criar uma nova conta normalmente. ✅
* Clientes não cadastrados não podem solicitar abertura de conta. ✅

---

## RF009 - Regras de Status do Cliente

### Status ATIVO

O cliente está **ATIVO** quando possui pelo menos uma conta ativa.

### Status INATIVO

O cliente está **INATIVO** quando não possui nenhuma conta ativa.

### Permissões do Cliente INATIVO

* Consultar dados cadastrais.
* Consultar contas.
* Consultar extratos.
* Consultar histórico de movimentações.

### Retorno para ATIVO

O cliente volta automaticamente para **ATIVO** quando possuir pelo menos uma conta ativa, seja por:

* Reativação de conta encerrada. ✅
* Abertura de uma nova conta. ✅

---

# Módulo 3 - Movimentações

## RF010 - Validação de Conta para Movimentações

**Descrição:** Garantir que apenas contas aptas possam realizar movimentações financeiras.

### Regras de Negócio

* O sistema deve validar o status da conta antes de qualquer movimentação. ✅
* Apenas contas com status **ATIVA** podem realizar movimentações financeiras. ✅
* Contas com status **ENCERRADA** não podem:
    * Receber depósitos; ✅
    * Realizar saques; ✅
    * Realizar transferências; 
    * Receber transferências;
* O sistema deve exibir uma mensagem clara ao tentar movimentar uma conta encerrada. ✅

---

## RF011 - Saque

**Descrição:** Permitir a realização de saques.

### Campos Obrigatórios

* Número da agência 
* Número da conta
* Valor

### Exibir

* Mensagem com "Valor sacado: R$0.00"

### Regras de Negócio

* Aplicar as validações definidas no RF010. ✅
* O saque deve possuir um valor. ✅
* O valor do saque deve ser maior que zero. ✅
* A conta deve possuir saldo suficiente para realizar o saque. ✅
* O sistema deve atualizar o saldo da conta quando o saque for efetivamente concluído. ✅

---

## RF012 - Depósito

**Descrição:** Permitir a realização de depósitos.

### Campos Obrigatórios

* Número da agência
* Número da conta
* Valor 

### Exibir

* Mensagem com "Valor depositado: R$0.00"

### Regras de Negócio

* Aplicar as validações definidas no RF010. ✅
* O depósito deve possuir um valor. ✅
* O valor do depósito deve ser maior que zero. ✅

---

# Módulo 4 - Histórico

### Regras de negócio

- O histórico deve registrar automaticamente todas as operações realizadas pelo cliente e pelas contas.
- Os registros do histórico não podem ser alterados ou excluídos.
- O histórico deve ser apresentado em ordem decrescente de data e hora (mais recente primeiro).
- O cliente só pode visualizar informações relacionadas às suas próprias contas.
- Cada registro deve possuir um identificador único.
- Todos os registros devem possuir data e hora de ocorrência.

---

## RF013 - Consultar Histórico do Cliente ✅

### Descrição

Permitir consultar todos os eventos relacionados ao ciclo de vida do cliente.

### Informações exibidas

- Abertura de contas.
- Reativação de contas.
- Inativação de contas.
- Encerramento de contas.
- Alterações de Informações do cliente.

---

## RF014 - Consultar Histórico por Tipo de Conta ✅

### Descrição

Permitir consultar o histórico de movimentações e eventos de uma conta específica por tipo de conta.

### informações para pesquisa

- CPF do Cliente
- Tipo de Conta: 
  - Conta Corrente
  - Conta Poupança

### Regras de negócio

- O sistema deve retornar apenas informações do tipo de conta selecionado.

---

## RF015 - Consultar Histórico por Tipo de Movimentação ✅

### Descrição

Permitir consultar o histórico de movimentações e eventos de uma conta específica por tipo de movimentação.

### Tipos disponíveis e Informações exibidas

- CPF do Cliente
- Tipo de movimentação:
  - Saque
  - Deposito
  - Transferencia
  - Agendamento de transferencia

### Regras de negócio

- O sistema deve retornar apenas movimentações pertencentes à conta informada.
- É permitido informar um ou mais tipos de movimentação.

---

## RF016 - Consultar Histórico por Período ✅

### Descrição 

Permitir consultar as movimentações de uma conta dentro de um período informado.

### Filtros disponíveis 

- Período inicial e final

### Regras de negócio 

- Os filtros podem ser utilizados de forma individual ou combinada. 
- Caso nenhum resultado seja encontrado, o sistema deve informar que não existem movimentações para os filtros selecionados.

---
## RF017 - Consultar Extrato Consolidado do Cliente

### Descrição

Permitir consultar o histórico de movimentações de um cliente, considerando todas as suas contas cadastradas ou uma conta específica quando informada.

### Filtros disponíveis

- Tipo da conta (Corrente ou Poupança) - opcional
- Tipo da movimentação (Depósito, Saque, Transferência, etc.) - opcional

### Regras de negócio

- O sistema deve permitir a consulta de todas as movimentações associadas ao cliente.
- Caso o tipo da conta seja informado, o sistema deve retornar apenas as movimentações da conta selecionada.
- Caso o tipo da conta não seja informado, o sistema deve retornar as movimentações de todas as contas vinculadas ao cliente.
- Os filtros podem ser utilizados de forma individual ou combinada.
- Caso o cliente não possua contas cadastradas, o sistema deve informar que não existem contas vinculadas ao cliente.
- Caso nenhum resultado seja encontrado, o sistema deve informar que não existem movimentações para os filtros selecionados.
- Caso o cliente informado não exista, o sistema deve retornar uma mensagem de cliente não encontrado.