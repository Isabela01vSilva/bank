# Fase 1 - Sistema Bancário + Controle de Gastos

# **Módulo 1 \- Cliente**

## **RF001 \- Cadastrar Cliente**

**Descrição:** Permitir o cadastro de um novo cliente.✅

### **Campos**

* Nome Completo
* CPF
* Data de nascimento
* E-mail
* Telefone
* Tipo de Conta (Poupança e/ou Corrente)

### **Regras de negócio**

* Nome Completo obrigatório. ✅
* CPF único e obrigatorio. ✅
* E-mail único e obrigatorio. ✅
* Data de nascimento obrigatória. ✅
* Telefone único e obrigatório. ✅
* O telefone é considerado único através da combinação DDD \+ número. ✅
* Tipo de Conta obrigatório. ✅
* No nome do cliente, não permitir caracteres numéricos ou especiais.✅
* O CPF deve conter apenas números e ser válido. ✅
* O e-mail deve ser válido, seguindo o padrão de email. ✅
* O telefone deve conter apenas números e ser válido. ✅
* O cliente pode escolher entre conta poupança, conta corrente ou ambas. ✅
* O sistema deve validar o formato do CPF, e-mail e telefone durante o cadastro. ✅
* O sistema deve exibir mensagens de erro claras e específicas para cada tipo de validação que falhar. ✅
* O sistema deve calcular a idade do cliente com base na data de nascimento e impedir o cadastro de clientes menores de 18 anos. ✅

## **RF002 \- Consultar Cliente** 

**Descrição:** Permitir consultar os dados do cliente.

### **Exibir**

* Nome Completo ✅
* CPF ✅
* E-mail ✅
* Telefone ✅
* Data de nascimento ✅

## **RF003 \- Atualizar Cliente**

**Descrição:** Permitir atualizar dados do cliente.

### **Campos permitidos**

* E-mail ✅
* Telefone ✅
* Nome Completo ✅

### **Regras de negócio**

Não permitir alteração de:

* CPF ✅
* Data de nascimento ✅

---

# **Módulo 2 \- Conta**

## **RF004 \- Criar Conta Automaticamente**

**Descrição:** Ao cadastrar um cliente, uma conta bancária deve ser criada automaticamente.

### **Regras de negócio**

* O cliente precisa informar o tipo de conta (poupança, corrente ou ambas) no momento do cadastro para que a conta seja criada automaticamente. CAMPO OBRIGATORIO✅
* Gerar número da conta automaticamente. ✅
* Número da conta deve ser único. ✅
* Número de agência fixo: 0001. ✅
* Conta é criada apenas quando o cliente é cadastrado. ou seja, não é possivel criar uma conta sem um cliente associado. ✅
* Saldo inicial igual a R$ 0,00. ✅
* O status da conta inicia ativa. ✅

---
## **RF005 \- Consultar Conta**

**Descrição:** Permitir consultar os dados da conta do cliente.

### **Exibir**

* Nome e Sobrenome do cliente
* CPF do cliente
* Agência
* Número da conta
* Tipo da conta
* Status
* Saldo atual
* Data de criação

* Posso consultar conta por CPF, se pesquisar por CPF exibe todas as contas vinculadas ao cliente. ✅
* Posso consultar conta por número da conta e agencia, se pesquisar por número da conta e agencia exibe a conta específica.
* O sistema deve permitir a consulta de contas ativas e inativas, mas deve destacar claramente o status da conta para evitar confusão.
* O sistema deve exibir uma mensagem de erro clara e específica se o cliente tentar consultar uma conta que não existe, informando que a conta não foi encontrada e sugerindo verificar os dados de consulta.

---

## **RF006 \- Tipo de Conta**

**Descrição:** O sistema deve permitir os seguintes tipos:

* Conta Corrente
* Conta Poupança

### **Regras de negócio**

* O cliente pode possuir apenas conta corrente. ✅
* O cliente pode possuir apenas conta poupança. ✅
* O cliente pode possuir ambas. ✅
* Cada conta possui saldo independente. ✅
* Cada conta possui transferências independentes. 
* O cliente pode usar ambas as duas contas para realizar transferência.

---

## **RF007 \- Atualizar Status Tipo de Conta**

**Descrição:** Permitir ATIVAR ou ENCERRAR uma conta.

### Auditoria

* Data da alteração.
* Hora da alteração.
* Status anterior.
* Novo status.
* Motivo da alteração.

### **Regras de negócio**

* Não permitir desativação de conta com saldo.
* A desativação de uma conta não afeta outras contas do cliente.
* O sistema deve registrar a data e hora da desativação da conta, e o motivo, para fins de histórico do cliente.
* O sistema deve permitir que o cliente reative uma conta encerrada.
* Não é permitido alterar o tipo da conta após sua criação.
* Uma conta corrente não pode ser convertida em conta poupança.
* Uma conta poupança não pode ser convertida em conta corrente.
* Apenas o status da conta (ATIVA ou ENCERRADA) pode ser alterado.
* Não é permitido encerrar uma conta que possua transferências agendadas pendentes.

---

## **RF008 - Abertura de Segunda Conta**

**Descrição:** Permitir que um cliente abra uma segunda conta bancária, desde que respeite as regras de tipos de conta permitidos.

### **Campos obrigatórios informados pelo cliente**

* CPF
* Tipo da conta

### **Regras**

* A nova conta deve seguir as mesmas regras de criação definidas no RF004.
* O sistema deve impedir a abertura de uma nova conta caso o cliente já possua uma conta do tipo solicitado.
* O sistema deve exibir uma mensagem informando que o cliente já possui uma conta do tipo solicitado quando a solicitação for rejeitada.
* O saldo inicial da nova conta deve ser R$ 0,00.
* A nova conta deve ser criada com status ATIVA.
* O número da conta deve ser gerado automaticamente e ser único no sistema.
* A data de abertura deve corresponder à data de criação da conta.

---

## **RF009 - Validação de Conta Ativa para Movimentações**

**Descrição:** Garantir que apenas contas ativas possam realizar movimentações financeiras.

### **Regras**

* O sistema deve validar o status da conta antes de permitir qualquer movimentação.
* Apenas contas com status ATIVA podem realizar depósitos.
* Apenas contas com status ATIVA podem realizar saques.
* Apenas contas com status ATIVA podem realizar transferências.
* O sistema deve exibir uma mensagem de erro ao tentar movimentar uma conta inativa.
* Contas encerradas não podem receber depósitos.

---
