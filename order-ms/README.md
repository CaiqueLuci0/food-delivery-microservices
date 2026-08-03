## Requisitos Funcionais

| ID    | Descrição                                                                                                                                                                                                                                | Prioridade | Entregue |
|-------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------|----------|
| RF-01 | Necessário garantir que o usuário que criou o pedido seja o mesmo que está tentando alterar os itens ou deletar o pedido                                                                                                                 | Alta       | ✅       |
| RF-02 | O pedido deve possuir multiplos status                                                                                                                                                                                                   | Alta       | ✅       |
| RF-04 | O sistema deve apagar user_reference mas manter os pedidos feitos pelo usuário ao consumir user-deleted                                                                                                                                  | Alta       | ✅       |
| RF-05 | O sistema deve consumir as filas order-ms-user-created-queue e order-ms-user-deleted-queue                                                                                                                                               | Alta       | ✅       |
| RF-06 | O sistema deve disponibilizar um endpoint para finalizar pedido acessível apenas para o usuário que abriu o pedido. Isso muda o status de EM_CADASTRAMENTO para AGUARDANDO_PAGAMENTO                                                     | Alta       | ✅       |
| RF-07 | O sistema deve consumir as filas PaymentApproved, PaymentFailed e atualizar o status de pagamento do pedido                                                                                                                              | Alta       | ✅       |
| RF-08 | O sistema deve disponibilizar um endpoint para atualizar o status do pedido acessível apenas para o dono do restaurante. Isso muda o status entre `AGUARDANDO_RESTAURANTE`, `PREPARANDO`, `SAIU_PARA_ENTREGA`, `ENTREGADOR_NO_LOCAL` e `ENTREGUE`   | Alta       | ✅       |
| RF-09 | order creation -> receive products ids and spec_options array of ids, retrieve data from catalog-ms(http), recebe o product e as spec_option requisitadas, stores order data from product and restaurant retrieved from the http request | Alta       | ✅       |
| RF-10 | O usuário pode avaliar um pedido com status ENTREGUE                                                                                                                                                                                     | Média      |          |
| RF-11 | O sistema deve disponibilizar um endpoint para o cliente acompanhar o status do pedido em tempo real                                                                                                                                     | Média      |          |

### Observações
- Status do pedido (RF-02):
  - Antes de finalizar (apenas cliente):
    - `POST /orders` — cria pedido em `EM_CADASTRAMENTO` com `items: [{ productId, specOptionIds[] }]`
    - `PUT /orders/{id}` — atualiza items (só `EM_CADASTRAMENTO`); substitui snapshots (RNF-04)
    - `PATCH /orders/{id}/finalize` — `EM_CADASTRAMENTO` → `AGUARDANDO_PAGAMENTO` e publica `{ id, orderId, userId, status, price }` na fila `order-created` (`price` calculado dos snapshots; não há coluna price em order)
  - Após pagamento (apenas dono do restaurante):
    - `PUT /orders/{id}/status` — livre entre `AGUARDANDO_RESTAURANTE`, `PREPARANDO`, `SAIU_PARA_ENTREGA`, `ENTREGADOR_NO_LOCAL`, `ENTREGUE` (exige `payment_status=PAGO`)
  - `POST /orders/{id}/cancel`:
    - Cliente: só em `EM_CADASTRAMENTO` ou `AGUARDANDO_PAGAMENTO`
    - Dono: nos status posteriores (`AGUARDANDO_RESTAURANTE` em diante)
  - Items de um pedido devem ser do **mesmo restaurante**. A validação é feita no **catalog-ms** (`POST /products/resolve` → 409). O order-ms apenas interpreta o status HTTP.
- Status de pagamento (filas `payment-approved` / `payment-failed`, payload `{ orderId }`):
  - Approved: `payment_status=PAGO` e `order.status=AGUARDANDO_RESTAURANTE`
  - Failed: `payment_status=CANCELADO` **e** `order.status=CANCELADO`
- Leitura (`GET /orders/{id}`, `GET /orders`): sem roles no JWT — compara com `client_id` e `restaurant_owner_id`. Sem `restaurantId`: pedidos do cliente. Com `?restaurantId=`: pedidos do restaurante cujo owner é o JWT.

## Requisitos não Funcionais

| ID     | Descrição                                                                                                                                            | Prioridade | Entregue |
|--------|------------------------------------------------------------------------------------------------------------------------------------------------------|------------|---------|
| RNF-02 | Utilizar mescla dos padrões de cache "write through" e "lazy loading" com Redis                                                                      | Baixa      |         |
| RNF-03 | Banco de dados PostgreSQL                                                                                                                            | Alta       | ✅       |
| RNF-04 | Quando um pedido for atualizado, deletar as entidades da snapshot anterior                                                                           | Alta       | ✅       |
| RNF-06 | Código em arquitetura hexagonal                                                                                                                      | Alta       | ✅       |
| RNF-07 | Ao iniciar o springboot, o sistema deve rodar as migrations liquibase                                                                                | Alta       | ✅       |
| RNF-08 | Apenas o dono do restaurante (order.restaurant_owner_id === jwt.id) ou o cliente que realizou o pedido (order.client_id === jwt id) podem cancela-lo | Alta       | ✅       |
| RNF-09 | O sistema não deve gerar tokens JWT, apenas decodifica-los utilizando a secret padrão para todos os ms                                               | Alta       | ✅       |
| RNF-10 | Migrations Liquibase em XML                                                                                                                          | Alta       | ✅       |
| RNF-11 | Criar filas rabbitMQ que consomem os tópicos delete e create do user-ms e as filas de payment-approved e payment-failed                              | Alta       | ✅       |
| RNF-12 | WebSocket para acompanhar o status do pedido                                                                                                         | Média      | ✅        |
| RNF-13 | O sistema recebe apenas id do pedido nas filas de payment-approved e payment-failed, então o order-ms deve buscar o pedido no banco e atualizar o status de pagamento do pedido. | Alta | ✅       |

## DER PostgreSQL
<img width="1001" height="479" alt="image" src="https://github.com/user-attachments/assets/3324640b-340f-4bbc-9dad-f54023f22449" />

### Observações
- User_reference vem da fila de usuário criado.
- `product_snapshot`, `spec_option_snapshot` e `order.restaurant_id` / `restaurant_owner_id` vêm do resolve do catalog-ms na criação/atualização do pedido.
- `client_id` FK → `user_reference` com ON DELETE SET NULL (user-deleted remove a reference e mantém os pedidos).
