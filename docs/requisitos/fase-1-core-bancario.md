# Fase 1 - Sistema Bancário + Controle de Gastos

# **Módulo 1 \- Cliente**

## **RF001 \- Cadastrar Cliente**

**Descrição:** Permitir o cadastro de um novo cliente.

### **Campos**

* Nome
* Sobrenome
* CPF
* Data de nascimento
* E-mail
* Telefone
* Tipo de Conta (Poupança e/ou Corrente)

### **Regras de negócio**

* Nome obrigatório.
* Sobrenome obrigatório.
* CPF único.
* E-mail único.
* Data de nascimento obrigatória.
* Telefone obrigatório.
* O telefone é considerado único através da combinação DDD \+ número.
* Tipo de Conta obrigatório.
* No nome do cliente, não permitir caracteres numéricos ou especiais.
* No sobrenome do cliente, não permitir caracteres numéricos ou especiais.
* O CPF deve conter apenas números e ser válido.
* O e-mail deve ser válido, seguindo o padrão de email.
* O telefone deve conter apenas números e ser válido.
* O cliente pode escolher entre conta poupança, conta corrente ou ambas.
* O sistema deve validar o formato do CPF, e-mail e telefone durante o cadastro.
* O sistema deve exibir mensagens de erro claras e específicas para cada tipo de validação que falhar.
* O sistema deve calcular a idade do cliente com base na data de nascimento e impedir o cadastro de clientes menores de 18 anos.
* O nome completo do cliente deve ser armazenado como a combinação do nome e sobrenome, e exibido dessa forma em todas as consultas relacionadas ao cliente.

## **RF002 \- Consultar Cliente**

**Descrição:** Permitir consultar os dados do cliente.

### **Exibir**

* Nome
* Sobrenome
* CPF
* E-mail
* Telefone
* Data de nascimento
* Data de criação do cadastro

## **RF003 \- Atualizar Cliente**

**Descrição:** Permitir atualizar dados do cliente.

### **Campos permitidos**

* E-mail
* Telefone
* Nome
* Sobrenome

### **Regras de negócio**

Não permitir alteração de:

* CPF
* Data de nascimento
* Tipo de Conta

---

# **Módulo 2 \- Conta**

## **RF004 \- Criar Conta Automaticamente**

**Descrição:** Ao cadastrar um cliente, uma conta bancária deve ser criada automaticamente.

### **Regras de negócio**

* Gerar número da conta automaticamente.
* Gerar agência automaticamente.
* Associar conta ao cliente.
* Saldo inicial igual a R$ 0,00.
* A conta inicia ativa.

## **RF005 \- Tipo de Conta**

**Descrição:** O sistema deve permitir os seguintes tipos:

* Conta Corrente
* Conta Poupança

### **Regras de negócio**

* O cliente pode possuir apenas conta corrente.
* O cliente pode possuir apenas conta poupança.
* O cliente pode possuir ambas.
* Cada conta possui saldo independente.
* Cada conta possui transferências independentes.
* O cliente pode usar ambas as duas contas para realizar transferência.

## **RF006 \- Consultar Conta**

**Descrição:** Permitir consultar os dados da conta.

### **Exibir**

* Nome e Sobrenome do cliente
* CPF do cliente
* Agência
* Número da conta
* Tipo da conta
* Status
* Saldo atual
* Data de criação

## **RF007 \- Atualizar Status da Conta**

**Descrição:** Permitir ativar ou desativar uma conta.

### **Regras de negócio**

* Não permitir desativação de conta com saldo.
* Apenas contas ativas podem ser movimentadas.
* A desativação de uma conta não afeta outras contas do cliente.
* Mas ele pode cadastrar abrir uma conta nova, se ele nao tiver corrente ou poupança, ou seja, ele pode ter as duas, mas nao pode ter mais de uma de cada tipo.
* O sistema deve enviar uma notificação ao cliente quando uma conta for desativada, informando o motivo da desativação e os passos necessários para reativar a conta, se aplicável.
* O sistema deve registrar a data e hora da desativação da conta, bem como o motivo, para fins de histórico do cliente.
* o sistema deve permitir que o cliente reative uma conta desativada.
* O sistema deve validar o status da conta antes de permitir qualquer movimentação, garantindo que apenas contas ativas possam ser movimentadas.
* O sistema deve exibir uma mensagem de erro clara e específica se o cliente tentar desativar uma conta que possui saldo, informando que a conta deve estar com saldo zero para ser desativada.
* O sistema não permite o cliente visualizar o historico de movimentações de uma conta desativada, mas permite visualizar o histórico de movimentações de contas ativas.

---