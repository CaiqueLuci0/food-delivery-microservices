# DeliveryBacana (web)

SPA React 19 + Vite + MUI.

## Docker

```bash
cd local-infra
docker compose up --build
```

| Serviço | URL |
|---------|-----|
| Frontend | http://localhost:3000 |
| API (nginx) | http://localhost:8080 |

O frontend chama a API em `http://localhost:8080` (CORS liberado para `:3000`).

Credenciais de seed: `local-infra/seed/CREDENTIALS.md`.

## Desenvolvimento local

```bash
cd web
npm install
npm run dev
```

Vite em `:5173` com proxy para `http://localhost:8080`.
