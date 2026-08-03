import { zodResolver } from '@hookform/resolvers/zod'
import { Alert, Button, Card, CardContent, Stack, TextField, Typography } from '@mui/material'
import { useMutation } from '@tanstack/react-query'
import { useState } from 'react'
import { Controller, useForm } from 'react-hook-form'
import { useNavigate } from 'react-router-dom'
import type { z } from 'zod'
import { getErrorMessage } from '@/api/httpClient'
import { ConfirmDialog } from '@/components/feedback/ConfirmDialog'
import { AddressFields } from '@/components/forms/AddressFields'
import { ImageUploadField } from '@/components/forms/ImageUploadField'
import { PageLoader } from '@/components/feedback/States'
import { useOwnerRestaurant } from '@/contexts/OwnerRestaurantContext'
import { useSnackbar } from '@/contexts/SnackbarContext'
import { restaurantService } from '@/services/restaurantService'
import { restaurantSchema } from '@/utils/schemas'

type FormValues = z.infer<typeof restaurantSchema>

export function EditRestaurantPage() {
  const { ownedRestaurant, isLoading, refetch } = useOwnerRestaurant()
  const { notify } = useSnackbar()
  const navigate = useNavigate()
  const [confirmOpen, setConfirmOpen] = useState(false)
  const [uploading, setUploading] = useState(false)

  const { control, setValue, handleSubmit } = useForm<FormValues>({
    resolver: zodResolver(restaurantSchema),
    values: ownedRestaurant
      ? {
          name: ownedRestaurant.name,
          description: ownedRestaurant.description,
          address: {
            cep: ownedRestaurant.address.cep,
            logradouro: ownedRestaurant.address.logradouro,
            numero: ownedRestaurant.address.numero,
            complemento: ownedRestaurant.address.complemento ?? '',
            bairro: ownedRestaurant.address.bairro,
            cidade: ownedRestaurant.address.cidade,
            uf: ownedRestaurant.address.uf,
            referencia: ownedRestaurant.address.referencia ?? '',
          },
        }
      : undefined,
  })

  const updateMutation = useMutation({
    mutationFn: (values: FormValues) => restaurantService.update(ownedRestaurant!.id, values),
    onSuccess: () => {
      notify('Restaurante atualizado')
      refetch()
    },
    onError: (error) => notify(getErrorMessage(error), 'error'),
  })

  const deleteMutation = useMutation({
    mutationFn: () => restaurantService.remove(ownedRestaurant!.id),
    onSuccess: () => {
      notify('Restaurante excluído')
      refetch()
      navigate('/', { replace: true })
    },
    onError: (error) => notify(getErrorMessage(error), 'error'),
  })

  const handleImageSelect = async (file: File) => {
    if (!ownedRestaurant) return
    setUploading(true)
    try {
      await restaurantService.uploadImage(ownedRestaurant.id, file)
      await refetch()
      notify('Imagem atualizada')
    } catch (error) {
      notify(getErrorMessage(error), 'error')
    } finally {
      setUploading(false)
    }
  }

  const handleImageClear = async () => {
    if (!ownedRestaurant) return
    setUploading(true)
    try {
      await restaurantService.removeImage(ownedRestaurant.id)
      await refetch()
      notify('Imagem removida')
    } catch (error) {
      notify(getErrorMessage(error), 'error')
    } finally {
      setUploading(false)
    }
  }

  if (isLoading || !ownedRestaurant) return <PageLoader />

  return (
    <Stack spacing={3} maxWidth={640}>
      <Typography variant="h4">Editar restaurante</Typography>
      <Card>
        <CardContent>
          <Stack
            spacing={2}
            component="form"
            onSubmit={handleSubmit((v) => updateMutation.mutate(v))}
          >
            {updateMutation.isError ? (
              <Alert severity="error">{getErrorMessage(updateMutation.error)}</Alert>
            ) : null}
            <ImageUploadField
              label="Imagem de perfil"
              imageUrl={ownedRestaurant.imageUrl}
              uploading={uploading}
              onSelect={(file) => void handleImageSelect(file)}
              onClear={() => void handleImageClear()}
            />
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
            <Button type="submit" variant="contained" disabled={updateMutation.isPending || uploading}>
              Salvar
            </Button>
          </Stack>
        </CardContent>
      </Card>
      <Button color="error" variant="outlined" onClick={() => setConfirmOpen(true)}>
        Excluir restaurante
      </Button>
      <ConfirmDialog
        open={confirmOpen}
        title="Excluir restaurante?"
        description="Produtos do catálogo vinculados também serão removidos."
        confirmLabel="Excluir"
        loading={deleteMutation.isPending}
        onClose={() => setConfirmOpen(false)}
        onConfirm={() => deleteMutation.mutate()}
      />
    </Stack>
  )
}
