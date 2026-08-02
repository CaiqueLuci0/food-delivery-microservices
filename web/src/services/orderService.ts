import { httpClient } from '@/api/httpClient'
import type { Order, OrderItemRequest, OrderStatus } from '@/types/api'

export const orderService = {
  create: async (items: OrderItemRequest[]): Promise<Order> => {
    const { data } = await httpClient.post<Order>('/order-ms/orders', { items })
    return data
  },
  update: async (id: string, items: OrderItemRequest[]): Promise<Order> => {
    const { data } = await httpClient.put<Order>(`/order-ms/orders/${id}`, { items })
    return data
  },
  finalize: async (id: string): Promise<Order> => {
    const { data } = await httpClient.patch<Order>(`/order-ms/orders/${id}/finalize`)
    return data
  },
  updateStatus: async (id: string, status: OrderStatus): Promise<Order> => {
    const { data } = await httpClient.put<Order>(`/order-ms/orders/${id}/status`, { status })
    return data
  },
  cancel: async (id: string): Promise<Order> => {
    const { data } = await httpClient.post<Order>(`/order-ms/orders/${id}/cancel`)
    return data
  },
  getById: async (id: string): Promise<Order> => {
    const { data } = await httpClient.get<Order>(`/order-ms/orders/${id}`)
    return data
  },
  list: async (restaurantId?: string): Promise<Order[]> => {
    const { data } = await httpClient.get<Order[]>('/order-ms/orders', {
      params: restaurantId ? { restaurantId } : undefined,
    })
    return data
  },
}
