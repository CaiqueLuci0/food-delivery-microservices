import { httpClient } from '@/api/httpClient'
import type { Product, ProductWriteRequest } from '@/types/api'

export const productService = {
  listByRestaurant: async (restaurantId: string, search?: string): Promise<Product[]> => {
    const { data } = await httpClient.get<Product[]>('/catalog-ms/products', {
      params: { restaurantId, search },
    })
    return data
  },
  getById: async (id: string): Promise<Product> => {
    const { data } = await httpClient.get<Product>(`/catalog-ms/products/${id}`)
    return data
  },
  create: async (payload: ProductWriteRequest): Promise<Product> => {
    const { data } = await httpClient.post<Product>('/catalog-ms/products', payload)
    return data
  },
  update: async (id: string, payload: ProductWriteRequest): Promise<Product> => {
    const { data } = await httpClient.put<Product>(`/catalog-ms/products/${id}`, payload)
    return data
  },
  remove: async (id: string): Promise<void> => {
    await httpClient.delete(`/catalog-ms/products/${id}`)
  },
  uploadImage: async (id: string, file: File): Promise<Product> => {
    const formData = new FormData()
    formData.append('file', file)
    const { data } = await httpClient.put<Product>(
      `/catalog-ms/products/${id}/image`,
      formData,
    )
    return data
  },
  removeImage: async (id: string): Promise<Product> => {
    const { data } = await httpClient.delete<Product>(`/catalog-ms/products/${id}/image`)
    return data
  },
}
