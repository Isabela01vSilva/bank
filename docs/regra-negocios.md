# Regras de Negócio

Este documento centraliza as principais regras de negócio do projeto Bank Isabela.

---

# Cliente

## RN001 - CPF Único

- Não é permitido cadastrar dois clientes com o mesmo CPF.

## RN002 - E-mail Único

- Não é permitido cadastrar dois clientes com o mesmo e-mail.

## RN003 - Maioridade

- O cliente deve possuir no mínimo 18 anos para realizar o cadastro.

## RN004 - Telefone

- O telefone é considerado único pela combinação DDD + número.

---

# Conta

## RN005 - Criação Automática

- Ao cadastrar um cliente, o sistema deve criar automaticamente uma ou mais contas conforme a escolha do cliente.

## RN006 - Saldo Inicial

- Toda conta deve ser criada com saldo igual a R$ 0,00.

## RN007 - Tipos de Conta

- O cliente pode possuir:
    - Apenas Conta Corrente;
    - Apenas Conta Poupança;
    - Conta Corrente e Conta Poupança.

## RN008 - Limite de Contas

- O cliente não pode possuir mais de uma conta do mesmo tipo.

## RN009 - Movimentação

- Apenas contas ativas podem realizar movimentações.

## RN010 - Desativação

- Uma conta só pode ser desativada se possuir saldo igual a R$ 0,00.

---

# Movimentações

## RN011 - Valores

- Toda movimentação deve possuir valor maior que zero.

## RN012 - Histórico

- Toda movimentação deve ser registrada automaticamente no histórico.

## RN013 - Auditoria

- Todas as movimentações devem armazenar:
    - Data e hora;
    - Tipo;
    - Valor;
    - Status;
    - Conta relacionada.

---

# Saques

## RN014 - Saldo Suficiente

- O saque somente pode ser realizado caso exista saldo suficiente.

## RN015 - Atualização de Saldo

- O saldo deve ser atualizado imediatamente após a realização do saque.

---

# Depósitos

## RN016 - Atualização de Saldo

- O saldo deve ser atualizado imediatamente após a confirmação do depósito.

---

# Boletos

## RN017 - Valor Mínimo

- Boletos de saque e depósito devem possuir valor mínimo de R$ 50,00.

## RN018 - Status do Boleto

- Os boletos podem possuir os seguintes status:
    - PENDENTE
    - PAGO
    - VENCIDO
    - CANCELADO

## RN019 - Bloqueio de Saldo

- Ao gerar um boleto de saque, o valor deve ficar bloqueado até pagamento, cancelamento ou vencimento.

---

# Transferências

## RN020 - Contas Ativas

- Transferências somente podem ocorrer entre contas ativas.

## RN021 - Saldo Suficiente

- A conta de origem deve possuir saldo suficiente para realizar a transferência.

## RN022 - Histórico

- A transferência deve ser registrada no histórico das contas envolvidas.

---

# Agendamentos

## RN023 - Data Futura

- Transferências agendadas devem possuir data futura.

## RN024 - Processamento

- O sistema deve processar transferências agendadas periodicamente.

## RN025 - Cancelamento

- Apenas transferências com status AGENDADA podem ser canceladas.

---

# Cartão de Crédito

## RN026 - Conta Corrente

- Apenas clientes que possuam conta corrente podem solicitar cartão de crédito.

## RN027 - Limite

- Compras não podem ultrapassar o limite disponível.

## RN028 - Fatura

- Toda compra realizada deve ser registrada na fatura vigente.

---

# Dashboard

## RN029 - Indicadores

- Os indicadores financeiros devem ser calculados com base nas movimentações registradas.

## RN030 - Comparativos

- Os comparativos devem utilizar como referência o período imediatamente anterior.

## RN031 - Período Máximo

- Caso a conta possua mais de 12 meses de existência, os gráficos devem exibir apenas os últimos 12 meses.