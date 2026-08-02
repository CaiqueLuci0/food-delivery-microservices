import { Box, Container } from '@mui/material'
import { Outlet } from 'react-router-dom'
import { AppHeader } from '@/components/layout/AppHeader'

export function MainLayout() {
  return (
    <Box minHeight="100vh" bgcolor="background.default">
      <AppHeader />
      <Container maxWidth="lg" sx={{ py: { xs: 2, md: 4 } }}>
        <Outlet />
      </Container>
    </Box>
  )
}
