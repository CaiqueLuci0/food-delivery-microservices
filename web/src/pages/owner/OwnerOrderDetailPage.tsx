import {
  Button,
  Card,
  CardContent,
  MenuItem,
  Stack,
  TextField,
  Typography,
} from '@mui/material'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { getErrorMessage } from '@/api/httpClient'
import { ConfirmDialog } from '@/components/feedback/ConfirmDialog'
import { ErrorState, PageLoader } from '@/components/feedback/States'
import { useOwnerRestaurant } from '@/contexts/OwnerRestaurantContext'
import { useSnackbar } from '@/contexts/SnackbarContext'
import { orderService } from '@/services/orderService'
import {
  ORDER_STATUS_LABEL,
  OWNER_ORDER_STATUSES,
  formatCurrency,
  orderTotal,
} from '@/utils/format'
import type { OrderStatus } from '@/types/api'

export function OwnerOrderDetailPage() {
  const { id = '' } = useParams()
  const { ownedRestaurant } = useOwnerRestaurant()
  const { notify } = useSnackbar()
  const queryClient = useQueryClient()
  const navigate = useNavigate()
  const [status, setStatus] = useState<OrderStatus>('PREPARANDO')
  const [cancelOpen, setCancelOpen] = useState(false)

  const query = useQuery({
    queryKey: ['order', id],
    enabled: Boolean(id),
    queryFn: () => orderService.getById(id),
    refetchInterval: 4000,
  })

  const statusMutation = useMutation({
    mutationFn: () => orderService.updateStatus(id, status),
    onSuccess: async () => {
      notify('Status atualizado')
      await queryClient.invalidateQueries({ queryKey: ['order', id] })
      await queryClient.invalidateQueries({
        queryKey: ['orders', 'restaurant', ownedRestaurant?.id],
      })
    },
    onError: (error) => notify(getErrorMessage(error), 'error'),
  })

  const cancelMutation = useMutation({
    mutationFn: () => orderService.cancel(id),
    onSuccess: async () => {
      notify('Pedido cancelado')
      setCancelOpen(false)
      await queryClient.invalidateQueries({ queryKey: ['order', id] })
      navigate('/restaurant/orders')
    },
    onError: (error) => notify(getErrorMessage(error), 'error'),
  })

  if (query.isLoading) return <PageLoader />
  if (query.isError) {
    return <ErrorState message={getErrorMessage(query.error)} onRetry={() => void query.refetch()} />
  }

  const order = query.data!
  const canUpdateStatus = order.paymentStatus === 'PAGO' && order.status !== 'CANCELADO'

  return (
    <Stack spacing={3} maxWidth={720}>
      <Typography variant="h4">Pedido #{order.id.slice(0, 8)}</Typography>
      <Typography color="text.secondary">
        Status atual: {ORDER_STATUS_LABEL[order.status] ?? order.status}
      </Typography>
      <Card>
        <CardContent>
          <Stack spacing={1}>
            {order.productSnapshots.map((item) => (
              <Typography key={item.id}>
                {item.name} — {formatCurrency(item.price)}
              </Typography>
            ))}
            <Typography variant="h6">Total {formatCurrency(orderTotal(order))}</Typography>
          </Stack>
        </CardContent>
      </Card>
      {canUpdateStatus ? (
        <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2} alignItems={{ sm: 'center' }}>
          <TextField
            select
            label="Novo status"
            value={status}
            onChange={(e) => setStatus(e.target.value as OrderStatus)}
            sx={{ minWidth: 240 }}
          >
            {OWNER_ORDER_STATUSES.map((s) => (
              <MenuItem key={s} value={s}>
                {ORDER_STATUS_LABEL[s]}
              </MenuItem>
            ))}
          </TextField>
          <Button
            variant="contained"
            disabled={statusMutation.isPending}
            onClick={() => statusMutation.mutate()}
          >
            Atualizar status
          </Button>
        </Stack>
      ) : (
        <Typography color="text.secondary">
          Atualização de status disponível após pagamento aprovado.
        </Typography>
      )}
      {order.status !== 'CANCELADO' && order.status !== 'EM_CADASTRAMENTO' ? (
        <Button color="error" variant="outlined" onClick={() => setCancelOpen(true)}>
          Cancelar pedido
        </Button>
      ) : null}
      <ConfirmDialog
        open={cancelOpen}
        title="Cancelar pedido?"
        description="O cliente verá o pedido como cancelado."
        confirmLabel="Cancelar"
        loading={cancelMutation.isPending}
        onClose={() => setCancelOpen(false)}
        onConfirm={() => cancelMutation.mutate()}
      />
    </Stack>
  )
}
