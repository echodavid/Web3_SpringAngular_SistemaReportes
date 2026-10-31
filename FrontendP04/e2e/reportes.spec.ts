import { test, expect, Page } from '@playwright/test';
import { mkdirSync, readFileSync } from 'fs';

function demoPassword(): string {
  if (process.env['DEMO_PASSWORD']) return process.env['DEMO_PASSWORD'];
  const lines = readFileSync('../.env', 'utf8').trim().split('\n');
  const values = Object.fromEntries(
    lines.filter((line) => line && !line.startsWith('#')).map((line) => {
      const at = line.indexOf('=');
      return [line.slice(0, at), line.slice(at + 1)];
    }),
  );
  if (!values['DEMO_PASSWORD']) throw new Error('Falta DEMO_PASSWORD en el entorno o en .env');
  return values['DEMO_PASSWORD'];
}

const password = demoPassword();
const evidenceDir = process.env['EVIDENCE_DIR'] || '../docs/evidence';
mkdirSync(evidenceDir, { recursive: true });

async function login(page: Page, role: string, navigate = true) {
  if (navigate) await page.goto('/');
  await page.getByLabel('Correo de prueba').fill(`${role}@example.test`);
  await page.getByLabel('Contraseña', { exact: true }).fill(password);
  await page.getByRole('button', { name: 'Entrar', exact: true }).click();
  const heading = role === 'ciudadano' ? 'Mis reportes' : 'Bandeja de atención';
  await expect(page.getByRole('heading', { name: heading })).toBeVisible();
}

async function createReport(page: Page, title: string, description: string) {
  await page.getByRole('button', { name: '+ Nuevo reporte' }).click();
  await page.getByLabel('Título', { exact: true }).fill(title);
  await page.getByLabel('Categoría', { exact: true }).selectOption({ index: 1 });
  await page.getByLabel('Zona general', { exact: true }).selectOption({ index: 1 });
  await page.getByLabel('Descripción ficticia').fill(description);
  await page.getByRole('button', { name: 'Registrar reporte', exact: true }).click();
  await expect(page.getByRole('heading', { name: title, exact: true })).toBeVisible();
}

function noHorizontalOverflow(page: Page) {
  return page.evaluate(() => ({
    scrollWidth: document.documentElement.scrollWidth,
    innerWidth: window.innerWidth,
  }));
}

test('ciudadano registra, recarga, agrega evidencia y operador cambia estado', async ({ page }) => {
  const errors: string[] = [];
  page.on('pageerror', (error) => errors.push(error.message));
  const title = `Reporte navegador ${Date.now()}`;
  const evidence = 'simulada://navegador/prueba';

  await login(page, 'ciudadano');
  await createReport(page, title, 'Incidencia sintética para demostrar persistencia.');
  await page.getByLabel('Referencia ficticia').fill(evidence);
  await page.getByRole('button', { name: 'Agregar evidencia' }).click();
  await expect(page.getByText(evidence, { exact: true })).toBeVisible();

  // Persistencia después de recargar la aplicación: el reporte y su evidencia siguen en PostgreSQL.
  await page.reload();
  await expect(page.getByLabel('Correo de prueba')).toBeVisible();
  await login(page, 'ciudadano', false);
  const row = page.getByRole('row').filter({ hasText: title });
  await expect(row).toBeVisible();
  await row.getByRole('button').click();
  await expect(page.getByText(evidence, { exact: true })).toBeVisible();
  await page.screenshot({ path: `${evidenceDir}/ciudadano.png`, fullPage: true });

  await page.getByRole('button', { name: 'Cerrar sesión' }).click();
  await login(page, 'operador', false);
  await page.getByRole('row').filter({ hasText: title }).getByRole('button').click();
  await page.getByLabel('Nuevo estado').selectOption('EN_REVISION');
  await page.getByLabel('Comentario', { exact: true }).fill('Revisión ficticia desde el navegador.');
  await page.getByRole('button', { name: 'Guardar cambio y seguimiento' }).click();
  await expect(page.getByText('Revisión ficticia desde el navegador.', { exact: true })).toBeVisible();
  await page.screenshot({ path: `${evidenceDir}/operador.png`, fullPage: true });

  expect(errors).toEqual([]);
});

test('muestra error de conexión y permite reintentar', async ({ page }) => {
  await login(page, 'ciudadano');
  await page.route('**/api/v1/reportes?*', (route) => route.abort('failed'));
  await page.getByRole('button', { name: 'Bandeja de reportes' }).click();
  await expect(page.getByRole('alert')).toContainText('No se pudo conectar con la API');
  await page.unroute('**/api/v1/reportes?*');
  await page.getByRole('button', { name: 'Reintentar conexión' }).click();
  await expect(page.getByRole('alert')).not.toBeVisible();
  await expect(page.getByRole('heading', { name: 'Mis reportes' })).toBeVisible();
});

test('no presenta desbordamiento horizontal en móvil', async ({ page }) => {
  await page.setViewportSize({ width: 390, height: 844 });
  await login(page, 'ciudadano');
  const title = `Reporte móvil ${Date.now()}`;
  await createReport(page, title, 'Incidencia sintética para comprobar el diseño móvil.');

  await page.getByRole('button', { name: 'Bandeja de reportes' }).click();
  await expect(page.getByRole('row').filter({ hasText: title })).toBeVisible();
  const list = await noHorizontalOverflow(page);
  expect(list.scrollWidth).toBeLessThanOrEqual(list.innerWidth + 1);

  await page.getByRole('row').filter({ hasText: title }).getByRole('button').click();
  await expect(page.getByRole('heading', { name: title, exact: true })).toBeVisible();
  const detail = await noHorizontalOverflow(page);
  expect(detail.scrollWidth).toBeLessThanOrEqual(detail.innerWidth + 1);
});

test('presenta un error de credenciales de la API', async ({ page }) => {
  await page.goto('/');
  await page.getByLabel('Correo de prueba').fill('noexiste@example.test');
  await page.getByLabel('Contraseña', { exact: true }).fill('ContraseñaErrónea123!');
  await page.getByRole('button', { name: 'Entrar', exact: true }).click();
  await expect(page.getByRole('alert')).toContainText('HTTP 401');
});
