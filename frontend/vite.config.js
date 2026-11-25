import { defineConfig } from 'vite';

export default defineConfig({
  server: { port: 5173 },
  publicDir: false,
  build: {
    outDir: 'dist',
    rollupOptions: {
      input: {
        index: 'index.html',
        app: 'app.js'
      },
      output: {
        entryFileNames: '[name].js',
        assetFileNames: '[name][extname]'
      }
    }
  }
});
