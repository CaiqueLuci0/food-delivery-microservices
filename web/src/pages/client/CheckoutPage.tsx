import { Alert, Button, Card, CardContent, Stack, Typography } from '@mui/material'
import { useMutation } from '@tanstack/react-query'
import { useNavigate } from 'react-router-dom'
import { getErrorMessage } from '@/api/httpClient'
import { EmptyState } from '@/components/feedback/States'
import { useAuth } from '@/contexts/AuthContext'
import { useCart } from '@/contexts/CartContext'
import { useSnackbar } from '@/contexts/SnackbarContext'
import { orderService } from '@/services/orderService'
import { formatCurrency } from '@/utils/format'

export function CheckoutPage() {
  const { items, toOrderItems, clear } = useCart()
  const { user } = useAuth()
  const { notify } = useSnackbar()
  const navigate = useNavigate()
  const total = items.reduce((sum, item) => sum + item.unitPrice * item.quantity, 0)

  const mutation = useMutation({
    mutationFn: async () => {
      const order = await orderService.create(toOrderItems())
      return orderService.finalize(order.id)
    },
    onSuccess: (order) => {
      clear()
      notify('Pedido finalizado — prossiga com o pagamento')
      navigate(`/orders/${order.id}`)
    },
  })

  if (items.length === 0) {
    return (
      <EmptyState
        title="Nada para checkout"
        description="Seu carrinho está vazio."
        actionLabel="Voltar"
        onAction={() => navigate('/')}
      />
    )
  }

  return (
    <Stack spacing={3} maxWidth={640}>
      <Typography variant="h4">Checkout</Typography>
      <Card>
        <CardContent>
          <Stack spacing={1}>
            <Typography fontWeight={600}>Entrega</Typography>
            <Typography color="text.secondary">
              {user?.address
                ? `${user.address.logradouro}, ${user.address.numero} — ${user.address.cidade}/${user.address.uf}`
                : 'Endereço do perfil'}
            </Typography>
          </Stack>
        </CardContent>
      </Card>
      <Card>
        <CardContent>
          <Stack spacing={1}>
            {items.map((item) => (
              <Stack key={item.key} direction="row" justifyContent="space-between">
                <Typography>
                  {item.quantity}x {item.productName}
                </Typography>
                <Typography>{formatCurrency(item.unitPrice * item.quantity)}</Typography>
              </Stack>
            ))}
            <Typography variant="h6" pt={1}>
              Total {formatCurrency(total)}
            </Typography>
          </Stack>
        </CardContent>
      </Card>
      {mutation.isError ? <Alert severity="error">{getErrorMessage(mutation.error)}</Alert> : null}
      <Button
        variant="contained"
        size="large"
        disabled={mutation.isPending}
        onClick={() => mutation.mutate()}
      >
        {mutation.isPending ? 'Criando pedido…' : 'Confirmar e aguardar pagamento'}
      </Button>
    </Stack>
  )
}
