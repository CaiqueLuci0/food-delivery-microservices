import { useEffect } from 'react'
import { useQueryClient } from '@tanstack/react-query'
import { subscribeOrderUpdates } from '@/services/orderStatusSocket'
import type { Order } from '@/types/api'

/** Live-updates React Query cache for an order via STOMP WebSocket. */
export function useOrderStatusSocket(orderId: string | undefined) {
  const queryClient = useQueryClient()

  useEffect(() => {
    if (!orderId) return

    return subscribeOrderUpdates(orderId, (order: Order) => {
      queryClient.setQueryData<Order>(['order', orderId], order)
      void queryClient.invalidateQueries({ queryKey: ['orders'] })
    })
  }, [orderId, queryClient])
}
