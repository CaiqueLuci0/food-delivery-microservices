import { zodResolver } from '@hookform/resolvers/zod'
import { Button, Card, CardContent, Stack, TextField, Typography } from '@mui/material'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { useState } from 'react'
import { Controller, useForm } from 'react-hook-form'
import { useNavigate, useParams } from 'react-router-dom'
import type { z } from 'zod'
import { getErrorMessage } from '@/api/httpClient'
import { ConfirmDialog } from '@/components/feedback/ConfirmDialog'
import { ErrorState, PageLoader } from '@/components/feedback/States'
import { ImageUploadField } from '@/components/forms/ImageUploadField'
import { ProductSpecificationsFields } from '@/components/owner/ProductSpecificationsFields'
import { useOwnerRestaurant } from '@/contexts/OwnerRestaurantContext'
import { useSnackbar } from '@/contexts/SnackbarContext'
import { productService } from '@/services/productService'
import type { Product } from '@/types/api'
import { productSchema } from '@/utils/schemas'

type FormValues = z.infer<typeof productSchema>

function toFormValues(product: Product): FormValues {
  return {
    name: product.name,
    price: product.price,
    description: product.description ?? '',
    specifications: (product.specifications ?? []).map((spec) => ({
      name: spec.name,
      description: spec.description ?? '',
      specOptions: (spec.specOptions ?? []).map((option) => ({
        name: option.name,
        description: option.description ?? '',
        extraPrice: option.extraPrice,
      })),
    })),
  }
}

export function EditProductPage() {
  const { id = '' } = useParams()
  const { ownedRestaurant } = useOwnerRestaurant()
  const { notify } = useSnackbar()
  const navigate = useNavigate()
  const queryClient = useQueryClient()
  const [confirmOpen, setConfirmOpen] = useState(false)
  const [uploading, setUploading] = useState(false)

  const query = useQuery({
    queryKey: ['product', id],
    enabled: Boolean(id),
    queryFn: () => productService.getById(id),
  })

  const { control, handleSubmit } = useForm<FormValues>({
    resolver: zodResolver(productSchema),
    values: query.data ? toFormValues(query.data) : undefined,
  })

  const updateMutation = useMutation({
    mutationFn: (values: FormValues) =>
      productService.update(id, {
        name: values.name,
        price: values.price,
        description: values.description,
        specifications: values.specifications,
      }),
    onSuccess: async () => {
      notify('Produto atualizado')
      await queryClient.invalidateQueries({ queryKey: ['product', id] })
      await queryClient.invalidateQueries({ queryKey: ['owner-products', ownedRestaurant?.id] })
    },
    onError: (error) => notify(getErrorMessage(error), 'error'),
  })

  const deleteMutation = useMutation({
    mutationFn: () => productService.remove(id),
    onSuccess: async () => {
      notify('Produto removido')
      await queryClient.invalidateQueries({ queryKey: ['owner-products', ownedRestaurant?.id] })
      navigate('/restaurant/products', { replace: true })
    },
    onError: (error) => notify(getErrorMessage(error), 'error'),
  })

  const handleImageSelect = async (file: File) => {
    setUploading(true)
    try {
      await productService.uploadImage(id, file)
      await queryClient.invalidateQueries({ queryKey: ['product', id] })
      await queryClient.invalidateQueries({ queryKey: ['owner-products', ownedRestaurant?.id] })
      notify('Imagem atualizada')
    } catch (error) {
      notify(getErrorMessage(error), 'error')
    } finally {
      setUploading(false)
    }
  }

  const handleImageClear = async () => {
    setUploading(true)
    try {
      await productService.removeImage(id)
      await queryClient.invalidateQueries({ queryKey: ['product', id] })
      await queryClient.invalidateQueries({ queryKey: ['owner-products', ownedRestaurant?.id] })
      notify('Imagem removida')
    } catch (error) {
      notify(getErrorMessage(error), 'error')
    } finally {
      setUploading(false)
    }
  }

  if (query.isLoading) return <PageLoader />
  if (query.isError) {
    return <ErrorState message={getErrorMessage(query.error)} onRetry={() => void query.refetch()} />
  }

  const product = query.data!

  return (
    <Stack spacing={3} maxWidth={720}>
      <Stack direction="row" alignItems="center" justifyContent="space-between" gap={2}>
        <Typography variant="h4">Editar produto</Typography>
        <Button variant="text" onClick={() => navigate('/restaurant/products')}>
          Voltar
        </Button>
      </Stack>
      <Card>
        <CardContent>
          <Stack
            spacing={2}
            component="form"
            onSubmit={handleSubmit((v) => updateMutation.mutate(v))}
          >
            <ImageUploadField
              label="Foto do produto"
              imageUrl={product.imageUrl}
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
              name="price"
              control={control}
              render={({ field, fieldState }) => (
                <TextField
                  {...field}
                  label="Preço"
                  type="number"
                  error={Boolean(fieldState.error)}
                  helperText={fieldState.error?.message}
                  fullWidth
                />
              )}
            />
            <Controller
              name="description"
              control={control}
              render={({ field }) => (
                <TextField {...field} value={field.value ?? ''} label="Descrição" fullWidth multiline />
              )}
            />
            <ProductSpecificationsFields control={control} />
            <Button type="submit" variant="contained" disabled={updateMutation.isPending || uploading}>
              Salvar
            </Button>
          </Stack>
        </CardContent>
      </Card>
      <Button color="error" variant="outlined" onClick={() => setConfirmOpen(true)}>
        Excluir produto
      </Button>
      <ConfirmDialog
        open={confirmOpen}
        title="Excluir produto?"
        description="O item sairá do cardápio."
        confirmLabel="Excluir"
        loading={deleteMutation.isPending}
        onClose={() => setConfirmOpen(false)}
        onConfirm={() => deleteMutation.mutate()}
      />
    </Stack>
  )
}
