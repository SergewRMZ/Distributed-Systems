import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import { VitePWA } from 'vite-plugin-pwa'

export default defineConfig({
  plugins: [react(),
    VitePWA({
      registerType: 'autoUpdate',
      injectRegister: 'auto',
      
      manifest: {
        name: 'Cloud Storage App',
        short_name: 'CloudStorage',
        description: 'Aplicación para subir y gestionar archivos en la nube.',
        theme_color: '#1976d2',
        background_color: '#ffffff',
        display: 'standalone',
        icons: [
          {
            src: 'nube-192x192.png',
            sizes: '192x192',
            type: 'image/png'
          },
          {
            src: 'nube-512x512.png',
            sizes: '512x512',
            type: 'image/png'
          }
        ]
      }
    })
  ],
})
