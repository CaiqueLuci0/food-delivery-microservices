import { Navigate, Outlet } from 'react-router-dom'
import { PageLoader } from '@/components/feedback/States'
import { useOwnerRestaurant } from '@/contexts/OwnerRestaurantContext'

export function OwnerRoute() {
  const { ownedRestaurant, isLoading } = useOwnerRestaurant()

  if (isLoading) {
    return <PageLoader />
  }

  if (!ownedRestaurant) {
    return <Navigate to="/restaurant/new" replace />
  }

  return <Outlet />
}
