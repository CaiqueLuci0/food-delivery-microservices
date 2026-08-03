# food-delivery-microservices

Desafio para uma vaga em uma empresa de delivery.

## Enunciado:

Back-end (obrigatório)


Autenticação (fluxo simples contendo):

- Cadastro de usuário (nome, e-mail e senha);

- Login utilizando e-mail e senha;

- Apenas usuários autenticados podem acessar o sistema;

- A forma de autenticação fica a seu critério.



API REST em Java + Spring Boot com endpoints para:

- Criar um pedido (cliente, itens, endereço de entrega);S

- Atualizar o status do pedido, considerando os status:

- RECEBIDO, EM_PREPARO, SAIU_PARA_ENTREGA, ENTREGUE e CANCELADO;

- Listar todos os pedidos e buscar um pedido por ID.


Persistência em SQLite ou similar.


Front-end (obrigatório)

- Aplicação em React que lista os pedidos com seus status atuais e permite criar um novo pedido.


Versionamento (obrigatório)

-Repositório Git com histórico de commits.

## Como rodar localmente

### Pré-requisitos

- Docker e Docker Compose
- Portas **livres** no host (mapeadas pelo compose):

| Porta | Serviço |
|-------|---------|
| `3000` | Frontend (web) |
| `8080` | API / Nginx (gateway) |
| `8001` | user-ms |
| `8002` | restaurant-ms |
| `8003` | catalog-ms |
| `8004` | order-ms |
| `8005` | payment-ms |
| `5433` | PostgreSQL (user-ms) |
| `5434` | PostgreSQL (catalog-ms) |
| `5435` | PostgreSQL (order-ms) |
| `3307` | MySQL (restaurant-ms) |
| `27017` | MongoDB (payment-ms) |
| `5672` | RabbitMQ (AMQP) |
| `4566` | LocalStack (S3) |

### Subir a stack

Na pasta `local-infra`:

```bash
cd local-infra
docker compose up -d --build
```

Aguarde **alguns minutos** após o compose subir: os microserviços precisam ficar healthy e o serviço **`seed`** popula usuários, restaurantes, produtos e imagens. Só então o ambiente estará pronto para uso.

Acompanhar o seed:

```bash
docker compose logs -f seed
```

Quando o log do seed indicar conclusão (o container `food-delivery-seed` encerra), abra:

| URL | Uso |
|-----|-----|
| http://localhost:3000 | Frontend DeliveryBacana |
| http://localhost:8080 | API via Nginx |

### Credenciais dos usuários pré-criados

Contas e senha padrão do seed estão em:

**[`local-infra/seed/CREDENTIALS.md`](./local-infra/seed/CREDENTIALS.md)**

Senha padrão de todos os usuários do seed: `senha123`.

## Arquitetura

A solução foi desenhada como **microserviços** para garantir **alta disponibilidade** e **tolerância a falhas**:

- **Isolamento por domínio** — falha em um serviço (ou no seu banco) não derruba o sistema inteiro.
- **Bancos independentes** por microserviço — cada domínio persiste no próprio datastore.
- **Comunicação assíncrona** via RabbitMQ (AMQP 0-9-1) — publishers e consumers ficam desacoplados; picos e indisponibilidades temporárias são absorvidos por filas.
- **Entrada única** pelo Nginx (reverse proxy) — roteamento HTTP/WebSocket para os MSs e proxy de objetos S3.

### Diagrama C4 (Container)

![C4 Container](./docs/c4-container.png)

## Documentações por microserviço

As **listas individuais de requisitos funcionais e não funcionais** de cada microserviço estão nas respectivas documentações abaixo. Nesses mesmos READMEs também estão os **DERs** (diagramas entidade-relacionamento) ou o modelo de dados (MongoDB no payment-ms).

- [Microserviço de usuários](./user-ms/README.md) — requisitos + DER PostgreSQL
- [Microserviço de restaurantes](./restaurant-ms/README.md) — requisitos + DER MySQL
- [Microserviço de catálogo](./catalog-ms/README.md) — requisitos + DER PostgreSQL
- [Microserviço de pagamento](./payment-ms/README.md) — requisitos + modelo MongoDB
- [Microserviço de pedidos](./order-ms/README.md) — requisitos + DER PostgreSQL

### Requisitos não implementados

