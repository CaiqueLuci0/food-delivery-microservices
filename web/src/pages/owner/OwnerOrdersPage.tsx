import {
  Card,
  CardActionArea,
  CardContent,
  Chip,
  Stack,
  Typography,
} from '@mui/material'
import { useQuery } from '@tanstack/react-query'
import { Link as RouterLink } from 'react-router-dom'
import { getErrorMessage } from '@/api/httpClient'
import { EmptyState, ErrorState, PageLoader } from '@/components/feedback/States'
import { useOwnerRestaurant } from '@/contexts/OwnerRestaurantContext'
import { orderService } from '@/services/orderService'
import { ORDER_STATUS_LABEL, formatCurrency, orderTotal } from '@/utils/format'

export function OwnerOrdersPage() {
  const { ownedRestaurant } = useOwnerRestaurant()

  const query = useQuery({
    queryKey: ['orders', 'restaurant', ownedRestaurant?.id],
    enabled: Boolean(ownedRestaurant?.id),
    queryFn: () => orderService.list(ownedRestaurant!.id),
    refetchInterval: 5000,
  })

  if (!ownedRestaurant || query.isLoading) return <PageLoader />
  if (query.isError) {
    return <ErrorState message={getErrorMessage(query.error)} onRetry={() => void query.refetch()} />
  }
  const orders = query.data ?? []
  if (orders.length === 0) {
    return <EmptyState title="Sem pedidos" description="Pedidos deste restaurante aparecerão aqui." />
  }

  return (
    <Stack spacing={2}>
      <Typography variant="h4">Pedidos do restaurante</Typography>
      {orders.map((order) => (
        <Card key={order.id}>
          <CardActionArea component={RouterLink} to={`/restaurant/orders/${order.id}`}>
            <CardContent>
              <Stack direction="row" justifyContent="space-between" alignItems="center" gap={2}>
                <Stack>
                  <Typography fontWeight={600}>#{order.id.slice(0, 8)}</Typography>
                  <Typography variant="body2" color="text.secondary">
                    {formatCurrency(orderTotal(order))} · pagamento {order.paymentStatus ?? '—'}
                  </Typography>
                </Stack>
                <Chip label={ORDER_STATUS_LABEL[order.status] ?? order.status} />
              </Stack>
            </CardContent>
          </CardActionArea>
        </Card>
      ))}
    </Stack>
  )
}
