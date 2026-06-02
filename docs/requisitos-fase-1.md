Fase 1 \- Sistema Bancário \+ Controle de Gastos 

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
* Sobrenome

### **Regras de negócio**

Não permitir alteração de:

* Nome  
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
* Mas ele pode cadastrar abrir uma conta nova, se ele nao tiver corrente ou poupança

---

# **Módulo 3 \- Dashboard**

Este será o principal diferencial da aplicação.

## **RF008 \- Visualizar Resumo Financeiro**

### **Exibir**

* Saldo atual.  
* Total de entradas do mês.  
* Total de saídas do mês.  
* Total economizado no mês.  
* Total economizado na conta.

## **RF009 \- Indicador Financeiro**

### **Exibir**

* Valor total de entradas do mês atual.  
* Comparação percentual com o mês anterior.  
* Valor total de saídas do mês atual.  
* Comparação percentual com o mês anterior.  
* Valor economizado no mês.

## **RF013 \- Evolução Financeira**

### **Exibir**

Gráfico contendo:

* Entradas  
* Saídas  
* Economia

### **Períodos**

* Últimos 30 dias  
* Últimos 6 meses  
* Últimos 12 meses

### **Regra de negócio**

Caso a conta possua mais de 12 meses de existência:

* Exibir apenas os últimos 12 meses.

## **RF014 \- Últimas Movimentações**

### **Exibir**

Últimas movimentações realizadas:

* Depósitos  
* Saques  
* Transferências   
* Pagamentos de boleto 

---

# **Módulo 4 \- Movimentações**

## **RF016 \- Sacar Dinheiro**

### **Regras de negócio**

* Saldo suficiente.  
* Valor maior que zero.  
* Registrar a movimentação no histórico.

## **RF017 \- Gerar Boleto para Depósito**

### **Regras de negócio**

* Possuir valor.  
* Possuir data de vencimento.  
* Possui status.

### **Status**

* PENDENTE  
* PAGO  
* VENCIDO  
* CANCELADO

### **Regra**

Quando o boleto for marcado como pago:

* Creditar valor na conta.  
* Registrar a movimentação no histórico.

---

# **Módulo 5 \- Transações**

## **RF008 \- Realizar Transferência Imediata**

**Descrição:** Transferir dinheiro entre contas imediatamente.

### **Regras**

* Conta origem deve possuir saldo.  
* Conta destino deve existir.  
* Valor deve ser maior que zero.  
* Registrar entrada e saída.  
* Ele pode transferir dinheiro das suas duas contas caso possua e queira, é opcional.   
* Ele pode transferir dinheiro entre suas próprias contas 

---

## **RF009 \- Agendar Transferência**

**Descrição:** Permitir agendar transferências.

### **Regras**

* Data futura obrigatória.  
* Valor obrigatório.  
* Conta origem deve possuir saldo na execução.

### **Status possíveis**

* AGENDADA  
* PROCESSANDO  
* REALIZADA  
* CANCELADA  
* FALHOU

---

## **RF010 \- Cancelar Transferência Agendada**

**Descrição:** Permitir cancelar uma transferência antes da execução.

### **Regras**

* Apenas transferências agendadas podem ser canceladas.

---

## **RF011 \- Editar Transferência Agendada**

**Descrição:** Permitir alterar uma transferência antes do processamento.

### **Campos editáveis**

* Valor  
* Data  
* Conta destino

### **Regras**

* Apenas transferências agendadas.

---

## **RF012 \- Processamento Automático**

**Descrição:** O sistema deve processar transferências agendadas.

### **Regra**

* Executar de 1 em 1 hora.

**Implementação futura**

* Spring Scheduler.

Exemplo:

@Scheduled(cron \= "0 0 \* \* \* \*")  
---

## **RF013 \- Validar Saldo**

**Descrição:** Antes de executar uma transferência.

### **Regras**

* Saldo suficiente.  
* Caso contrário:

Status:

* FALHOU

Motivo:

* SALDO INSUFICIENTE

# **Módulo 6 \- Histórico**

## **RF014 \- Histórico de Transações**

**Descrição:** Consultar movimentações da conta.

### **Exibir**

* Data  
* Tipo  
* Valor  
* Status  
* Conta origem  
* Conta destino

## **RF015 \- Filtrar Histórico**

### **Filtros**

* Data inicial  
* Data final  
* Tipo de movimentação  
* Valor mínimo  
* Valor máximo  
* Status

## **RF016 \- Histórico por Status**

Visualizar:

* Realizadas  
* Agendadas  
* Canceladas  
* Falhas

---

* Sobre a conta: Ter um dashboard com a analise da conta, aonde mostra os dados de quanto entrou na conta no mes ou ano, quanto saiu no mes ou ano o quanto poupei desde que abri a conta, na verdade o quanto  tenho poupado  e nao gastei   
* cartão de credito: quero ter uma analise de quanto gastei ental coisa como, quanto gastei por exemplo usando uber   
* conta: no dashboard quero um fluxo mensal, de um ano ou desde que abri a conta mas se tiver mais do que um ano exibe apenas um ano   
* Cona: historico de transferencia   
* Cartão de credito quero poder transferir dinheiro do cartão de credito;   
* Quero ter tipos de conta, conta poupança e conta corrente  
* quero poder sacar dinheiro   
* quero poder gerar um boleto para depositar na conta   
* quero pode depositar mas nao tenho ideia de como   
* quero poder realiza uma transação   
* quero que as transações sejam feitas de 1h em 1h,   
* quero poder editar uma transação se tiver tempo,   
* caso eu nao teha saldo suficiente quero que a transação seja barrada   
* quero poder excluir uma transação agendada   
* quero poder agendar uma transação   
* e posso fazer uma transação imediata   
* ao criar um cliente automaticamente cria uma conta \+ agencia   
* quero poder filtrar as transações, por tipo entrada, saida, valor min e maximo, periodo da transação.   
* quero poder ver o historico das transações   
* quero ver o historicos das transações realizadas, agendadas, canceladas  
  