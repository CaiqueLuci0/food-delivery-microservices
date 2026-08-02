## Requisitos Funcionais

| ID    | Descrição                                                                                                                                                         | Prioridade | Entregue |
|-------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------|----------|
| RF-01 | O sistema deve consumir a fila `order-created` e persistir o pagamento (`id`, `orderId`, `userId`, `status`, `price`)                                             | Alta       | ✅       |
| RF-02 | Ao consumir `order-created`, o sistema deve criar um produto no Stripe via API e persistir o `stripe_id` gerado                                                    | Baixa      |          |
| RF-03 | O sistema deve consumir a fila `order-deleted` e tratar o pagamento correspondente (ex.: cancelar / remover conforme regra de negócio)                             | Alta       | ✅       |
| RF-04 | O sistema deve disponibilizar um endpoint para gerar o link de pagamento do pedido                                                                                | Baixa      |          |
| RF-05 | O sistema deve consumir webhooks de pagamento do Stripe e, conforme o resultado, publicar nas filas `payment-approved` ou `payment-failed`                        | Baixa      |          |
| RF-06 | O sistema deve disponibilizar um endpoint que altera o status do pagamento para `PAGO` e publica em `payment-approved` com payload `{ orderId }`                   | Alta       | ✅       |
| RF-07 | O sistema deve publicar `PaymentApproved` / `PaymentFailed` com payload `{ orderId }` para o order-ms atualizar o status de pagamento                              | Alta       | ✅       |

### Observações
- Store: collection `payment` com `id`, `orderId`, `userId`, `status`, `price`, `stripe_id` (`stripe_id` só entra com a integração Stripe — prioridade baixa).
- Consumo `order-created` (payload do order-ms): `{ id, orderId, userId, status, price }`.
- Consumo `order-deleted` (payload do order-ms no cancel): `{ orderId }` — marca payment como `CANCELADO` se ainda não estiver `PAGO`.
- Publicação: filas `payment-approved` e `payment-failed` com payload `{ orderId }` (contrato já esperado pelo order-ms).
- Por enquanto (sem Stripe): `PATCH /payments/{orderId}/pay` — JWT do cliente; marca `PAGO` e dispara `payment-approved`.
- RF-02 / RF-04 / RF-05: fluxo Stripe completo (produto, link de checkout, webhook) fica para depois.

## Requisitos não Funcionais

| ID     | Descrição                                                                                              | Prioridade | Entregue |
|--------|--------------------------------------------------------------------------------------------------------|------------|----------|
| RNF-01 | Banco de dados MongoDB                                                                                 | Alta       | ✅       |
| RNF-02 | Código em arquitetura hexagonal                                                                        | Alta       | ✅       |
| RNF-03 | Ao iniciar o Spring Boot, o sistema deve rodar as migrations Mongock                                   | Alta       | ✅       |
| RNF-04 | O sistema não deve gerar tokens JWT, apenas decodificá-los utilizando a secret padrão para todos os ms | Alta       | ✅       |
| RNF-05 | Integração com Stripe (API de produto/checkout + webhook)                                              | Baixa      |          |
| RNF-06 | Criar/consumir filas RabbitMQ: `order-created`, `order-deleted`, `payment-approved`, `payment-failed`  | Alta       | ✅       |

## Modelo MongoDB

Collection `payment`: `id`, `orderId`, `userId`, `status`, `price`, `stripe_id`.
