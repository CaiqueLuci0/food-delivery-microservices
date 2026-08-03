#!/usr/bin/env bash
set -euo pipefail

BASE_URL="${BASE_URL:-http://localhost:8080}"
PASSWORD="${SEED_PASSWORD:-senha123}"
MAX_WAIT_SEC="${MAX_WAIT_SEC:-180}"
RETRY_SLEEP_SEC="${RETRY_SLEEP_SEC:-2}"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
IMAGES_DIR="${SEED_IMAGES_DIR:-${SCRIPT_DIR}/images}"

log() { printf '[seed] %s\n' "$*" >&2; }
die() { printf '[seed] ERROR: %s\n' "$*" >&2; exit 1; }

need_cmd() {
  command -v "$1" >/dev/null 2>&1 || die "comando obrigatório não encontrado: $1"
}

need_cmd curl
need_cmd jq

mime_for() {
  case "${1##*.}" in
    jpg|jpeg|JPG|JPEG) printf 'image/jpeg' ;;
    png|PNG) printf 'image/png' ;;
    webp|WEBP) printf 'image/webp' ;;
    gif|GIF) printf 'image/gif' ;;
    *) printf 'image/jpeg' ;;
  esac
}

http_code() {
  local method="$1" url="$2" data="${3:-}" auth="${4:-}"
  local args=(-sS -o /tmp/seed-response.json -w '%{http_code}' -X "$method" "$url" -H 'Content-Type: application/json')
  if [[ -n "$auth" ]]; then
    args+=(-H "Authorization: Bearer $auth")
  fi
  if [[ -n "$data" ]]; then
    args+=(-d "$data")
  fi
  curl "${args[@]}"
}

wait_gateway() {
  local elapsed=0
  log "Aguardando gateway em ${BASE_URL} (até ${MAX_WAIT_SEC}s)..."
  while (( elapsed < MAX_WAIT_SEC )); do
    if curl -sS -o /dev/null -w '%{http_code}' "${BASE_URL}/user-ms/users" | grep -Eq '401|403|200|400'; then
      log "Gateway disponível."
      return 0
    fi
    sleep "$RETRY_SLEEP_SEC"
    elapsed=$((elapsed + RETRY_SLEEP_SEC))
  done
  die "timeout aguardando gateway"
}

create_user() {
  local name="$1" email="$2" cep="$3" logradouro="$4" numero="$5" bairro="$6"
  local body
  body=$(jq -n \
    --arg name "$name" \
    --arg email "$email" \
    --arg password "$PASSWORD" \
    --arg cep "$cep" \
    --arg logradouro "$logradouro" \
    --arg numero "$numero" \
    --arg bairro "$bairro" \
    '{
      name: $name,
      email: $email,
      password: $password,
      address: {
        cep: $cep,
        logradouro: $logradouro,
        numero: $numero,
        complemento: "Seed",
        bairro: $bairro,
        cidade: "São Paulo",
        uf: "SP",
        referencia: "local-infra seed"
      }
    }')
  local code
  code=$(http_code POST "${BASE_URL}/user-ms/users" "$body")
  if [[ "$code" == "201" ]]; then
    log "Usuário criado: $email"
    jq -r '.id' /tmp/seed-response.json
    return 0
  fi
  if [[ "$code" == "409" ]] || [[ "$code" == "400" ]]; then
    log "Usuário pode já existir ($code): $email — seguindo com login"
    echo ""
    return 0
  fi
  die "falha ao criar usuário $email (HTTP $code): $(cat /tmp/seed-response.json)"
}

login() {
  local email="$1"
  local body
  body=$(jq -n --arg email "$email" --arg password "$PASSWORD" '{email: $email, password: $password}')
  local code
  code=$(http_code POST "${BASE_URL}/user-ms/auth/login" "$body")
  [[ "$code" == "200" ]] || die "login falhou para $email (HTTP $code): $(cat /tmp/seed-response.json)"
  jq -r '.token' /tmp/seed-response.json
}

