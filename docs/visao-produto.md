Sistema Bancário + Controle de Gastos 

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

# **Módulo 3 \- Dashboard**

Este será o principal diferencial da aplicação.

## **RF008 \- Visualizar Resumo Financeiro**

### **Exibir**

* Saldo atual.  
* Total de entradas do mês.  
* Total de saídas do mês.  
* Total economizado no mês.  
* Total economizado da conta.

## **RF009 \- Indicador Financeiro**

### **Exibir**

* Valor total de entradas do mês atual. Comparação percentual com o mês anterior.  
* Valor total de saídas do mês atual. Comparação percentual com o mês anterior.  
* Valor economizado no mês. Comparação percentual com o mês anterior.

## **RF010 \- Evolução Financeira**

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

## **RF011 \- Últimas Movimentações**

### **Exibir**

Últimas movimentações realizadas:

* Depósitos  
* Saques  
* Transferências   
* Pagamentos de boleto 

---

# **Módulo 4 \- Movimentações**

## **RF012 \- Gerar boleto para Saque**

### **Regras de negócio**

* Possui status.
* O sistema deve registrar a data, hora, valor e tipo da movimentação para fins de histórico e auditoria.
* O sistema deve atualizar o saldo da conta imediatamente após a realização do saque.
* O sistema deve permitir que o client visualize o histórico de saques realizados, incluindo informações como data, valor e status da operação.
* O sistema deve permitir a geração de um boleto de saque para valores iguais ou superiores a R$ 50,00, possibilitando que o client realize a retirada do valor em uma agência.
* O boleto de saque somente poderá ser gerado caso o client possua saldo suficiente no momento da solicitação.
* Após a geração do boleto, o valor correspondente deverá ficar bloqueado na conta do cliente, não podendo ser utilizado em outras movimentações até que o boleto seja pago, cancelado ou expire.

### **Status**

* PENDENTE
* PAGO
* VENCIDO
* CANCELADO

## **RF013 \- Gerar Boleto para Depósito**

### **Regras de negócio**

* Possuir valor.  
* Possuir data de vencimento.  
* Possui status.
* O sistema deve registrar a data, hora, valor e tipo da movimentação para fins de histórico e auditoria.
* O sistema deve atualizar o saldo da conta imediatamente após a realização do depósito.
* O sistema deve permitir que o client visualize o histórico de depósitos realizados, incluindo informações como data, valor e status da operação.
* O sistema deve permitir a geração de um boleto de depósito para valores iguais ou superiores a R$ 50,00, possibilitando que o client realize o depósito em uma agência ou por meio de transferência bancária.
* O boleto pode ser gerado mesmo sem saldo suficiente, pois o client pode gerar o boleto e depois realizar o pagamento para que o valor seja creditado na conta.
* O boleto de depósito deve conter um código de barras ou QR code para facilitar o pagamento em agências ou por meio de aplicativos de pagamento, garantindo a praticidade e segurança na realização do depósito

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

# **Módulo 5 \- Histórico**

## **RF014 \- Visualizar Histórico de Movimentações**

### Descrição

Permitir que o cliente consulte todas as movimentações realizadas em suas contas.

### Informações exibidas

- Data e hora da movimentação
- Tipo da movimentação
- Valor
- Status
- Conta relacionada
- Descrição da operação

### Tipos de movimentação

- Depósito
- Saque
- Geração de boleto
- Pagamento de boleto
- Cancelamento de boleto
- Transferência (futuro)
- Transferência agendada (futuro)

### Regras de negócio

- O histórico deve registrar automaticamente toda movimentação realizada na conta.
- Os registros do histórico não podem ser alterados ou excluídos pelo cliente.
- O histórico deve apresentar as movimentações em ordem decrescente de data (mais recentes primeiro).
- O cliente só pode visualizar movimentações das próprias contas.
- Cada registro deve possuir um identificador único.

---

## RF015 - Filtrar Histórico de Movimentações

### Descrição

Permitir que o cliente realize consultas específicas no histórico.

### Filtros disponíveis

- Período inicial e final
- Tipo de movimentação
- Status da movimentação
- Valor mínimo
- Valor máximo

### Regras de negócio

- Os filtros podem ser utilizados de forma individual ou combinada.
- O sistema deve retornar apenas movimentações pertencentes ao cliente autenticado.
- Caso nenhum resultado seja encontrado, o sistema deve informar que não existem movimentações para os filtros selecionados.

---

## RF016 - Consultar Resumo do Histórico

### Descrição

Permitir que o cliente visualize indicadores resumidos das movimentações.

### Informações exibidas

