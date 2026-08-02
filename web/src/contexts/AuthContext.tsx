import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useRef,
  useState,
  type ReactNode,
} from 'react'
import axios from 'axios'
import { useLocation } from 'react-router-dom'
import { getStoredToken, setStoredToken, setUnauthorizedHandler } from '@/api/httpClient'
import { PageLoader } from '@/components/feedback/States'
import { authService } from '@/services/userService'
import type { User } from '@/types/api'

async function wait(ms: number): Promise<void> {
  await new Promise((resolve) => setTimeout(resolve, ms))
}

function isUnauthorized(error: unknown): boolean {
  return axios.isAxiosError(error) && error.response?.status === 401
}

function isTransient(error: unknown): boolean {
  if (!axios.isAxiosError(error)) return false
  const status = error.response?.status
  return status == null || status === 502 || status === 503 || status === 504
}

async function bootstrapSession(token: string) {
  const maxAttempts = 5
  let lastError: unknown
  for (let attempt = 1; attempt <= maxAttempts; attempt += 1) {
    try {
      return await authService.isLogged(token)
    } catch (error) {
      lastError = error
      if (isUnauthorized(error) || !isTransient(error) || attempt === maxAttempts) {
        throw error
      }
      await wait(attempt * 800)
    }
  }
  throw lastError
}

type AuthContextValue = {
  token: string | null
  user: User | null
  isAuthenticated: boolean
  isBootstrapping: boolean
  setSession: (token: string, user: User) => void
  updateUser: (user: User) => void
  logout: () => void
}

const AuthContext = createContext<AuthContextValue | null>(null)

const USER_KEY = 'fd_user'

function readStoredUser(): User | null {
  const raw = localStorage.getItem(USER_KEY)
  if (!raw) return null
  try {
    return JSON.parse(raw) as User
  } catch {
    return null
  }
}

export function AuthProvider({ children }: { children: ReactNode }) {
  const location = useLocation()
  const [token, setToken] = useState<string | null>(() => getStoredToken())
  const [user, setUser] = useState<User | null>(() => readStoredUser())
  const [isBootstrapping, setIsBootstrapping] = useState(() => Boolean(getStoredToken()))
  const isFirstCheck = useRef(true)

  const logout = useCallback(() => {
    setToken(null)
    setUser(null)
    setStoredToken(null)
    localStorage.removeItem(USER_KEY)
  }, [])

  const setSession = useCallback((nextToken: string, nextUser: User) => {
    setToken(nextToken)
    setUser(nextUser)
    setStoredToken(nextToken)
    localStorage.setItem(USER_KEY, JSON.stringify(nextUser))
  }, [])

  const updateUser = useCallback((nextUser: User) => {
    setUser(nextUser)
    localStorage.setItem(USER_KEY, JSON.stringify(nextUser))
  }, [])

  useEffect(() => {
    setUnauthorizedHandler(() => {
      logout()
    })
  }, [logout])

  useEffect(() => {
    const storedToken = getStoredToken()
    if (!storedToken) {
      setIsBootstrapping(false)
      isFirstCheck.current = false
      return
    }

    let cancelled = false
    const withRetry = isFirstCheck.current

    void (withRetry ? bootstrapSession(storedToken) : authService.isLogged(storedToken))
      .then((data) => {
        if (!cancelled) {
          setSession(data.token, data.user)
        }
      })
      .catch((error) => {
        if (!cancelled && isUnauthorized(error)) {
          logout()
        }
      })
      .finally(() => {
        if (!cancelled) {
          isFirstCheck.current = false
          setIsBootstrapping(false)
        }
      })

    return () => {
      cancelled = true
    }
  }, [location.pathname, logout, setSession])

  const value = useMemo(
    () => ({
      token,
      user,
      isAuthenticated: Boolean(token && user),
      isBootstrapping,
      setSession,
      updateUser,
      logout,
    }),
    [token, user, isBootstrapping, setSession, updateUser, logout],
  )

  if (isBootstrapping) {
    return <PageLoader />
  }

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth(): AuthContextValue {
  const ctx = useContext(AuthContext)
  if (!ctx) {
    throw new Error('useAuth must be used within AuthProvider')
  }
  return ctx
}
