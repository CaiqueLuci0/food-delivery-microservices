import {
  Box,
  Button,
  Card,
  CardContent,
  Checkbox,
  FormControlLabel,
  FormGroup,
  Stack,
  Typography,
} from '@mui/material'
import { useQuery } from '@tanstack/react-query'
import { useMemo, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { getErrorMessage } from '@/api/httpClient'
import { ErrorState, PageLoader } from '@/components/feedback/States'
import { useCart } from '@/contexts/CartContext'
import { useSnackbar } from '@/contexts/SnackbarContext'
import { productService } from '@/services/productService'
import { restaurantService } from '@/services/restaurantService'
import { formatCurrency } from '@/utils/format'

export function ProductDetailPage() {
  const { id = '' } = useParams()
  const { addItem } = useCart()
  const { notify } = useSnackbar()
  const navigate = useNavigate()
  const [selectedOptions, setSelectedOptions] = useState<Record<string, string>>({})

  const productQuery = useQuery({
    queryKey: ['product', id],
    enabled: Boolean(id),
    queryFn: () => productService.getById(id),
  })

  const restaurantQuery = useQuery({
    queryKey: ['restaurant', productQuery.data?.restaurantId],
    enabled: Boolean(productQuery.data?.restaurantId),
    queryFn: () => restaurantService.getById(productQuery.data!.restaurantId),
  })

  const extrasTotal = useMemo(() => {
    if (!productQuery.data) return 0
    return productQuery.data.specifications.reduce((sum, spec) => {
      const optionId = selectedOptions[spec.id]
      const option = spec.specOptions.find((o) => o.id === optionId)
      return sum + (option?.extraPrice ?? 0)
    }, 0)
  }, [productQuery.data, selectedOptions])

  if (productQuery.isLoading) return <PageLoader />
  if (productQuery.isError) {
    return (
      <ErrorState
        message={getErrorMessage(productQuery.error)}
        onRetry={() => void productQuery.refetch()}
      />
    )
  }

  const product = productQuery.data!

  const toggleOption = (specId: string, optionId: string) => {
    setSelectedOptions((prev) => ({
      ...prev,
      [specId]: prev[specId] === optionId ? '' : optionId,
    }))
  }

  const handleAdd = () => {
    const optionIds = Object.values(selectedOptions).filter(Boolean)
    const labels = product.specifications.flatMap((spec) => {
      const optionId = selectedOptions[spec.id]
      const option = spec.specOptions.find((o) => o.id === optionId)
      return option ? [`${spec.name}: ${option.name}`] : []
    })
    addItem({
      productId: product.id,
      productName: product.name,
      unitPrice: product.price + extrasTotal,
      restaurantId: product.restaurantId,
      restaurantName: restaurantQuery.data?.name ?? 'Restaurante',
      specOptionIds: optionIds,
      specLabels: labels,
    })
    notify('Adicionado ao carrinho')
    navigate(`/restaurants/${product.restaurantId}`)
  }

  return (
    <Stack spacing={3} maxWidth={720}>
      <Box>
        <Typography variant="h3">{product.name}</Typography>
        <Typography color="text.secondary" mt={1}>
          {product.description}
        </Typography>
        <Typography variant="h5" mt={2}>
          {formatCurrency(product.price + extrasTotal)}
        </Typography>
      </Box>
      {product.specifications.map((spec) => (
        <Card key={spec.id}>
          <CardContent>
            <Typography variant="h6" gutterBottom>
              {spec.name}
            </Typography>
            <FormGroup>
              {spec.specOptions.map((option) => (
                <FormControlLabel
                  key={option.id}
                  control={
                    <Checkbox
                      checked={selectedOptions[spec.id] === option.id}
                      onChange={() => toggleOption(spec.id, option.id)}
                    />
                  }
                  label={`${option.name} (+${formatCurrency(option.extraPrice)})`}
                />
              ))}
            </FormGroup>
          </CardContent>
        </Card>
      ))}
      <Button variant="contained" size="large" onClick={handleAdd}>
        Adicionar ao carrinho
      </Button>
    </Stack>
  )
}