create_restaurant() {
  local token="$1" name="$2" description="$3" cep="$4" logradouro="$5" numero="$6" bairro="$7"
  local body
  body=$(jq -n \
    --arg name "$name" \
    --arg description "$description" \
    --arg cep "$cep" \
    --arg logradouro "$logradouro" \
    --arg numero "$numero" \
    --arg bairro "$bairro" \
    '{
      name: $name,
      description: $description,
      address: {
        cep: $cep,
        logradouro: $logradouro,
        numero: $numero,
        complemento: "Loja seed",
        bairro: $bairro,
        cidade: "São Paulo",
        uf: "SP",
        referencia: "local-infra seed"
      }
    }')
  local code
  code=$(http_code POST "${BASE_URL}/restaurant-ms/restaurants" "$body" "$token")
  if [[ "$code" == "201" ]]; then
    jq -r '.id' /tmp/seed-response.json
    return 0
  fi
  if [[ "$code" == "409" ]]; then
    log "Restaurante já existe para este owner — buscando na listagem"
    code=$(http_code GET "${BASE_URL}/restaurant-ms/restaurants" "" "$token")
    [[ "$code" == "200" ]] || die "não foi possível listar restaurantes (HTTP $code)"
    local id
    id=$(jq -r --arg name "$name" 'map(select(.name == $name)) | .[0].id // empty' /tmp/seed-response.json)
    [[ -n "$id" ]] || id=$(jq -r '.[0].id // empty' /tmp/seed-response.json)
    [[ -n "$id" ]] || die "restaurante existente não encontrado para $name"
    echo "$id"
    return 0
  fi
  die "falha ao criar restaurante $name (HTTP $code): $(cat /tmp/seed-response.json)"
}

upload_image() {
  local endpoint="$1" token="$2" file="$3"
  [[ -f "$file" ]] || die "imagem não encontrada: $file"
  local content_type code
  content_type=$(mime_for "$file")
  code=$(curl -sS -o /tmp/seed-response.json -w '%{http_code}' -X PUT "$endpoint" \
    -H "Authorization: Bearer ${token}" \
    -F "file=@${file};type=${content_type}")
  [[ "$code" == "200" ]] || die "falha no upload de imagem (HTTP $code): $(cat /tmp/seed-response.json)"
}

attach_restaurant_image() {
  local token="$1" restaurant_id="$2" file="$3"
  upload_image \
    "${BASE_URL}/restaurant-ms/restaurants/${restaurant_id}/image" \
    "$token" \
    "$file"
  log "  Imagem de perfil: $(basename "$file")"
}

create_product_with_retry() {
  local token="$1" name="$2" price="$3" description="$4"
  local body
  body=$(jq -n \
    --arg name "$name" \
    --argjson price "$price" \
    --arg description "$description" \
    '{
      name: $name,
      price: $price,
      description: $description,
      specifications: [
        {
          name: "Tamanho",
          description: "Escolha o tamanho",
          specOptions: [
            { name: "Pequeno", description: "Porção pequena", extraPrice: 0 },
            { name: "Grande", description: "Porção grande", extraPrice: 8 }
          ]
        }
      ]
    }')

  local elapsed=0
  while (( elapsed < MAX_WAIT_SEC )); do
    local code
    code=$(http_code POST "${BASE_URL}/catalog-ms/products" "$body" "$token")
    if [[ "$code" == "201" ]]; then
      log "  Produto criado: $name"
      jq -r '.id' /tmp/seed-response.json
      return 0
    fi
    # catalog ainda sem restaurant_reference (mensagem assíncrona)
    if [[ "$code" == "409" ]]; then
      sleep "$RETRY_SLEEP_SEC"
      elapsed=$((elapsed + RETRY_SLEEP_SEC))
      continue
    fi
    die "falha ao criar produto $name (HTTP $code): $(cat /tmp/seed-response.json)"
  done
  die "timeout aguardando restaurant_reference no catalog para criar $name"
}

attach_product_image() {
  local token="$1" product_id="$2" file="$3"
  upload_image \
    "${BASE_URL}/catalog-ms/products/${product_id}/image" \
    "$token" \
    "$file"
  log "    Foto: $(basename "$file")"
}

