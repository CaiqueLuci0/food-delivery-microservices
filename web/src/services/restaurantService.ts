import { httpClient } from '@/api/httpClient'
import type { Restaurant, RestaurantWriteRequest } from '@/types/api'

export type RestaurantListParams = {
  search?: string
  latitude?: number
  longitude?: number
}

export const restaurantService = {
  list: async (params: RestaurantListParams = {}): Promise<Restaurant[]> => {
    const { data } = await httpClient.get<Restaurant[]>('/restaurant-ms/restaurants', { params })
    return data
  },
  getById: async (id: string): Promise<Restaurant> => {
    const { data } = await httpClient.get<Restaurant>(`/restaurant-ms/restaurants/${id}`)
    return data
  },
  create: async (payload: RestaurantWriteRequest): Promise<Restaurant> => {
    const { data } = await httpClient.post<Restaurant>('/restaurant-ms/restaurants', payload)
    return data
  },
  update: async (id: string, payload: RestaurantWriteRequest): Promise<Restaurant> => {
    const { data } = await httpClient.put<Restaurant>(`/restaurant-ms/restaurants/${id}`, payload)
    return data
  },
  remove: async (id: string): Promise<void> => {
    await httpClient.delete(`/restaurant-ms/restaurants/${id}`)
  },
  uploadImage: async (id: string, file: File): Promise<Restaurant> => {
    const formData = new FormData()
    formData.append('file', file)
    const { data } = await httpClient.put<Restaurant>(
      `/restaurant-ms/restaurants/${id}/image`,
      formData,
    )
    return data
  },
  removeImage: async (id: string): Promise<Restaurant> => {
    const { data } = await httpClient.delete<Restaurant>(`/restaurant-ms/restaurants/${id}/image`)
    return data
  },
}
