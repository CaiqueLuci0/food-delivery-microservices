import {
  Button,
  Card,
  CardContent,
  Chip,
  Stack,
  Typography,
} from '@mui/material'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { getErrorMessage } from '@/api/httpClient'
import { ConfirmDialog } from '@/components/feedback/ConfirmDialog'
import { ErrorState, PageLoader } from '@/components/feedback/States'
import { useSnackbar } from '@/contexts/SnackbarContext'
import { useOrderStatusSocket } from '@/hooks/useOrderStatusSocket'
import { orderService } from '@/services/orderService'
import { paymentService } from '@/services/paymentService'
import { ORDER_STATUS_LABEL, formatCurrency, orderTotal } from '@/utils/format'

export function OrderDetailPage() {
  const { id = '' } = useParams()
  const { notify } = useSnackbar()
  const queryClient = useQueryClient()
  const navigate = useNavigate()
  const [cancelOpen, setCancelOpen] = useState(false)

  useOrderStatusSocket(id)

  const query = useQuery({
    queryKey: ['order', id],
    enabled: Boolean(id),
    queryFn: () => orderService.getById(id),
  })

  const payMutation = useMutation({
    mutationFn: () => paymentService.markAsPaid(id),
    onSuccess: async () => {
      notify('Pagamento confirmado')
      await queryClient.invalidateQueries({ queryKey: ['order', id] })
      await queryClient.invalidateQueries({ queryKey: ['orders'] })
    },
    onError: (error) => notify(getErrorMessage(error), 'error'),
  })

  const cancelMutation = useMutation({
    mutationFn: () => orderService.cancel(id),
    onSuccess: async () => {
      notify('Pedido cancelado')
      setCancelOpen(false)
      await queryClient.invalidateQueries({ queryKey: ['order', id] })
      await queryClient.invalidateQueries({ queryKey: ['orders'] })
    },
    onError: (error) => notify(getErrorMessage(error), 'error'),
  })

  if (query.isLoading) return <PageLoader />
  if (query.isError) {
    return <ErrorState message={getErrorMessage(query.error)} onRetry={() => void query.refetch()} />
  }

  const order = query.data!
  const canPay = order.status === 'AGUARDANDO_PAGAMENTO' && order.paymentStatus !== 'PAGO'
  const canCancel =
    order.status === 'EM_CADASTRAMENTO' || order.status === 'AGUARDANDO_PAGAMENTO'

  return (
    <Stack spacing={3} maxWidth={720}>
      <Stack direction="row" justifyContent="space-between" alignItems="center" gap={2}>
        <Typography variant="h4">Pedido #{order.id.slice(0, 8)}</Typography>
        <Chip label={ORDER_STATUS_LABEL[order.status] ?? order.status} color="primary" />
      </Stack>
      <Typography color="text.secondary">
        Pagamento: {order.paymentStatus ?? 'pendente'}
      </Typography>
      <Card>
        <CardContent>
          <Stack spacing={1.5}>
            {order.productSnapshots.map((item) => (
              <Stack key={item.id} spacing={0.5}>
                <Stack direction="row" justifyContent="space-between">
                  <Typography fontWeight={600}>{item.name}</Typography>
                  <Typography>{formatCurrency(item.price)}</Typography>
                </Stack>
                {item.specOptionSnapshots.map((opt) => (
                  <Typography key={opt.id} variant="body2" color="text.secondary">
                    + {opt.name} ({formatCurrency(opt.extraPrice)})
                  </Typography>
                ))}
              </Stack>
            ))}
            <Typography variant="h6" pt={1}>
              Total {formatCurrency(orderTotal(order))}
            </Typography>
          </Stack>
        </CardContent>
      </Card>
      <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5}>
        {canPay ? (
          <Button
            variant="contained"
            size="large"
            disabled={payMutation.isPending}
            onClick={() => payMutation.mutate()}
          >
            {payMutation.isPending ? 'Confirmando…' : 'Confirmar pagamento'}
          </Button>
        ) : null}
        {canCancel ? (
          <Button color="error" variant="outlined" onClick={() => setCancelOpen(true)}>
            Cancelar pedido
          </Button>
        ) : null}
        <Button variant="text" onClick={() => navigate('/orders')}>
          Voltar
        </Button>
      </Stack>
      <ConfirmDialog
        open={cancelOpen}
        title="Cancelar pedido?"
        description="O pedido será cancelado e o pagamento correspondente também."
        confirmLabel="Cancelar pedido"
        loading={cancelMutation.isPending}
        onClose={() => setCancelOpen(false)}
        onConfirm={() => cancelMutation.mutate()}
      />
    </Stack>
  )
}
