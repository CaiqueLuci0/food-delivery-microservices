import {
  createContext,
  useCallback,
  useContext,
  useMemo,
  useState,
  type ReactNode,
} from 'react'
import type { CartItem } from '@/types/api'

type CartContextValue = {
  items: CartItem[]
  restaurantId: string | null
  itemCount: number
  addItem: (item: Omit<CartItem, 'key' | 'quantity'> & { quantity?: number }) => void
  removeItem: (key: string) => void
  setQuantity: (key: string, quantity: number) => void
  clear: () => void
  toOrderItems: () => Array<{ productId: string; specOptionIds: string[] }>
}

const CartContext = createContext<CartContextValue | null>(null)

function buildKey(productId: string, specOptionIds: string[]): string {
  return `${productId}:${[...specOptionIds].sort().join(',')}`
}

export function CartProvider({ children }: { children: ReactNode }) {
  const [items, setItems] = useState<CartItem[]>([])

  const restaurantId = items[0]?.restaurantId ?? null

  const addItem = useCallback(
    (item: Omit<CartItem, 'key' | 'quantity'> & { quantity?: number }) => {
      const key = buildKey(item.productId, item.specOptionIds)
      setItems((prev) => {
        if (prev.length > 0 && prev[0]?.restaurantId !== item.restaurantId) {
          return [
            {
              ...item,
              key,
              quantity: item.quantity ?? 1,
            },
          ]
        }
        const existing = prev.find((p) => p.key === key)
        if (existing) {
          return prev.map((p) =>
            p.key === key ? { ...p, quantity: p.quantity + (item.quantity ?? 1) } : p,
          )
        }
        return [...prev, { ...item, key, quantity: item.quantity ?? 1 }]
      })
    },
    [],
  )

  const removeItem = useCallback((key: string) => {
    setItems((prev) => prev.filter((p) => p.key !== key))
  }, [])

  const setQuantity = useCallback((key: string, quantity: number) => {
    setItems((prev) =>
      prev
        .map((p) => (p.key === key ? { ...p, quantity } : p))
        .filter((p) => p.quantity > 0),
    )
  }, [])

  const clear = useCallback(() => setItems([]), [])

  const toOrderItems = useCallback(() => {
    const result: Array<{ productId: string; specOptionIds: string[] }> = []
    for (const item of items) {
      for (let i = 0; i < item.quantity; i += 1) {
        result.push({ productId: item.productId, specOptionIds: item.specOptionIds })
      }
    }
    return result
  }, [items])

  const value = useMemo(
    () => ({
      items,
      restaurantId,
      itemCount: items.reduce((acc, i) => acc + i.quantity, 0),
      addItem,
      removeItem,
      setQuantity,
      clear,
      toOrderItems,
    }),
    [items, restaurantId, addItem, removeItem, setQuantity, clear, toOrderItems],
  )

  return <CartContext.Provider value={value}>{children}</CartContext.Provider>
}

export function useCart(): CartContextValue {
  const ctx = useContext(CartContext)
  if (!ctx) {
    throw new Error('useCart must be used within CartProvider')
  }
  return ctx
}
