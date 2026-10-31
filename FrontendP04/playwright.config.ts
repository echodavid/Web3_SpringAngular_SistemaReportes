import { defineConfig } from '@playwright/test';

export default defineConfig({
  testDir: './e2e',
  outputDir: process.env['PLAYWRIGHT_OUTPUT_DIR'] || './test-results',
  use: {
    baseURL: process.env['FRONTEND_URL'] || 'http://localhost:4200',
    trace: 'retain-on-failure',
    launchOptions: { executablePath: process.env['CHROMIUM_PATH'] },
  },
  workers: 1,
  reporter: 'list',
});
