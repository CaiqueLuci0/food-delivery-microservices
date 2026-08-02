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
import { ListSkeleton } from '@/components/feedback/Skeletons'
import { EmptyState, ErrorState } from '@/components/feedback/States'
import { orderService } from '@/services/orderService'
import { ORDER_STATUS_LABEL, formatCurrency, orderTotal } from '@/utils/format'

export function OrdersPage() {
  const query = useQuery({
    queryKey: ['orders', 'mine'],
    queryFn: () => orderService.list(),
  })

  if (query.isLoading) return <ListSkeleton count={4} />
  if (query.isError) {
    return <ErrorState message={getErrorMessage(query.error)} onRetry={() => void query.refetch()} />
  }
  const orders = query.data ?? []
  if (orders.length === 0) {
    return (
      <EmptyState
        title="Nenhum pedido ainda"
        description="Quando você pedir, o histórico aparece aqui."
      />
    )
  }

  return (
    <Stack spacing={2}>
      <Typography variant="h4">Meus pedidos</Typography>
      {orders.map((order) => (
        <Card key={order.id}>
          <CardActionArea component={RouterLink} to={`/orders/${order.id}`}>
            <CardContent>
              <Stack direction="row" justifyContent="space-between" alignItems="center" gap={2}>
                <Stack spacing={0.5}>
                  <Typography fontWeight={600}>Pedido #{order.id.slice(0, 8)}</Typography>
                  <Typography variant="body2" color="text.secondary">
                    {order.productSnapshots.length} item(ns) · {formatCurrency(orderTotal(order))}
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
