import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import path from 'node:path'
import { fileURLToPath } from 'node:url'

const rootDir = path.dirname(fileURLToPath(import.meta.url))

export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      '@': path.resolve(rootDir, './src'),
    },
  },
  server: {
    port: 5173,
    proxy: {
      '/user-ms': 'http://localhost:8080',
      '/restaurant-ms': 'http://localhost:8080',
      '/catalog-ms': 'http://localhost:8080',
      '/order-ms': 'http://localhost:8080',
      '/payment-ms': 'http://localhost:8080',
    },
  },
})
