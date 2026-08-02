import { zodResolver } from '@hookform/resolvers/zod'
import {
  Button,
  Card,
  CardContent,
  Stack,
  TextField,
  Typography,
} from '@mui/material'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { useState } from 'react'
import { Controller, useForm } from 'react-hook-form'
import { useNavigate } from 'react-router-dom'
import { z } from 'zod'
import { getErrorMessage } from '@/api/httpClient'
import { ConfirmDialog } from '@/components/feedback/ConfirmDialog'
import { ErrorState, PageLoader } from '@/components/feedback/States'
import { useAuth } from '@/contexts/AuthContext'
import { useSnackbar } from '@/contexts/SnackbarContext'
import { userService } from '@/services/userService'

const profileSchema = z.object({
  name: z.string().min(2, 'Nome muito curto'),
})

type FormValues = z.infer<typeof profileSchema>

export function ProfilePage() {
  const { user, updateUser, logout } = useAuth()
  const { notify } = useSnackbar()
  const navigate = useNavigate()
  const queryClient = useQueryClient()
  const [confirmOpen, setConfirmOpen] = useState(false)

  const query = useQuery({
    queryKey: ['user', user?.id],
    enabled: Boolean(user?.id),
    queryFn: () => userService.getById(user!.id),
  })

  const { control, handleSubmit, reset } = useForm<FormValues>({
    resolver: zodResolver(profileSchema),
    values: { name: query.data?.name ?? user?.name ?? '' },
  })

  const updateMutation = useMutation({
    mutationFn: (values: FormValues) => userService.update(user!.id, values),
    onSuccess: (data) => {
      updateUser(data)
      void queryClient.invalidateQueries({ queryKey: ['user', user?.id] })
      notify('Perfil atualizado')
      reset({ name: data.name })
    },
    onError: (error) => notify(getErrorMessage(error), 'error'),
  })

  const deleteMutation = useMutation({
    mutationFn: () => userService.remove(user!.id),
    onSuccess: () => {
      logout()
      notify('Conta excluída')
      navigate('/login', { replace: true })
    },
    onError: (error) => notify(getErrorMessage(error), 'error'),
  })

  if (query.isLoading) return <PageLoader />
  if (query.isError) {
    return <ErrorState message={getErrorMessage(query.error)} onRetry={() => void query.refetch()} />
  }

  const profile = query.data

  return (
    <Stack spacing={3} maxWidth={560}>
      <Typography variant="h4">Meu perfil</Typography>
      <Card>
        <CardContent>
          <Stack spacing={2} component="form" onSubmit={handleSubmit((v) => updateMutation.mutate(v))}>
            <Typography color="text.secondary">{profile?.email}</Typography>
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
            {profile?.address ? (
              <Typography variant="body2" color="text.secondary">
                {profile.address.logradouro}, {profile.address.numero} — {profile.address.bairro},{' '}
                {profile.address.cidade}/{profile.address.uf}
              </Typography>
            ) : null}
            <Button type="submit" variant="contained" disabled={updateMutation.isPending}>
              Salvar
            </Button>
          </Stack>
        </CardContent>
      </Card>
      <Button color="error" variant="outlined" onClick={() => setConfirmOpen(true)}>
        Excluir conta
      </Button>
      <ConfirmDialog
        open={confirmOpen}
        title="Excluir conta?"
        description="Esta ação não pode ser desfeita."
        confirmLabel="Excluir"
        loading={deleteMutation.isPending}
        onClose={() => setConfirmOpen(false)}
        onConfirm={() => deleteMutation.mutate()}
      />
    </Stack>
  )
}
