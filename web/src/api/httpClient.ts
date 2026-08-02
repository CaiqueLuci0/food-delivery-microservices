import axios, { type AxiosError } from 'axios'

const TOKEN_KEY = 'fd_token'

export function getStoredToken(): string | null {
  return localStorage.getItem(TOKEN_KEY)
}

export function setStoredToken(token: string | null): void {
  if (token) {
    localStorage.setItem(TOKEN_KEY, token)
  } else {
    localStorage.removeItem(TOKEN_KEY)
  }
}

export const httpClient = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || undefined,
  headers: { 'Content-Type': 'application/json' },
})

httpClient.interceptors.request.use((config) => {
  const token = getStoredToken()
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

type UnauthorizedHandler = () => void

let onUnauthorized: UnauthorizedHandler | null = null

export function setUnauthorizedHandler(handler: UnauthorizedHandler): void {
  onUnauthorized = handler
}

httpClient.interceptors.response.use(
  (response) => response,
  (error: AxiosError<{ message?: string }>) => {
    if (error.response?.status === 401) {
      onUnauthorized?.()
    }
    return Promise.reject(error)
  },
)

const FRIENDLY_UNAVAILABLE =
  'Serviço temporariamente indisponível. Tente novamente em instantes.'

function isGatewayOrNetworkError(error: AxiosError): boolean {
  const status = error.response?.status
  if (status === 502 || status === 503 || status === 504) return true
  // Sem response = rede/gateway caiu antes de responder
  return status == null && Boolean(error.request)
}

export function getErrorMessage(error: unknown): string {
  if (axios.isAxiosError(error)) {
    if (isGatewayOrNetworkError(error)) {
      return FRIENDLY_UNAVAILABLE
    }
    const data = error.response?.data as { message?: string } | undefined
    if (typeof data?.message === 'string' && data.message.trim()) {
      return data.message
    }
    return 'Não foi possível concluir a operação. Tente novamente.'
  }
  if (error instanceof Error) {
    return error.message
  }
  return 'Erro inesperado'
}
