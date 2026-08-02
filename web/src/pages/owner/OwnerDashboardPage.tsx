import { Button, Card, CardContent, Stack, Typography } from '@mui/material'
import { Link as RouterLink } from 'react-router-dom'
import { useOwnerRestaurant } from '@/contexts/OwnerRestaurantContext'
import { PageLoader } from '@/components/feedback/States'

export function OwnerDashboardPage() {
  const { ownedRestaurant, isLoading } = useOwnerRestaurant()

  if (isLoading) return <PageLoader />
  if (!ownedRestaurant) return null

  return (
    <Stack spacing={3}>
      <Typography variant="h4">{ownedRestaurant.name}</Typography>
      <Typography color="text.secondary">{ownedRestaurant.description}</Typography>
      <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
        <Card sx={{ flex: 1 }}>
          <CardContent>
            <Typography variant="h6" gutterBottom>
              Cardápio
            </Typography>
            <Button component={RouterLink} to="/restaurant/products" variant="contained">
              Gerenciar produtos
            </Button>
          </CardContent>
        </Card>
        <Card sx={{ flex: 1 }}>
          <CardContent>
            <Typography variant="h6" gutterBottom>
              Pedidos
            </Typography>
            <Button component={RouterLink} to="/restaurant/orders" variant="contained">
              Ver pedidos
            </Button>
          </CardContent>
        </Card>
        <Card sx={{ flex: 1 }}>
          <CardContent>
            <Typography variant="h6" gutterBottom>
              Dados
            </Typography>
            <Button component={RouterLink} to="/restaurant/edit" variant="outlined">
              Editar restaurante
            </Button>
          </CardContent>
        </Card>
      </Stack>
    </Stack>
  )
}
