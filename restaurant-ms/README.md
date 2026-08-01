## Requisitos Funcionais

| ID    | Descrição                                                                                            | Prioridade |
|-------|------------------------------------------------------------------------------------------------------|------------|
| RF-01 | O sistema deve consumir as filas restaurant-ms-user-created-queue e restaurant-ms-user-deleted-queue | Alta       |
| RF-02 | O sistema deve utilizar autenticação bearer via token JWT                                            | Alta       |
| RF-03 | O sistema deve publicar nas filas restaurant-created, restaurant-deleted                             | Alta       |
| RF-05 | O sistema deve permitir que o restaurante tenha uma foto de perfil                                   | Média      |
| RF-06 | O sistema deve possuir endpoints para a CRUD de restaurantes                                         | Alta       |
| RF-07 | O sistema deve permitir busca de restaurantes por nome, descrição                                    | Alta       |
| RF-08 | O sistema deve permitir busca de restaurantes por localização (latitude, longitude)                  | Média      |
| RF-09 | O sistema deve salvar a localização com latitude e longitude                                         | Média      |

## Requisitos não Funcionais

| ID     | Descrição                                                                                                                                                                | Prioridade |
|--------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------|
| RNF-01 | O sistema deve salvar o id recebido por restaurant-ms-user-created-queue em uma tabela chamada "user_reference"                                                          | Alta       |
| RNF-02 | O sistema não deve gerar tokens JWT, apenas decodifica-los utilizando a secret padrão para todos os ms                                                                   | Alta       |
| RNF-03 | Utilizar PostgreSQL                                                                                                                                                      | Alta       |
| RNF-04 | todas as rotas http exigindo autenticação                                                                                                                                | Alta       |
| RNF-05 | O sistema deve armazenar imagens em um bucket S3                                                                                                                         | Média      |
| RNF-06 | O sistema deve usar viacep para validar o endereço do restaurante da mesma forma que é feita em User-ms                                                                  | Alta       |
| RNF-07 | Código em arquitetura hexagonal                                                                                                                                          | Alta       |
| RNF-08 | Ao iniciar o springboot, o sistema deve rodar as migrations liquibase                                                                                                    | Alta       |
|RNF-09 | Utilizar mescla dos padrões de cache "write through" e "lazy loading" com Redis                                                                                          | Baixa      |
| RNF-10 | Na busca de restaurantes, o sistema recebe "search" e busca esse valor nos campos "description" e "name" da entidade "restaurant"                                        | Alta       |
| RNF-11 | Todas as rotinas que alteram dados de restaurantes precisam garantir (via id de usuario no JWT) que o usuário logado é o dono do restaurante (representado por owner_id) | Alta       |
