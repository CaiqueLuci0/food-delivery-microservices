import { httpClient } from '@/api/httpClient'
import type {
  LoginRequest,
  LoginResponse,
  User,
  UserCreateRequest,
  UserUpdateRequest,
} from '@/types/api'

export const authService = {
  login: async (payload: LoginRequest): Promise<LoginResponse> => {
    const { data } = await httpClient.post<LoginResponse>('/user-ms/auth/login', payload)
    return data
  },
  isLogged: async (token: string): Promise<LoginResponse> => {
    const { data } = await httpClient.get<LoginResponse>('/user-ms/auth/islogged', {
      params: { token },
    })
    return data
  },
}

export const userService = {
  create: async (payload: UserCreateRequest): Promise<User> => {
    const { data } = await httpClient.post<User>('/user-ms/users', payload)
    return data
  },
  getById: async (id: string): Promise<User> => {
    const { data } = await httpClient.get<User>(`/user-ms/users/${id}`)
    return data
  },
  update: async (id: string, payload: UserUpdateRequest): Promise<User> => {
    const { data } = await httpClient.put<User>(`/user-ms/users/${id}`, payload)
    return data
  },
  remove: async (id: string): Promise<void> => {
    await httpClient.delete(`/user-ms/users/${id}`)
  },
}
