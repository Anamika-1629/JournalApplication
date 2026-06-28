import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/public': 'http://localhost:8080',
      '/journal': 'http://localhost:8080',
      '/user': 'http://localhost:8080',
    }
  }
})
