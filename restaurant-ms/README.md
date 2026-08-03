## Tecnologias

| Tecnologia | Uso |
|------------|-----|
| Java 17 | Runtime |
| Spring Boot | Framework HTTP, Security, AMQP |
| Spring Security + JWT (JJWT) | Validação de token (não emite JWT) |
| Spring Data JPA | Persistência |
| MySQL | Banco de dados |
| Liquibase | Migrations |
| RabbitMQ (AMQP) | Consome eventos de usuário; publica `restaurant-created` / `restaurant-deleted` |
| AWS SDK S3 | Upload/remoção de imagem de perfil (LocalStack em local) |
| ViaCEP | Validação/enriquecimento de endereço |
| Geoapify | Geocoding (lat/long) |
| Arquitetura hexagonal | Ports & adapters |

## Endpoints

Paths via Nginx (`http://localhost:8080`).

| Método | Path | Função |
|--------|------|--------|
| POST | `/restaurant-ms/restaurants` | Cria restaurante do usuário autenticado |
| GET | `/restaurant-ms/restaurants` | Lista/busca (`search`, `latitude`, `longitude`) |
| GET | `/restaurant-ms/restaurants/{id}` | Busca restaurante por id |
| PUT | `/restaurant-ms/restaurants/{id}` | Atualiza restaurante (dono) |
| PUT | `/restaurant-ms/restaurants/{id}/image` | Upload multipart da imagem de perfil (S3) |
| DELETE | `/restaurant-ms/restaurants/{id}/image` | Remove imagem de perfil (S3 + `imageKey`) |
| DELETE | `/restaurant-ms/restaurants/{id}` | Exclui restaurante (dono) |

## Requisitos Funcionais

| ID    | Descrição                                                                                            | Prioridade | Entregue |
|-------|------------------------------------------------------------------------------------------------------|------------|----------|
| RF-01 | O sistema deve consumir as filas restaurant-ms-user-created-queue e restaurant-ms-user-deleted-queue | Alta       | ✅       |
| RF-02 | O sistema deve utilizar autenticação bearer via token JWT                                            | Alta       | ✅       |
| RF-03 | O sistema deve publicar nas filas restaurant-created, restaurant-deleted                             | Alta       | ✅       |
| RF-05 | O sistema deve permitir que o restaurante tenha uma foto de perfil                                   | Média      | ✅       |
| RF-06 | O sistema deve possuir endpoints para a CRUD de restaurantes                                         | Alta       | ✅       |
| RF-07 | O sistema deve permitir busca de restaurantes por nome, descrição                                    | Alta       | ✅       |
| RF-08 | O sistema deve permitir busca de restaurantes por localização (latitude, longitude)                  | Média      | ✅       |
| RF-09 | O sistema deve salvar a localização com latitude e longitude                                         | Média      | ✅       |

## Requisitos não Funcionais

| ID     | Descrição                                                                                                                                                                | Prioridade | Entregue |
|--------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------|----------|
| RNF-01 | O sistema deve salvar o id recebido por restaurant-ms-user-created-queue em uma tabela chamada "user_reference"                                                          | Alta       | ✅       |
| RNF-02 | O sistema não deve gerar tokens JWT, apenas decodifica-los utilizando a secret padrão para todos os ms                                                                   | Alta       | ✅       |
| RNF-03 | Utilizar MySQL                                                                                                                                                           | Alta       | ✅       |
| RNF-04 | todas as rotas http exigindo autenticação                                                                                                                                | Alta       | ✅       |
| RNF-05 | O sistema deve armazenar imagens em um bucket S3                                                                                                                         | Média      | ✅       |
| RNF-06 | O sistema deve usar viacep para validar o endereço do restaurante da mesma forma que é feita em User-ms                                                                  | Alta       | ✅       |
| RNF-07 | Código em arquitetura hexagonal                                                                                                                                          | Alta       | ✅       |
| RNF-08 | Ao iniciar o springboot, o sistema deve rodar as migrations liquibase                                                                                                    | Alta       | ✅       |
| RNF-09 | Utilizar mescla dos padrões de cache "write through" e "lazy loading" com Redis                                                                                          | Baixa      |          |
| RNF-10 | Na busca de restaurantes, o sistema recebe "search" e busca esse valor nos campos "description" e "name" da entidade "restaurant"                                        | Alta       | ✅       |
| RNF-11 | Todas as rotinas que alteram dados de restaurantes precisam garantir (via id de usuario no JWT) que o usuário logado é o dono do restaurante (representado por owner_id) | Alta       | ✅       |
| RNF-12 | Ao consumir a fila de usuário novo, o sistema não deve fazer nada caso o usuário já exista no sistema                                                                    | Alta       | ✅       |

## DER MySQL
<img width="828" height="646" alt="image" src="https://github.com/user-attachments/assets/a9d08b6e-3eb0-495e-9e5f-616c38e1061f" />