- Total de entradas
- Total de saídas
- Quantidade de movimentações
- Maior movimentação realizada
- Última movimentação realizada

### Regras de negócio

- Os valores devem ser calculados com base nos filtros aplicados.
- O resumo deve considerar apenas movimentações pertencentes ao cliente autenticado.
- O sistema deve atualizar os indicadores sempre que uma nova movimentação for registrada.

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

# Módulo 7 - Agendamento de Transferências

## RF019 - Agendar Transferência

### Descrição

Permitir que o cliente agende transferências para uma data futura.

### Informações exibidas

* Conta de origem
* Conta de destino
* Valor
* Data de agendamento
* Data prevista para execução
* Status

### Regras de negócio

* A data de execução deve ser futura.
* O valor da transferência deve ser maior que zero.
* A conta de origem deve estar ativa.
* A conta de destino deve estar ativa.
* O sistema deve registrar o agendamento no histórico.
* O sistema deve processar automaticamente as transferências agendadas.
* O saldo será validado apenas no momento da execução da transferência.
* Caso não exista saldo suficiente no momento da execução, a transferência deverá ser marcada como falha.

### Status

* AGENDADA
* PROCESSANDO
* CONCLUIDA
* FALHOU
* CANCELADA

---

## RF020 - Editar Transferência Agendada

### Descrição

Permitir que o cliente altere uma transferência agendada antes da execução.

### Campos permitidos

* Valor
* Data de execução
* Conta de destino

### Regras de negócio

* Apenas transferências com status AGENDADA podem ser alteradas.
* Alterações devem ser registradas para fins de auditoria.
* O sistema deve atualizar a data da última alteração.

---

## RF021 - Cancelar Transferência Agendada

### Descrição

Permitir que o cliente cancele uma transferência agendada.

### Regras de negócio

* Apenas transferências com status AGENDADA podem ser canceladas.
* O cancelamento deve ser registrado no histórico.
* Transferências já processadas não podem ser canceladas.

---

## RF022 - Processamento Automático de Transferências

### Descrição

Permitir que o sistema execute automaticamente transferências agendadas.

### Regras de negócio

* O sistema deve executar o processamento a cada 1 hora.
* O sistema deve validar saldo disponível antes da execução.
* O sistema deve atualizar os saldos das contas envolvidas.
* O sistema deve registrar o resultado da execução no histórico.
* O sistema deve atualizar o status da transferência conforme o resultado do processamento.

---

# Módulo 8 - Cartão de Crédito

## RF023 - Emitir Cartão de Crédito

### Descrição

Permitir que o cliente solicite um cartão de crédito vinculado à conta corrente.

### Regras de negócio

* Apenas clientes com conta corrente podem possuir cartão de crédito.
* O cartão deve possuir limite disponível.
* O cartão deve possuir status.
* O cartão deve estar vinculado a apenas uma conta corrente.

### Status

* ATIVO
* BLOQUEADO
* CANCELADO

---

## RF024 - Realizar Compra no Cartão

### Descrição

Permitir que o cliente realize compras utilizando o limite do cartão.

### Informações exibidas

* Estabelecimento
* Valor da compra
* Data da compra
* Categoria da compra

### Regras de negócio

* O limite disponível deve ser suficiente para realizar a compra.
* O sistema deve reduzir o limite disponível após a compra.
* O sistema deve registrar a compra na fatura do cartão.
* O sistema deve registrar a movimentação no histórico.
* O sistema deve impedir compras em cartões bloqueados ou cancelados.

---

## RF025 - Consultar Fatura

### Descrição

Permitir que o cliente visualize a fatura do cartão.

### Informações exibidas

* Valor total da fatura
* Quantidade de compras
* Limite total
* Limite utilizado
* Limite disponível
* Data de vencimento

### Regras de negócio

* O sistema deve exibir todas as compras pertencentes ao período da fatura.
* O sistema deve calcular automaticamente o valor total da fatura.
* O sistema deve atualizar os valores sempre que uma nova compra for registrada.

---

## RF026 - Análise de Gastos do Cartão

### Descrição

Permitir que o cliente acompanhe seus gastos por categoria e estabelecimento.

### Informações exibidas

* Total gasto por categoria
* Total gasto por estabelecimento
* Percentual de gastos por categoria
* Evolução dos gastos por período

### Exemplos de categorias

* Transporte
* Alimentação
* Mercado
* Saúde
* Lazer
* Streaming
* Outros

### Regras de negócio

* O sistema deve consolidar automaticamente os gastos por categoria.
* O sistema deve permitir filtros por período.
* Os dados devem ser utilizados para composição dos indicadores do dashboard financeiro.
