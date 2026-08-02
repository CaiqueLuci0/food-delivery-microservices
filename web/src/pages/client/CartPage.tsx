import AddIcon from '@mui/icons-material/Add'
import RemoveIcon from '@mui/icons-material/Remove'
import {
  Button,
  Card,
  CardContent,
  IconButton,
  Stack,
  Typography,
} from '@mui/material'
import { Link as RouterLink, useNavigate } from 'react-router-dom'
import { EmptyState } from '@/components/feedback/States'
import { useCart } from '@/contexts/CartContext'
import { formatCurrency } from '@/utils/format'

export function CartPage() {
  const { items, setQuantity, removeItem, restaurantId } = useCart()
  const navigate = useNavigate()
  const total = items.reduce((sum, item) => sum + item.unitPrice * item.quantity, 0)

  if (items.length === 0) {
    return (
      <EmptyState
        title="Carrinho vazio"
        description="Adicione itens de um restaurante para continuar."
        actionLabel="Ver restaurantes"
        onAction={() => navigate('/')}
      />
    )
  }

  return (
    <Stack spacing={3} maxWidth={720}>
      <Typography variant="h4">Carrinho</Typography>
      <Typography color="text.secondary">{items[0]?.restaurantName}</Typography>
      {items.map((item) => (
        <Card key={item.key}>
          <CardContent>
            <Stack direction="row" justifyContent="space-between" alignItems="center" gap={2}>
              <Stack spacing={0.5} flex={1}>
                <Typography fontWeight={600}>{item.productName}</Typography>
                {item.specLabels.length > 0 ? (
                  <Typography variant="body2" color="text.secondary">
                    {item.specLabels.join(' · ')}
                  </Typography>
                ) : null}
                <Typography>{formatCurrency(item.unitPrice * item.quantity)}</Typography>
              </Stack>
              <Stack direction="row" alignItems="center" spacing={1}>
                <IconButton
                  aria-label="Diminuir quantidade"
                  onClick={() => setQuantity(item.key, item.quantity - 1)}
                >
                  <RemoveIcon />
                </IconButton>
                <Typography aria-label={`Quantidade ${item.quantity}`}>{item.quantity}</Typography>
                <IconButton
                  aria-label="Aumentar quantidade"
                  onClick={() => setQuantity(item.key, item.quantity + 1)}
                >
                  <AddIcon />
                </IconButton>
                <Button color="error" onClick={() => removeItem(item.key)}>
                  Remover
                </Button>
              </Stack>
            </Stack>
          </CardContent>
        </Card>
      ))}
      <Stack direction="row" justifyContent="space-between" alignItems="center">
        <Typography variant="h6">Total {formatCurrency(total)}</Typography>
        <Button
          component={RouterLink}
          to="/checkout"
          variant="contained"
          size="large"
          disabled={!restaurantId}
        >
          Finalizar pedido
        </Button>
      </Stack>
    </Stack>
  )
}
