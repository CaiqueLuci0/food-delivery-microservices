import { zodResolver } from '@hookform/resolvers/zod'
import { Alert, Box, Button, Card, CardContent, Stack, TextField, Typography } from '@mui/material'
import { useMutation } from '@tanstack/react-query'
import { Controller, useForm } from 'react-hook-form'
import { Link as RouterLink, useLocation, useNavigate } from 'react-router-dom'
import type { z } from 'zod'
import { getErrorMessage } from '@/api/httpClient'
import { PasswordTextField } from '@/components/forms/PasswordTextField'
import { useAuth } from '@/contexts/AuthContext'
import { authService } from '@/services/userService'
import { loginSchema } from '@/utils/schemas'

type FormValues = z.infer<typeof loginSchema>

export function LoginPage() {
  const { setSession } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()
  const from = (location.state as { from?: string } | null)?.from ?? '/'

  const {
    control,
    handleSubmit,
    formState: { errors },
  } = useForm<FormValues>({
    resolver: zodResolver(loginSchema),
    defaultValues: { email: '', password: '' },
  })

  const mutation = useMutation({
    mutationFn: authService.login,
    onSuccess: (data) => {
      setSession(data.token, data.user)
      navigate(from, { replace: true })
    },
  })

  return (
    <Box maxWidth={440} mx="auto">
      <Card>
        <CardContent sx={{ p: { xs: 3, md: 4 } }}>
          <Stack spacing={3} component="form" onSubmit={handleSubmit((v) => mutation.mutate(v))}>
            <Box>
              <Typography variant="h4" gutterBottom>
                Entrar
              </Typography>
              <Typography color="text.secondary">Acesse sua conta DeliveryBacana</Typography>
            </Box>
            {mutation.isError ? <Alert severity="error">{getErrorMessage(mutation.error)}</Alert> : null}
            <Controller
              name="email"
              control={control}
              render={({ field }) => (
                <TextField
                  {...field}
                  label="E-mail"
                  type="email"
                  autoComplete="email"
                  error={Boolean(errors.email)}
                  helperText={errors.email?.message}
                  fullWidth
                />
              )}
            />
            <Controller
              name="password"
              control={control}
              render={({ field }) => (
                <PasswordTextField
                  {...field}
                  label="Senha"
                  autoComplete="current-password"
                  error={Boolean(errors.password)}
                  helperText={errors.password?.message}
                  fullWidth
                />
              )}
            />
            <Button type="submit" variant="contained" size="large" disabled={mutation.isPending}>
              {mutation.isPending ? 'Entrando…' : 'Entrar'}
            </Button>
            <Typography variant="body2" textAlign="center">
              Não tem conta?{' '}
              <Button component={RouterLink} to="/register" size="small">
                Cadastre-se
              </Button>
            </Typography>
          </Stack>
        </CardContent>
      </Card>
    </Box>
  )
}
