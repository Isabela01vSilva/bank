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

* Motivo da alteração ✅

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

* Não permitir encerramento de conta com saldo diferente de R$ 0,00.
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

* O sistema deve validar o status da conta antes de qualquer movimentação.
* Apenas contas com status **ATIVA** podem realizar movimentações financeiras.
* Contas com status **ENCERRADA** não podem:
    * Receber depósitos;
    * Realizar saques;
    * Realizar transferências; 
    * Receber transferências;
* O sistema deve exibir uma mensagem clara ao tentar movimentar uma conta encerrada.

---

## RF011 - Saque

**Descrição:** Permitir a realização de saques.

### Campos Obrigatórios

* Número da agencia 
* Número da conta
* Valor

### Exibir

* Mensagem com "Valor de R$00.00 de saque da conta: 00000-0 agencia: 0000, saldo atual: "

### Regras de Negócio

* Aplicar as validações definidas no RF010.
* O saque deve possuir um valor.
* O valor do saque deve ser maior que zero.
* A conta deve possuir saldo suficiente para realizar o saque.
* O sistema deve atualizar o saldo da conta quando o saque for efetivamente concluído.

---

## RF012 - Depósito

**Descrição:** Permitir a realização de depósitos.

### Campos Obrigatórios

* Número da agencia
* Número da conta
* Valor 

### Exibir

* Mensagem com "Valor de R$00.00 de depósito para a conta: 00000-0 agencia: 0000, saldo atual: "

### Regras de Negócio

* Aplicar as validações definidas no RF010.
* O depósito deve possuir um valor.
* O valor do depósito deve ser maior que zero.

---

## RF013 - Transferência

**Descrição:** Permitir a realização de transferências entre contas bancárias.

### Campos Obrigatórios

* Número da agencia Origem
* Número da conta Origem
* Valor
* Número da agencia Destino
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