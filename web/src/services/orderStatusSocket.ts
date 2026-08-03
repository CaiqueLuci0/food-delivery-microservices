import { Client, type IMessage, type StompSubscription } from '@stomp/stompjs'
import { getStoredToken } from '@/api/httpClient'
import type { Order } from '@/types/api'

function resolveWsUrl(): string {
  const base = (import.meta.env.VITE_API_BASE_URL || window.location.origin).replace(/\/$/, '')
  const wsBase = base.replace(/^http/, 'ws')
  return `${wsBase}/order-ms/ws`
}

type OrderUpdateHandler = (order: Order) => void

export function subscribeOrderUpdates(orderId: string, onUpdate: OrderUpdateHandler): () => void {
  const token = getStoredToken()
  if (!token || !orderId) {
    return () => undefined
  }

  let subscription: StompSubscription | null = null

  const client = new Client({
    brokerURL: resolveWsUrl(),
    connectHeaders: {
      Authorization: `Bearer ${token}`,
    },
    reconnectDelay: 3000,
    heartbeatIncoming: 10000,
    heartbeatOutgoing: 10000,
    onConnect: () => {
      subscription = client.subscribe(`/topic/orders/${orderId}`, (message: IMessage) => {
        try {
          const order = JSON.parse(message.body) as Order
          onUpdate(order)
        } catch {
          // ignore malformed frames
        }
      })
    },
  })

  client.activate()

  return () => {
    try {
      subscription?.unsubscribe()
    } catch {
      // ignore
    }
    void client.deactivate()
  }
}
