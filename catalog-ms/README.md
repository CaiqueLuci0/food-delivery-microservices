## Requisitos Funcionais

| ID    | Descrição                                                                                                  | Prioridade | Entregue |
|-------|------------------------------------------------------------------------------------------------------------|------------|----------|
| RF-01 | O sistema deve salvar restaurant_reference(restaurant_id, owner_id) ao consumir a fila de novo restaurante | Alta       | ✅       |
| RF-02 | O produto pode ou não ter especificações                                                                   | Alta       | ✅       |
| RF-03 | O usuário deve poder fazer todas as rotinas de CRUD de produtos para o seu restaurante                     | Alta       | ✅       |
| RF-04 | O usuário deve poder buscar produtos por restaurante, nome e descrição                                     | Alta       | ✅       |
| RF-05 | O sistema deve permitir que os produtos e opções de especificações tenha uma fotos                         | Baixa      |          |
| RF-06 | O sistema deve disponibilizar um endpoint que retorna o product e as spec_option requisitadas              | Alta       | ✅       |
| RF-07 | O sistema deve apagar restaurant_reference (e produtos em cascade no banco) ao consumir restaurant-deleted | Alta       | ✅       |

### Observações
RF-06: O endpoint recebe um array de spec_option_id e um product_id e retorna o product e as spec_option requisitadas. Caso não encontre um dos ids listados (product ou spec), retorna 404.

RF-07: Consumo da fila `restaurant-deleted` com payload `{ restaurantId }`. A exclusão da `restaurant_reference` dispara cascade no PostgreSQL para `product` → `specification` → `spec_option` (RNF-01).

## Requisitos não Funcionais

| ID     | Descrição                                                                                                                                                            | Prioridade | Entregue |
|--------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------|----------|
| RNF-01 | Delete cascade EM BANCO (não via annotation spring) restaurant_reference -> products -> specification -> spec_option                                                 | Alta       | ✅       |
| RNF-02 | Utilizar mescla dos padrões de cache "write through" e "lazy loading" com Redis                                                                                      | Baixa      |          |
| RNF-03 | Banco de dados PostgreSQL                                                                                                                                            | Alta       | ✅       |
| RNF-04 | Garantir que o sistema não faça nada quando receber uma mensagem que contenha um restaurante já registrado                                                           | Alta       | ✅       |
| RNF-05 | Salvar imagens de produtos e especificações em um bucket S3                                                                                                          | Baixa      |          |
| RNF-06 | O sistema deve disponibilizar endpoint para retornar informações de endereço de acordo com o CEP recebido                                                            | Média      |          |
| RNF-07 | Código em arquitetura hexagonal                                                                                                                                      | Alta       | ✅       |
| RNF-08 | Ao iniciar o springboot, o sistema deve rodar as migrations liquibase                                                                                                | Alta       | ✅       |
| RNF-09 | Todas as rotinas que alteram dados de produtos precisam garantir (via id de usuario no JWT) que o usuário logado é o dono do restaurante (representado por owner_id) | Alta       | ✅       |
| RNF-10 | O sistema não deve gerar tokens JWT, apenas decodifica-los utilizando a secret padrão para todos os ms                                                               | Alta       | ✅       |
| RNF-11 | Migrations Liquibase em XML                                                                                                                                          | Alta       | ✅       |

## DER PostgreSQL

<img width="743" height="600" alt="image" src="https://github.com/user-attachments/assets/5683cfb1-47a0-44f4-a6e8-4bd9a3131bbd" />