seed_restaurant_with_products() {
  local token="$1" rest_id="$2" profile_image="$3"
  shift 3
  # remaining args: pairs of "Product Name|price|description|image_relative_path"
  attach_restaurant_image "$token" "$rest_id" "${IMAGES_DIR}/${profile_image}"

  local entry name price description image_rel product_id
  for entry in "$@"; do
    IFS='|' read -r name price description image_rel <<<"$entry"
    product_id=$(create_product_with_retry "$token" "$name" "$price" "$description")
    attach_product_image "$token" "$product_id" "${IMAGES_DIR}/${image_rel}"
  done
}

[[ -d "$IMAGES_DIR" ]] || die "pasta de imagens não encontrada: $IMAGES_DIR"
log "Usando imagens em ${IMAGES_DIR}"

wait_gateway

log "Criando base: 3 restaurantes x 5 produtos (com imagens)"

# --- Owner 1 / Pizzaria ---
create_user "Ana Pizza" "ana.pizza@seed.local" "01001000" "Praça da Sé" "100" "Sé" >/dev/null
TOKEN=$(login "ana.pizza@seed.local")
REST_ID=$(create_restaurant "$TOKEN" "Pizzaria Centro Seed" "Pizzas artesanais" "01001000" "Praça da Sé" "100" "Sé")
log "Restaurante 1 id=$REST_ID"
seed_restaurant_with_products "$TOKEN" "$REST_ID" "profile_pics/pizza.jpg" \
  "Pizza Margherita|42.9|Mussarela e manjericão|products/pizza2.jpg" \
  "Pizza Calabresa|45.9|Calabresa e cebola|products/pizza3.jpg" \
  "Pizza Quatro Queijos|49.9|Mistura de queijos|products/pizzaCatupiry.jpg" \
  "Pizza Portuguesa|47.5|Presunto, ovo e cebola|products/pizza2.jpg" \
  "Pizza Frango Catupiry|48.0|Frango e catupiry|products/pizzaCatupiry.jpg"

# --- Owner 2 / Burger ---
create_user "Bruno Burger" "bruno.burger@seed.local" "01310100" "Avenida Paulista" "1500" "Bela Vista" >/dev/null
TOKEN=$(login "bruno.burger@seed.local")
REST_ID=$(create_restaurant "$TOKEN" "Burger House Seed" "Hambúrgueres smash" "01310100" "Avenida Paulista" "1500" "Bela Vista")
log "Restaurante 2 id=$REST_ID"
seed_restaurant_with_products "$TOKEN" "$REST_ID" "profile_pics/burger.jpg" \
  "Classic Burger|32.0|Blend 160g, queijo e salada|products/hamburguer1.jpg" \
  "Bacon Burger|36.5|Blend 160g e bacon|products/hamburger2.jpg" \
  "Double Smash|39.9|Dois blends smash|products/hamburger3.jpg" \
  "Chicken Burger|34.0|Frango empanado|products/hamburguer4.jpg" \
  "Veggie Burger|33.5|Hambúrguer de grão-de-bico|products/hamburger2.jpg"

# --- Owner 3 / Sushi ---
create_user "Carla Sushi" "carla.sushi@seed.local" "04038001" "Rua Domingos de Morais" "2564" "Vila Mariana" >/dev/null
TOKEN=$(login "carla.sushi@seed.local")
REST_ID=$(create_restaurant "$TOKEN" "Sushi Bar Seed" "Comida japonesa" "04038001" "Rua Domingos de Morais" "2564" "Vila Mariana")
log "Restaurante 3 id=$REST_ID"
seed_restaurant_with_products "$TOKEN" "$REST_ID" "profile_pics/sushi.jpg" \
  "Combo Salmão|59.9|8 peças de salmão|products/sushi.jpg" \
  "Hot Roll|28.0|8 unidades|products/hotroll.jpg" \
  "Temaki Salmão|24.5|Temaki completo|products/sushi.jpg" \
  "Yakissoba|35.0|Macarrão oriental|products/hotroll.jpg" \
  "Missoshiru|12.0|Sopa de missô|products/sushi.jpg"

# --- Cliente de teste (sem restaurante) ---
create_user "Diego Cliente" "diego.cliente@seed.local" "01414001" "Rua Augusta" "2690" "Jardins" >/dev/null

log "Seed concluído. Credenciais em local-infra/seed/CREDENTIALS.md"
