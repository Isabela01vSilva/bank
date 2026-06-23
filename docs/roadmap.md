# Roadmap

Este documento apresenta o planejamento de evolução do projeto Bank Isabela.

Legenda:

- ✅ Concluído
- 🚧 Em desenvolvimento
- 📅 Planejado

---

# ✅ Fase 1 - Core Bancário

## Cliente

- [✅] RF001 - Cadastro de clientes
- [✅] RF002 - Consulta de clientes
- [✅] RF003 -Atualização de clientes

## Conta

- [✅] RF004 - Criação automática de conta
- [✅] RF005 - Consultar Conta
- [✅] RF006 - Tipos de Contas (Poupança e Corrente)
- [✅] RF007 - Ativar ou Encerrar Conta
- [✅] RF008 - Abertura de Nova Conta
- [✅] RF009 - Regras de Status do Cliente (ATIVO/INATIVO)

## Movimentações

- [✅] RF010 - Validação de Conta para Movimentações
- [✅] RF011 - Saque
- [✅] RF012 - Depósito

## Histórico

- [✅] RF013 - Consultar Histórico do Cliente
- [✅] RF014 - Consultar Histórico por Tipo de Conta
- [✅] RF015 - Consultar Histórico por Tipo de Movimentação
- [✅] RF016 - Consultar Histórico por Período
- [✅] RF017 - Consultar Extrato Consolidado do Cliente

---

# 🚧 Fase 2 - Transferências

## Transferências

- [ ] RF018 - Realizar Transferência 
- [ ] RF019 - Histórico de transferências

---

### Fase 2.1 - Qualidade e Padronização

- [ ] Tratamento global de exceções
- [ ] Padronização das mensagens de erro
- [ ] Padronização das respostas da API
- [ ] Validações avançadas
- [ ] Melhorias de código

---

# 📅 Fase 3 - Agendamentos

## Scheduler

- [ ] Integração com Schedule
- [ ] Agendamento de transferências
- [ ] Cancelamento de agendamentos
- [ ] Edição de agendamentos
- [ ] Processamento automático

---

# 📅 Fase 4 - Dashboard Financeiro

## Indicadores

- [ ] Saldo atual
- [ ] Entradas do mês
- [ ] Saídas do mês
- [ ] Economia do mês
- [ ] Economia acumulada

## Comparativos

- [ ] Comparação com mês anterior
- [ ] Indicadores percentuais

## Gráficos

- [ ] Evolução financeira
- [ ] Entradas por período
- [ ] Saídas por período
- [ ] Economia por período

---

# 📅 Fase 5 - Boletos

## Boleto de Depósito

- [ ] Geração de boleto
- [ ] Pagamento de boleto
- [ ] Controle de status

## Boleto de Saque

- [ ] Geração de boleto
- [ ] Bloqueio de saldo
- [ ] Cancelamento
- [ ] Expiração automática

---

# 📅 Fase 6 - Cartão de Crédito

## Cartão

- [ ] Emissão de cartão
- [ ] Controle de limite
- [ ] Bloqueio de cartão
- [ ] Cancelamento de cartão

## Compras

- [ ] Registro de compras
- [ ] Controle de limite utilizado

## Fatura

- [ ] Geração de fatura
- [ ] Consulta de fatura

## Análise de Gastos

- [ ] Gastos por categoria
- [ ] Gastos por estabelecimento
- [ ] Indicadores financeiros

---

# Ideias Futuras

- [ ] PIX
- [ ] Metas financeiras
- [ ] Investimentos
- [ ] Notificações por e-mail
- [ ] Exportação de extrato em PDF ou EXCEL