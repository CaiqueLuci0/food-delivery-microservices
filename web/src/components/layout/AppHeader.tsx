import StorefrontIcon from '@mui/icons-material/Storefront'
import ShoppingCartOutlinedIcon from '@mui/icons-material/ShoppingCartOutlined'
import ReceiptLongIcon from '@mui/icons-material/ReceiptLong'
import PersonOutlineIcon from '@mui/icons-material/PersonOutlineOutlined'
import {
  AppBar,
  Badge,
  Box,
  Button,
  Container,
  IconButton,
  Toolbar,
  Typography,
} from '@mui/material'
import { Link as RouterLink, useNavigate } from 'react-router-dom'
import { useAuth } from '@/contexts/AuthContext'
import { useCart } from '@/contexts/CartContext'
import { useOwnerRestaurant } from '@/contexts/OwnerRestaurantContext'

export function AppHeader() {
  const { isAuthenticated, logout, user } = useAuth()
  const { itemCount } = useCart()
  const { ownedRestaurant } = useOwnerRestaurant()
  const navigate = useNavigate()

  return (
    <AppBar
      position="sticky"
      elevation={0}
      sx={{
        bgcolor: 'background.paper',
        color: 'text.primary',
        borderBottom: '1px solid',
        borderColor: 'divider',
      }}
    >
      <Container maxWidth="lg">
        <Toolbar disableGutters sx={{ gap: 1, minHeight: 72 }}>
          <Typography
            component={RouterLink}
            to="/"
            variant="h5"
            sx={{ textDecoration: 'none', color: 'primary.main', mr: 'auto' }}
          >
            DeliveryBacana
          </Typography>

          {isAuthenticated ? (
            <>
              {ownedRestaurant ? (
                <Button
                  component={RouterLink}
                  to="/restaurant"
                  startIcon={<StorefrontIcon />}
                  aria-label="Meu restaurante"
                >
                  Meu restaurante
                </Button>
              ) : (
                <Button component={RouterLink} to="/restaurant/new" startIcon={<StorefrontIcon />}>
                  Criar restaurante
                </Button>
              )}
              <IconButton
                component={RouterLink}
                to="/orders"
                aria-label="Meus pedidos"
                color="inherit"
              >
                <ReceiptLongIcon />
              </IconButton>
              <IconButton component={RouterLink} to="/cart" aria-label="Carrinho" color="inherit">
                <Badge badgeContent={itemCount} color="primary">
                  <ShoppingCartOutlinedIcon />
                </Badge>
              </IconButton>
              <IconButton
                component={RouterLink}
                to="/profile"
                aria-label={`Perfil de ${user?.name ?? 'usuário'}`}
                color="inherit"
              >
                <PersonOutlineIcon />
              </IconButton>
              <Button
                onClick={() => {
                  logout()
                  navigate('/login')
                }}
              >
                Sair
              </Button>
            </>
          ) : (
            <Box display="flex" gap={1}>
              <Button component={RouterLink} to="/login">
                Entrar
              </Button>
              <Button component={RouterLink} to="/register" variant="contained">
                Cadastrar
              </Button>
            </Box>
          )}
        </Toolbar>
      </Container>
    </AppBar>
  )
}
