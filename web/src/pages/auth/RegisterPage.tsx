import { zodResolver } from '@hookform/resolvers/zod'
import { Alert, Box, Button, Card, CardContent, Stack, TextField, Typography } from '@mui/material'
import { useMutation } from '@tanstack/react-query'
import { Controller, useForm } from 'react-hook-form'
import { Link as RouterLink, useNavigate } from 'react-router-dom'
import type { z } from 'zod'
import { getErrorMessage } from '@/api/httpClient'
import { AddressFields } from '@/components/forms/AddressFields'
import { useAuth } from '@/contexts/AuthContext'
import { useSnackbar } from '@/contexts/SnackbarContext'
import { authService, userService } from '@/services/userService'
import { registerSchema } from '@/utils/schemas'

type FormValues = z.infer<typeof registerSchema>

export function RegisterPage() {
  const navigate = useNavigate()
  const { setSession } = useAuth()
  const { notify } = useSnackbar()

  const { control, handleSubmit } = useForm<FormValues>({
    resolver: zodResolver(registerSchema),
    defaultValues: {
      name: '',
      email: '',
      password: '',
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
    mutationFn: async (values: FormValues) => {
      await userService.create(values)
      return authService.login({ email: values.email, password: values.password })
    },
    onSuccess: (data) => {
      setSession(data.token, data.user)
      notify('Conta criada com sucesso')
      navigate('/', { replace: true })
    },
  })

  return (
    <Box maxWidth={560} mx="auto">
      <Card>
        <CardContent sx={{ p: { xs: 3, md: 4 } }}>
          <Stack spacing={3} component="form" onSubmit={handleSubmit((v) => mutation.mutate(v))}>
            <Box>
              <Typography variant="h4" gutterBottom>
                Criar conta
              </Typography>
              <Typography color="text.secondary">Cadastro com endereço de entrega</Typography>
            </Box>
            {mutation.isError ? <Alert severity="error">{getErrorMessage(mutation.error)}</Alert> : null}
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
              name="email"
              control={control}
              render={({ field, fieldState }) => (
                <TextField
                  {...field}
                  label="E-mail"
                  type="email"
                  error={Boolean(fieldState.error)}
                  helperText={fieldState.error?.message}
                  fullWidth
                />
              )}
            />
            <Controller
              name="password"
              control={control}
              render={({ field, fieldState }) => (
                <TextField
                  {...field}
                  label="Senha"
                  type="password"
                  error={Boolean(fieldState.error)}
                  helperText={fieldState.error?.message}
                  fullWidth
                />
              )}
            />
            <Typography variant="h6">Endereço</Typography>
            <AddressFields control={control} prefix="address" />
            <Button type="submit" variant="contained" size="large" disabled={mutation.isPending}>
              {mutation.isPending ? 'Salvando…' : 'Cadastrar'}
            </Button>
            <Typography variant="body2" textAlign="center">
              Já tem conta?{' '}
              <Button component={RouterLink} to="/login" size="small">
                Entrar
              </Button>
            </Typography>
          </Stack>
        </CardContent>
      </Card>
    </Box>
  )
}
