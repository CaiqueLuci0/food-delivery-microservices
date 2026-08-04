# Credenciais do seed (`local-infra/seed/seed.sh`)

Senha padrão de todos os usuários do seed: **`senha123`**

(Override opcional via `SEED_PASSWORD`.)

## Donos de restaurante

| Nome | Email | Restaurante | Produtos |
|------|-------|-------------|----------|
| Ana Pizza | `ana.pizza@seed.local` | Pizzaria Centro Seed | 5 pizzas |
| Bruno Burger | `bruno.burger@seed.local` | Burger House Seed | 5 burgers |
| Carla Sushi | `carla.sushi@seed.local` | Sushi Bar Seed | 5 pratos |

## Cliente (sem restaurante)

| Nome | Email |
|------|-------|
| Diego Cliente | `diego.cliente@seed.local` |

## Execução automática

O serviço **`seed`** no [docker-compose.yml](../docker-compose.yml) sobe depois do `nginx` e popula a base sozinho:

```bash
cd local-infra
docker compose up --build
```

Acompanhar o seed:

```bash
docker compose logs -f seed
```

O container `food-delivery-seed` encerra após o script (`restart: "no"`). Ao terminar com sucesso, grava `/tmp/seed.done` no filesystem do container; um novo `compose up` no **mesmo** container detecta o arquivo e pula o seed. `compose down` remove o container — na próxima subida o seed roda de novo.

Rodar o seed de novo sem derrubar a stack (container novo):

```bash
docker compose run --rm seed
```

Ou manualmente no host (com a stack no ar):

```bash
./seed/seed.sh
```

Variáveis úteis: `BASE_URL`, `SEED_PASSWORD`, `MAX_WAIT_SEC`, `RETRY_SLEEP_SEC`, `SEED_IMAGES_DIR`.

Imagens em [`seed/images`](./images): `profile_pics/` (perfil do restaurante) e `products/` (foto de cada produto). O seed envia multipart (`curl -F file=@...`) para `PUT .../image` no restaurant-ms / catalog-ms.

Login de exemplo:

```bash
curl -s -X POST http://localhost:8080/user-ms/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"ana.pizza@seed.local","password":"senha123"}'
```
