## Tecnologias

| Tecnologia | Uso |
|------------|-----|
| Java 17 | Runtime |
| Spring Boot | Framework HTTP, Security, AMQP |
| Spring Security + JWT (JJWT) | Autenticação e emissão de token |
| Spring Data JPA | Persistência |
| PostgreSQL | Banco de dados |
| Liquibase | Migrations |
| RabbitMQ (AMQP) | Publicação de eventos `user.created` / `user.deleted` |
| ViaCEP | Validação/enriquecimento de endereço |
| Geoapify | Geocoding (lat/long) |
| Arquitetura hexagonal | Ports & adapters |

## Endpoints

Paths via Nginx (`http://localhost:8080`).

| Método | Path | Função |
|--------|------|--------|
| POST | `/user-ms/auth/login` | Login com e-mail e senha; retorna JWT |
| GET | `/user-ms/auth/islogged` | Valida se o token ainda está autenticado |
| POST | `/user-ms/users` | Cadastro de usuário (público) com endereço |
| GET | `/user-ms/users` | Lista usuários |
| GET | `/user-ms/users/{id}` | Busca usuário por id |
| PUT | `/user-ms/users/{id}` | Atualiza nome e endereço (dono do recurso) |
| DELETE | `/user-ms/users/{id}` | Exclui usuário (dono do recurso) |

## Requisitos Funcionais

| ID    | Descrição                                                                                                 | Prioridade | Entregue |
|-------|-----------------------------------------------------------------------------------------------------------|------------|----------|
| RF-01 | O sistema deve disponibilizar endpoints para CRUD de usuários.                                            | Alta       | ✅       |
| RF-02 | O sistema deve exigir endereço no cadastro de usuários                                                    | Alta       | ✅       |
| RF-03 | O sistema deve publicar em um tópico (broker) uma mensagen quando usuários forem criados ou deletados     | Alta       | ✅       |
| RF-04 | O usuário pode fazer login com email e senha                                                              | Alta       | ✅       |
| RF-06 | O sistema deve disponibilizar endpoint para retornar informações de endereço de acordo com o CEP recebido | Média      |          |



## Requisitos não Funcionais
| ID     | Descrição                                                                          | Prioridade | Entregue |
|--------|------------------------------------------------------------------------------------|------------|----------|
| RNF-01 | As senhas dos usuários devem ser armazenadas utilizando algoritmo de hash seguro (ex.: BCrypt). | Alta       | ✅       |
| RNF-02 | Todas as rotas protegidas da API devem exigir autenticação via JWT.                | Alta       | ✅       |
| RNF-03 | O sistema deve usar PostgreSQL                                                     | Alta       | ✅       |
| RNF-04 | Utilizar mescla dos padrões de cache "write through" e "lazy loading" com Redis    | Baixa      |          |
| RNF-05 | O sistema deve usar RabbitMQ para publicar filas e tópicos                         | Alta       | ✅       |
| RNF-06 | O Sistema deve armazenar imagens em um bucket S3                                   | Baixa      |          |
| RNF-07 | O sistema deve se conectar com a API do VIA CEP para validar o endereço no cadastro | Média      | ✅       |
| RNF-08 | O sistema deve salvar em banco a geolocalização de todos os endereços (lat, long). Utilizar alguma API externa | Baixa      | ✅       |
| RNF-09 | Código em arquitetura hexagonal                                                    | Alta       | ✅       |
| RNF-10 | Ao iniciar o springboot, o sistema deve rodar as migrations liquibase              | Alta       | ✅       |
| RNF-11 | Todas as rotinas que alteram dados de usuários precisam garantir (via id de usuario no JWT) que o usuário logado é o dono do usuário (representado por owner_id) | Alta       | ✅       |

## DER PostgreSQL
<img width="680" height="442" alt="image" src="https://github.com/user-attachments/assets/53baba0a-1a4b-450e-af5c-28b7b9b82325" />
