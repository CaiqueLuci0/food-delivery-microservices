import { useQuery } from '@tanstack/react-query'
import { createContext, useContext, useMemo, type ReactNode } from 'react'
import { useAuth } from '@/contexts/AuthContext'
import { restaurantService } from '@/services/restaurantService'
import type { Restaurant } from '@/types/api'

type OwnerRestaurantContextValue = {
  ownedRestaurant: Restaurant | null
  isLoading: boolean
  refetch: () => void
}

const OwnerRestaurantContext = createContext<OwnerRestaurantContextValue | null>(null)

export function OwnerRestaurantProvider({ children }: { children: ReactNode }) {
  const { user, isAuthenticated } = useAuth()

  const query = useQuery({
    queryKey: ['owned-restaurant', user?.id],
    enabled: isAuthenticated && Boolean(user?.id),
    queryFn: async () => {
      const list = await restaurantService.list()
      return list.find((r) => r.ownerId === user!.id) ?? null
    },
  })

  const value = useMemo(
    () => ({
      ownedRestaurant: query.data ?? null,
      isLoading: query.isLoading,
      refetch: () => {
        void query.refetch()
      },
    }),
    [query],
  )

  return (
    <OwnerRestaurantContext.Provider value={value}>{children}</OwnerRestaurantContext.Provider>
  )
}

export function useOwnerRestaurant(): OwnerRestaurantContextValue {
  const ctx = useContext(OwnerRestaurantContext)
  if (!ctx) {
    throw new Error('useOwnerRestaurant must be used within OwnerRestaurantProvider')
  }
  return ctx
}
