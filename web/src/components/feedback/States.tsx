import { Box, Button, CircularProgress, Stack, Typography } from '@mui/material'
import type { ReactNode } from 'react'

export function PageLoader(): ReactNode {
  return (
    <Box display="flex" justifyContent="center" py={8}>
      <CircularProgress aria-label="Carregando" />
    </Box>
  )
}

export function EmptyState({
  title,
  description,
  actionLabel,
  onAction,
}: {
  title: string
  description?: string
  actionLabel?: string
  onAction?: () => void
}): ReactNode {
  return (
    <Stack alignItems="center" spacing={1.5} py={8} textAlign="center">
      <Typography variant="h5">{title}</Typography>
      {description ? (
        <Typography color="text.secondary" maxWidth={420}>
          {description}
        </Typography>
      ) : null}
      {actionLabel && onAction ? (
        <Button variant="contained" onClick={onAction}>
          {actionLabel}
        </Button>
      ) : null}
    </Stack>
  )
}

export function ErrorState({
  message,
  onRetry,
}: {
  message: string
  onRetry?: () => void
}): ReactNode {
  return (
    <Stack alignItems="center" spacing={1.5} py={8} textAlign="center">
      <Typography variant="h5" color="error">
        Algo deu errado
      </Typography>
      <Typography color="text.secondary" maxWidth={420}>
        {message}
      </Typography>
      {onRetry ? (
        <Button variant="outlined" onClick={onRetry}>
          Tentar novamente
        </Button>
      ) : null}
    </Stack>
  )
}
