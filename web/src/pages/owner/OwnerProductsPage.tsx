import { zodResolver } from '@hookform/resolvers/zod'
import {
  Button,
  Card,
  CardContent,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Stack,
  TextField,
  Typography,
} from '@mui/material'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { useState } from 'react'
import { Controller, useForm } from 'react-hook-form'
import { Link as RouterLink } from 'react-router-dom'
import type { z } from 'zod'
import { getErrorMessage } from '@/api/httpClient'
import { EmptyState, ErrorState, PageLoader } from '@/components/feedback/States'
import { ProductSpecificationsFields } from '@/components/owner/ProductSpecificationsFields'
import { useOwnerRestaurant } from '@/contexts/OwnerRestaurantContext'
import { useSnackbar } from '@/contexts/SnackbarContext'
import { productService } from '@/services/productService'
import { formatCurrency } from '@/utils/format'
import { productSchema } from '@/utils/schemas'

type FormValues = z.infer<typeof productSchema>

const emptyProduct: FormValues = {
  name: '',
  price: 0,
  description: '',
  specifications: [],
}

function specsHint(specCount: number, optionCount: number): string | null {
  if (specCount === 0) return null
  const specsLabel = specCount === 1 ? '1 especificação' : `${specCount} especificações`
  const optionsLabel = optionCount === 1 ? '1 opção' : `${optionCount} opções`
  return `${specsLabel} · ${optionsLabel}`
}

export function OwnerProductsPage() {
  const { ownedRestaurant } = useOwnerRestaurant()
  const { notify } = useSnackbar()
  const queryClient = useQueryClient()
  const [open, setOpen] = useState(false)

  const query = useQuery({
    queryKey: ['owner-products', ownedRestaurant?.id],
    enabled: Boolean(ownedRestaurant?.id),
    queryFn: () => productService.listByRestaurant(ownedRestaurant!.id),
  })

  const { control, handleSubmit, reset } = useForm<FormValues>({
    resolver: zodResolver(productSchema),
    defaultValues: emptyProduct,
  })

  const createMutation = useMutation({
    mutationFn: (values: FormValues) =>
      productService.create({
        name: values.name,
        price: values.price,
        description: values.description,
        specifications: values.specifications,
      }),
    onSuccess: async () => {
      notify('Produto criado')
      setOpen(false)
      reset(emptyProduct)
      await queryClient.invalidateQueries({ queryKey: ['owner-products', ownedRestaurant?.id] })
    },
    onError: (error) => notify(getErrorMessage(error), 'error'),
  })

  if (!ownedRestaurant) return <PageLoader />
  if (query.isLoading) return <PageLoader />
  if (query.isError) {
    return <ErrorState message={getErrorMessage(query.error)} onRetry={() => void query.refetch()} />
  }

  const products = query.data ?? []

  return (
    <Stack spacing={3}>
      <Stack direction="row" justifyContent="space-between" alignItems="center">
        <Typography variant="h4">Cardápio</Typography>
        <Button
          variant="contained"
          onClick={() => {
            reset(emptyProduct)
            setOpen(true)
          }}
        >
          Novo produto
        </Button>
      </Stack>
      {products.length === 0 ? (
        <EmptyState title="Sem produtos" description="Cadastre o primeiro item do cardápio." />
      ) : (
        products.map((product) => {
          const specCount = product.specifications?.length ?? 0
          const optionCount =
            product.specifications?.reduce((sum, spec) => sum + (spec.specOptions?.length ?? 0), 0) ??
            0
          const hint = specsHint(specCount, optionCount)
          return (
            <Card key={product.id}>
              <CardContent>
                <Stack direction="row" justifyContent="space-between" alignItems="center" gap={2}>
                  <Stack>
                    <Typography fontWeight={600}>{product.name}</Typography>
                    <Typography variant="body2" color="text.secondary">
                      {formatCurrency(product.price)}
                    </Typography>
                    {hint ? (
                      <Typography variant="caption" color="text.secondary">
                        {hint}
                      </Typography>
                    ) : null}
                  </Stack>
                  <Button
                    component={RouterLink}
                    to={`/restaurant/products/${product.id}/edit`}
                    variant="outlined"
                  >
                    Editar
                  </Button>
                </Stack>
              </CardContent>
            </Card>
          )
        })
      )}
      <Dialog open={open} onClose={() => setOpen(false)} fullWidth maxWidth="md">
        <DialogTitle>Novo produto</DialogTitle>
        <DialogContent>
          <Stack
            spacing={2}
            pt={1}
            component="form"
            id="create-product-form"
            onSubmit={handleSubmit((v) => createMutation.mutate(v))}
          >
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
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpen(false)}>Cancelar</Button>
          <Button
            type="submit"
            form="create-product-form"
            variant="contained"
            disabled={createMutation.isPending}
          >
            Salvar
          </Button>
        </DialogActions>
      </Dialog>
    </Stack>
  )
}
