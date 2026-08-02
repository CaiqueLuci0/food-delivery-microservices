import { createTheme } from '@mui/material/styles'

export const theme = createTheme({
  palette: {
    mode: 'light',
    primary: {
      main: '#E23744',
      dark: '#C01F2C',
      light: '#FF6B75',
      contrastText: '#FFFFFF',
    },
    secondary: {
      main: '#1A1A1A',
      contrastText: '#FFFFFF',
    },
    background: {
      default: '#F7F7F5',
      paper: '#FFFFFF',
    },
    text: {
      primary: '#1A1A1A',
      secondary: '#5C5C5C',
    },
    success: { main: '#2E7D32' },
    error: { main: '#D32F2F' },
  },
  typography: {
    fontFamily: '"DM Sans", "Segoe UI", sans-serif',
    h1: { fontFamily: '"Fraunces", Georgia, serif', fontWeight: 700 },
    h2: { fontFamily: '"Fraunces", Georgia, serif', fontWeight: 700 },
    h3: { fontFamily: '"Fraunces", Georgia, serif', fontWeight: 650 },
    h4: { fontFamily: '"Fraunces", Georgia, serif', fontWeight: 650 },
    h5: { fontWeight: 650 },
    h6: { fontWeight: 600 },
    button: { textTransform: 'none', fontWeight: 600 },
  },
  shape: { borderRadius: 14 },
  components: {
    MuiButton: {
      styleOverrides: {
        root: {
          borderRadius: 999,
          paddingInline: 20,
          boxShadow: 'none',
          '&:hover': { boxShadow: 'none' },
        },
      },
    },
    MuiCard: {
      styleOverrides: {
        root: {
          borderRadius: 18,
          boxShadow: '0 8px 28px rgba(26, 26, 26, 0.06)',
          border: '1px solid rgba(26, 26, 26, 0.06)',
        },
      },
    },
    MuiPaper: {
      styleOverrides: {
        rounded: { borderRadius: 18 },
      },
    },
  },
})
