import SearchIcon from '@mui/icons-material/Search'
import MyLocationIcon from '@mui/icons-material/MyLocation'
import {
  Box,
  Card,
  CardActionArea,
  CardContent,
  IconButton,
  InputAdornment,
  Stack,
  TextField,
  Typography,
} from '@mui/material'
import { useQuery } from '@tanstack/react-query'
import { useMemo, useState } from 'react'
import { Link as RouterLink } from 'react-router-dom'
import { getErrorMessage } from '@/api/httpClient'
import { ListSkeleton } from '@/components/feedback/Skeletons'
import { EmptyState, ErrorState } from '@/components/feedback/States'
import { useAuth } from '@/contexts/AuthContext'
import { useSnackbar } from '@/contexts/SnackbarContext'
import { restaurantService } from '@/services/restaurantService'

export function HomePage() {
  const { user } = useAuth()
  const { notify } = useSnackbar()
  const [search, setSearch] = useState('')
  const [coords, setCoords] = useState<{ latitude?: number; longitude?: number }>({})

  const params = useMemo(
    () => ({
      search: search.trim() || undefined,
      latitude: coords.latitude,
      longitude: coords.longitude,
    }),
    [search, coords],
  )

  const query = useQuery({
    queryKey: ['restaurants', params],
    queryFn: () => restaurantService.list(params),
  })

  const requestLocation = () => {
    const latitude = user?.address?.latitude
    const longitude = user?.address?.longitude
    if (latitude == null || longitude == null) {
      notify('Seu endereço cadastrado não possui coordenadas. Atualize o perfil.', 'warning')
      return
    }
    setCoords({ latitude, longitude })
    notify('Buscando restaurantes perto do seu endereço')
  }

  return (
    <Stack spacing={3}>
      <Box>
        <Typography variant="h3" gutterBottom>
          O que você vai pedir hoje?
        </Typography>
        <Typography color="text.secondary">Restaurantes perto de você</Typography>
      </Box>
      <TextField
        value={search}
        onChange={(e) => setSearch(e.target.value)}
        placeholder="Buscar restaurantes"
        fullWidth
        InputProps={{
          startAdornment: (
            <InputAdornment position="start">
              <SearchIcon />
            </InputAdornment>
          ),
          endAdornment: (
            <InputAdornment position="end">
              <IconButton
                aria-label="Usar localização do meu endereço cadastrado"
                onClick={requestLocation}
              >
                <MyLocationIcon />
              </IconButton>
            </InputAdornment>
          ),
        }}
      />
      {query.isLoading ? <ListSkeleton /> : null}
      {query.isError ? (
        <ErrorState message={getErrorMessage(query.error)} onRetry={() => void query.refetch()} />
      ) : null}
      {query.isSuccess && query.data.length === 0 ? (
        <EmptyState title="Nenhum restaurante encontrado" description="Tente outra busca." />
      ) : null}
      {query.isSuccess ? (
        <Box
          display="grid"
          gap={2}
          gridTemplateColumns={{ xs: '1fr', sm: '1fr 1fr', md: '1fr 1fr 1fr' }}
        >
          {query.data.map((restaurant) => (
            <Card key={restaurant.id}>
              <CardActionArea component={RouterLink} to={`/restaurants/${restaurant.id}`}>
                <Box
                  sx={{
                    height: 140,
                    background: 'linear-gradient(135deg, #FFE8EA 0%, #FFD0D4 50%, #F7F7F5 100%)',
                  }}
                />
                <CardContent>
                  <Typography variant="h6">{restaurant.name}</Typography>
                  <Typography variant="body2" color="text.secondary" noWrap>
                    {restaurant.description}
                  </Typography>
                </CardContent>
              </CardActionArea>
            </Card>
          ))}
        </Box>
      ) : null}
    </Stack>
  )
}
