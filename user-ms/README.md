## Requisitos Funcionais

| ID    | Descrição                                                                                                 | Prioridade |
|-------|-----------------------------------------------------------------------------------------------------------|------------|
| RF-01 | O sistema deve disponibilizar endpoints para CRUD de usuários.                                            | Alta       |
| RF-02 | O sistema deve exigir endereço no cadastro de usuários                                                    | Alta       |
| RF-03 | O sistema deve publicar em um tópico (broker) uma mensagen quando usuários forem criados ou deletados     | Alta       |
| RF-04 | O usuário pode fazer login com email e senha                                                              | Alta       |
| RF-05 | O sistema deve permitir que o usuário tenha uma foto de perfil                                            | Baixa      |
| RF-06 | O sistema deve disponibilizar endpoint para retornar informações de endereço de acordo com o CEP recebido | Média      |



## Requisitos não Funcionais
| ID     | Descrição                                                                                                      | Prioridade |
|--------|----------------------------------------------------------------------------------------------------------------|------------|
| RNF-01 | As senhas dos usuários devem ser armazenadas utilizando algoritmo de hash seguro (ex.: BCrypt).                | Alta       | 
| RNF-02 | Todas as rotas protegidas da API devem exigir autenticação via JWT.                                            | Alta       |
| RNF-03 | O sistema deve usar PostgreSQL                                                                                 | Alta       |
| RNF-04 | Utilizar mescla dos padrões de cache "write through" e "lazy loading" com Redis                                          | Baixa      |
| RNF-05 | O sistema deve usar RabbitMQ para publicar filas e tópicos                                                     | Alta       |
| RNF-06 | O Sistema deve armazenar imagens em um bucket S3                                                               | Baixa      |
| RNF-07 | O sistema deve se conectar com a API do VIA CEP para validar o endereço no cadastro                            | Média      |
| RNF-08 | O sistema deve salvar em banco a geolocalização de todos os endereços (lat, long). Utilizar alguma API externa | Baixa      |
| RNF-09 | Código em arquitetura hexagonal                                                                                | Alta |
| RNF-10 | Ao iniciar o springboot, o sistema deve rodar as migrations liquibase                                                                               | Alta |

## DER PostgreSQL
<img width="680" height="442" alt="image" src="https://github.com/user-attachments/assets/53baba0a-1a4b-450e-af5c-28b7b9b82325" />


