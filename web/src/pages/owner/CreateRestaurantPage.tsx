import { zodResolver } from '@hookform/resolvers/zod'
import { Alert, Button, Card, CardContent, Stack, TextField, Typography } from '@mui/material'
import { useMutation } from '@tanstack/react-query'
import { Controller, useForm } from 'react-hook-form'
import { Navigate, useNavigate } from 'react-router-dom'
import type { z } from 'zod'
import { getErrorMessage } from '@/api/httpClient'
import { AddressFields } from '@/components/forms/AddressFields'
import { PageLoader } from '@/components/feedback/States'
import { useOwnerRestaurant } from '@/contexts/OwnerRestaurantContext'
import { useSnackbar } from '@/contexts/SnackbarContext'
import { restaurantService } from '@/services/restaurantService'
import { restaurantSchema } from '@/utils/schemas'

type FormValues = z.infer<typeof restaurantSchema>

export function CreateRestaurantPage() {
  const { ownedRestaurant, isLoading, refetch } = useOwnerRestaurant()
  const { notify } = useSnackbar()
  const navigate = useNavigate()

  const { control, setValue, handleSubmit } = useForm<FormValues>({
    resolver: zodResolver(restaurantSchema),
    defaultValues: {
      name: '',
      description: '',
      address: {
        cep: '',
        logradouro: '',
        numero: '',
        complemento: '',
        bairro: '',
        cidade: '',
        uf: '',
        referencia: '',
      },
    },
  })

  const mutation = useMutation({
    mutationFn: restaurantService.create,
    onSuccess: () => {
      notify('Restaurante criado')
      refetch()
      navigate('/restaurant', { replace: true })
    },
  })

  if (isLoading) return <PageLoader />
  if (ownedRestaurant) return <Navigate to="/restaurant" replace />

  return (
    <Stack spacing={3} maxWidth={640}>
      <Typography variant="h4">Criar restaurante</Typography>
      <Card>
        <CardContent>
          <Stack spacing={2} component="form" onSubmit={handleSubmit((v) => mutation.mutate(v))}>
            {mutation.isError ? (
              <Alert severity="error">{getErrorMessage(mutation.error)}</Alert>
            ) : null}
            <Controller
              name="name"
              control={control}
              render={({ field, fieldState }) => (
                <TextField
                  {...field}
                  label="Nome"
                  error={Boolean(fieldState.error)}
                  helperText={fieldState.error?.message}
                  fullWidth
                />
              )}
            />
            <Controller
              name="description"
              control={control}
              render={({ field, fieldState }) => (
                <TextField
                  {...field}
                  label="Descrição"
                  multiline
                  minRows={3}
                  error={Boolean(fieldState.error)}
                  helperText={fieldState.error?.message}
                  fullWidth
                />
              )}
            />
            <Typography variant="h6">Endereço</Typography>
            <AddressFields control={control} setValue={setValue} prefix="address" />
            <Button type="submit" variant="contained" disabled={mutation.isPending}>
              {mutation.isPending ? 'Salvando…' : 'Criar'}
            </Button>
          </Stack>
        </CardContent>
      </Card>
    </Stack>
  )
}