Os itens abaixo **não foram implementados** por serem de **prioridade média ou baixa**. A entrega priorizou autenticação, CRUD dos domínios, mensageria, fluxo de pedido/pagamento e o frontend.

| MS | ID | Descrição | Prioridade |
|----|----|-----------|------------|
| user-ms | RF-06 | Endpoint dedicado para consulta de endereço por CEP (o cadastro já valida via ViaCEP no backend; o frontend também consulta ViaCEP) | Média |
| user-ms | RNF-04 | Cache Redis (write-through + lazy loading) | Baixa |
| user-ms | RNF-06 | Armazenar imagens de usuário em S3 | Baixa |
| restaurant-ms | RNF-09 | Cache Redis (write-through + lazy loading) | Baixa |
| catalog-ms | RF-05 | Fotos também para opções de especificação (foto do produto já existe via S3) | Baixa |
| catalog-ms | RNF-02 | Cache Redis (write-through + lazy loading) | Baixa |
| order-ms | RF-10 | Avaliação de pedido com status `ENTREGUE` | Média |
| order-ms | RNF-02 | Cache Redis (write-through + lazy loading) | Baixa |
| payment-ms | RF-02 | Criar produto no Stripe e persistir `stripe_id` | Baixa |
| payment-ms | RF-04 | Endpoint para gerar link de pagamento (Stripe Checkout) | Baixa |
| payment-ms | RF-05 | Webhooks Stripe → `payment-approved` / `payment-failed` | Baixa |
| payment-ms | RNF-05 | Integração completa com Stripe | Baixa |

O pagamento em ambiente local usa o endpoint `PATCH /payment-ms/payments/{orderId}/pay` (sem Stripe).

## Frontend

- [Aplicação web (DeliveryBacana)](./web/README.md)

## RabbitMQ — exchanges e filas

Definições em [`local-infra/rabbitmq/definitions.json`](./local-infra/rabbitmq/definitions.json). Protocolo: **AMQP 0-9-1**.

### Exchanges (fanout — tópicos de publicação)

Os eventos de usuário usam exchanges **fanout** (pub/sub): uma publicação chega a várias filas ligadas ao mesmo exchange.

| Exchange | Tipo | Uso |
|----------|------|-----|
| `user.created` | fanout | Usuário criado (user-ms publica) |
| `user.deleted` | fanout | Usuário deletado (user-ms publica) |
| `user.created.dlx` | fanout | Dead-letter de criação de usuário |
| `user.deleted.dlx` | fanout | Dead-letter de exclusão de usuário |

### Filas

| Fila | Publisher | Consumer | Papel |
|------|-----------|----------|--------|
| `restaurant-ms-user-created-queue` | user-ms (`user.created`) | restaurant-ms | Persiste `user_reference` |
| `restaurant-ms-user-deleted-queue` | user-ms (`user.deleted`) | restaurant-ms | Remove `user_reference` |
| `restaurant-ms-user-created-dlq` | `user.created.dlx` | — | DLQ criação (restaurant-ms) |
| `restaurant-ms-user-deleted-dlq` | `user.deleted.dlx` | — | DLQ exclusão (restaurant-ms) |
| `order-ms-user-created-queue` | user-ms (`user.created`) | order-ms | Persiste `user_reference` |
| `order-ms-user-deleted-queue` | user-ms (`user.deleted`) | order-ms | Remove `user_reference` (mantém pedidos) |
| `order-ms-user-created-dlq` | `user.created.dlx` | — | DLQ criação (order-ms) |
| `order-ms-user-deleted-dlq` | `user.deleted.dlx` | — | DLQ exclusão (order-ms) |
| `restaurant-created` | restaurant-ms | catalog-ms | Cria `restaurant_reference` |
| `restaurant-deleted` | restaurant-ms | catalog-ms | Remove `restaurant_reference` (cascade produtos) |
| `order-created` | order-ms | payment-ms | Cria pagamento ao finalizar pedido |
| `order-deleted` | order-ms | payment-ms | Cancela pagamento no cancelamento do pedido |
| `payment-approved` | payment-ms | order-ms | Marca pagamento `PAGO` e status `AGUARDANDO_RESTAURANTE` |
| `payment-failed` | payment-ms | order-ms | Marca pagamento/pedido como cancelados |
