import SearchIcon from '@mui/icons-material/Search'
import {
  Box,
  Card,
  CardActionArea,
  CardContent,
  InputAdornment,
  Stack,
  TextField,
  Typography,
} from '@mui/material'
import { useQuery } from '@tanstack/react-query'
import { useState } from 'react'
import { Link as RouterLink, useParams } from 'react-router-dom'
import { getErrorMessage } from '@/api/httpClient'
import { ListSkeleton } from '@/components/feedback/Skeletons'
import { EmptyState, ErrorState, PageLoader } from '@/components/feedback/States'
import { productService } from '@/services/productService'
import { restaurantService } from '@/services/restaurantService'
import { formatCurrency } from '@/utils/format'

export function RestaurantDetailPage() {
  const { id = '' } = useParams()
  const [search, setSearch] = useState('')

  const restaurantQuery = useQuery({
    queryKey: ['restaurant', id],
    enabled: Boolean(id),
    queryFn: () => restaurantService.getById(id),
  })

  const productsQuery = useQuery({
    queryKey: ['products', id, search],
    enabled: Boolean(id),
    queryFn: () => productService.listByRestaurant(id, search.trim() || undefined),
  })

  if (restaurantQuery.isLoading) return <PageLoader />
  if (restaurantQuery.isError) {
    return (
      <ErrorState
        message={getErrorMessage(restaurantQuery.error)}
        onRetry={() => void restaurantQuery.refetch()}
      />
    )
  }

  const restaurant = restaurantQuery.data!

  return (
    <Stack spacing={3}>
      <Box>
        <Typography variant="h3">{restaurant.name}</Typography>
        <Typography color="text.secondary" mt={1}>
          {restaurant.description}
        </Typography>
      </Box>
      <TextField
        value={search}
        onChange={(e) => setSearch(e.target.value)}
        placeholder="Buscar no cardápio"
        fullWidth
        InputProps={{
          startAdornment: (
            <InputAdornment position="start">
              <SearchIcon />
            </InputAdornment>
          ),
        }}
      />
      {productsQuery.isLoading ? <ListSkeleton count={4} /> : null}
      {productsQuery.isError ? (
        <ErrorState
          message={getErrorMessage(productsQuery.error)}
          onRetry={() => void productsQuery.refetch()}
        />
      ) : null}
      {productsQuery.isSuccess && productsQuery.data.length === 0 ? (
        <EmptyState title="Cardápio vazio" description="Nenhum produto neste restaurante." />
      ) : null}
      {productsQuery.isSuccess
        ? productsQuery.data.map((product) => (
            <Card key={product.id}>
              <CardActionArea component={RouterLink} to={`/products/${product.id}`}>
                <CardContent>
                  <Stack direction="row" justifyContent="space-between" gap={2}>
                    <Box>
                      <Typography variant="h6">{product.name}</Typography>
                      <Typography variant="body2" color="text.secondary">
                        {product.description}
                      </Typography>
                    </Box>
                    <Typography fontWeight={700}>{formatCurrency(product.price)}</Typography>
                  </Stack>
                </CardContent>
              </CardActionArea>
            </Card>
          ))
        : null}
    </Stack>
  )
}
