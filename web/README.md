# DeliveryBacana (web)

SPA React 19 + Vite + MUI.

## Informações básicas de navegação

### Login
<img width="1018" height="542" alt="image" src="https://github.com/user-attachments/assets/3586710f-3678-45da-99d6-a796d7de2b28" />

### Cadastro de Usuário
<img width="1017" height="875" alt="image" src="https://github.com/user-attachments/assets/a54c8670-9bd9-414b-8406-9557341b1d4c" />

### Header
<img width="1082" height="55" alt="image" src="https://github.com/user-attachments/assets/0c1b9060-3fb1-4b77-bb4f-b9ad7183dfaf" />

<img width="1082" height="58" alt="image" src="https://github.com/user-attachments/assets/390d961c-ac99-40e3-a4da-fbef82f5b99b" />
- Abre formulário de criação de restaurante

<img width="1082" height="57" alt="image" src="https://github.com/user-attachments/assets/dbf91467-c28c-4930-9130-065206b7dde8" />
- Abre lista de pedidos

<img width="1082" height="55" alt="image" src="https://github.com/user-attachments/assets/4c7aef31-dbc6-489f-8188-da7a4374cc88" />
- Mostra o carrinho do usuário

<img width="1082" height="55" alt="image" src="https://github.com/user-attachments/assets/57caa569-f81b-4e4f-a171-bae69903d212" />
- Permite o usuário editar suas informações

<img width="1082" height="55" alt="image" src="https://github.com/user-attachments/assets/88e5e752-c8c7-4924-acb8-e08ca94cf06b" />
- Encerra a sessão

<img width="1082" height="55" alt="image" src="https://github.com/user-attachments/assets/44d46e6d-7276-468a-adfa-8504088aca3f" />
- Volta para a página inicial

### Página principal

<img width="1009" height="644" alt="image" src="https://github.com/user-attachments/assets/af673759-3b10-4924-b10f-754afb64543d" />

<img width="1009" height="644" alt="image" src="https://github.com/user-attachments/assets/2dc75a74-fc2f-43aa-bab5-8a64cd52e4e4" />
- Busca restaurantes em um raio de 3km do endereço cadastrado pelo usuário

<img width="1009" height="644" alt="image" src="https://github.com/user-attachments/assets/9613e59d-a152-478e-8f35-3ce330a3c299" />
- Busca pelo texto nos "nomes" e "descrições" do restaurante

<img width="1009" height="644" alt="image" src="https://github.com/user-attachments/assets/442933c4-83c8-4296-9b50-3be86594235d" />
- Ao clicar redireciona para a página do restaurante

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
