## Requisitos Funcionais

| ID    | Descrição                                                                                                                                                                                                                                | Prioridade | Entregue |
|-------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------|--------|
| RF-01 | Necessário garantir que o usuário que criou o pedido seja o mesmo que está tentando alterar ou deletar o pedido                                                                                                                          | Alta       |      |
| RF-02 | O pedido deve possuir multiplos status                                                                                                                                                                                                   | Alta       |      |
| RF-04 | O sistema deve apagar user_reference mas manter os pedidos feitos pelo usuário ao consumir user-deleted                                                                                                                                  | Alta       |      |
| RF-05 | O sistema deve consumir as filas order-ms-user-created-queue e order-ms-user-deleted-queue                                                                                                                                               | Alta       |      |
| RF-06 | O sistema deve disponibilizar um endpoint para finalizar pedido acessível apenas para o usuário que abriu o pedido. Isso muda o status de EM_CADASTRAMENTO para AGUARDANDO_PAGAMENTO                                                     | Alta       |      |
| RF-07 | O sistema deve consumir as filas PaymentApproved, PaymentFailed e atualizar o status de pagamento do pedido                                                                                                                              | Alta       |       |
| RF-08 | O sistema deve disponibilizar um endpoint para atualizar o status do pedido acessível apenas para o dono do restaurante. Isso muda o status de AGUARDANDO_PAGAMENTO para PREPARANDO, SAIU_PARA_ENTREGA, ENTREGADOR_NO_LOCAL e ENTREGUE   | Alta       |       |
| RF-09 | order creation -> receive products ids and spec_options array of ids, retrieve data from catalog-ms(http), recebe o product e as spec_option requisitadas, stores order data from product and restaurant retrieved from the http request | Alta       | |
| RF-10 | O usuário pode avaliar um pedido com status ENTREGUE                                                                                                                                                                                     | Média      | |

### Observações
- Status do pedido:
  - Responsabilidade do order-ms 
    - Antes de finalizar (apenas cliente), 1 endpoints (recebe orderId (se tiver), productId, specOptionIds e um dos dois status. RF-09):
      - EM_CADASTRAMENTO: status inicial do pedido, quando o cliente está montando o pedido.
      - AGUARDANDO_PAGAMENTO: status do pedido quando o cliente finaliza o pedido. Quando o pedido muda para esse status o sistema deve publicar "payment (id, orderId, userId, status, price)" na fila order-created
    - Após pagamento (apenas vendedor controla), 1 endpoit apenas: 
      - PREPARANDO: status do pedido quando o pagamento é aprovado e o restaurante está preparando o pedido.
      - SAIU_PARA_ENTREGA: status do pedido quando o restaurante finaliza o pedido e envia para entrega.
      - ENTREGADOR_NO_LOCAL: status do pedido quando o entregador chega no local do cliente.
      - ENTREGUE: status do pedido quando o entregador finaliza a entrega.
    - CANCELADO: status do pedido quando o cliente ou o dono do restaurante cancela o pedido.
    - Obs: o cliente só pode cancelar enquanto o pedido estiver no status EM_CADASTRAMENTO ou AGUARDANDO_PAGAMENTO. Após, apenas o dono do restaurante pode cancelar.
- Status de pagamento (Atualizações recebidas do payment-ms):
    - PAGO: status do pedido quando o pagamento é aprovado.
    - CANCELADO: status do pedido quando o pagamento é recusado.

## Requisitos não Funcionais

| ID     | Descrição                                                                                                                                            | Prioridade | Entregue |
|--------|------------------------------------------------------------------------------------------------------------------------------------------------------|------------|----------|
| RNF-02 | Utilizar mescla dos padrões de cache "write through" e "lazy loading" com Redis                                                                      | Baixa      |          |
| RNF-03 | Banco de dados PostgreSQL                                                                                                                            | Alta       |        |
| RNF-04 | Quando um pedido for atualizado, deletar as entidades da snapshot anterior                                                                           | Alta       |        |
| RNF-06 | Código em arquitetura hexagonal                                                                                                                      | Alta       |        |
| RNF-07 | Ao iniciar o springboot, o sistema deve rodar as migrations liquibase                                                                                | Alta       |        |
| RNF-08 | Apenas o dono do restaurante (order.restaurant_owner_id === jwt.id) ou o cliente que realizou o pedido (order.client_id === jwt id) podem cancela-lo | Alta       |        |
| RNF-09 | O sistema não deve gerar tokens JWT, apenas decodifica-los utilizando a secret padrão para todos os ms                                               | Alta       |        |
| RNF-10 | Migrations Liquibase em XML                                                                                                                          | Alta       |        |
| RNF-11 | Criar filas rabbitMQ que consomem os tópicos delete e create do user-ms e as filas de payment-approved e payment-failed                              | Alta       |          |
| RNF-12 | WebSocket para acompanhar o status do pedido                                                                                                         | Média      |          |
| RNF-13 | O sistema recebe apenas id do pedido nas filas de payment-approved e payment-failed, então o order-ms deve buscar o pedido no banco e atualizar o status de pagamento do pedido. | Alta | |
## DER PostgreSQL
<img width="1001" height="479" alt="image" src="https://github.com/user-attachments/assets/3324640b-340f-4bbc-9dad-f54023f22449" />


### Observações
- User_reference vem da fila de usuário criado.
- as entidades product_snapshot, spec_option_snapshot e os seguintes dados da entidade order: restaurant_id, restaurant_owner_id; são recuperados na requisição que o order-ms faz para catalog-ms durante a criação do pedido.
