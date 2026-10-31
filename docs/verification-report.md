# Estado de verificación · P04/P05 · PR07

Fecha: **2026-10-09**, America/Mexico_City. Actualizado el **2026-10-30**. Los resultados corresponden al código de esta entrega; no se reutilizan resultados del backend Java 21 original. La arquitectura y los contratos provienen de P04; las pruebas, el wrapper y el despliegue reproducible corresponden a la continuidad P05.

| Comprobación | Estado | Evidencia / alcance |
| --- | --- | --- |
| Compilación y paquete Spring Boot 2.7.18 con --release 11 | VERIFICADO | evidence/backend-summary.txt; Maven 3.9.9 ejecutado con JDK 17, objetivo de bytecode 11 |
| 15 pruebas automatizadas Spring/Mockito/política de transiciones | VERIFICADO | evidence/backend-summary-2026-10-30.txt; 13 integración MockMvc/H2 (incluida concurrencia 200/409), 1 servicio, 1 matriz de 75 combinaciones; cero fallos |
| Arranque del JAR en Java 11 | VERIFICADO | Temurin JRE 11.0.32.1, esquema PostgreSQL validado |
| Persistencia real con PostgreSQL 16.14 | VERIFICADO | evidence/integration.json (2026-10-30) y evidence/integration-java11.json; seis entidades del dominio y seguimiento persistido |
| Positivos y negativos HTTP | VERIFICADO | evidence/integration.json: 20 comprobaciones; 201 creación/evidencia/evento; 200 consulta/cambios; 400/401/403/409 controlados; salto inválido conserva estado |
| Build Angular 16.2.12 con Node 18.20.8 | VERIFICADO | evidence/frontend-build.txt; evidence/frontend-build-2026-10-30.txt sin errores tras los cambios de reintento y CSS móvil |
| Sintaxis y tipos de las pruebas Playwright | VERIFICADO | `tsc --noEmit` sin errores y evidence/playwright-discovery.txt descubre 4 pruebas |
| Recorrido navegador Angular → API → PostgreSQL | VERIFICADO | evidence/browser.log (4 pruebas, 2026-10-30) y capturas ciudadano.png / operador.png; alta, recarga, evidencia y cambio de estado real |
| Error HTTP 401 visible en Angular | VERIFICADO | Prueba Playwright de credenciales inválidas; no usa mocks |
| OpenAPI exportado | VERIFICADO | openapi.json y openapi-runtime.json (3.0.3, 14 rutas, 16 esquemas); obtenido del balanceador real |
| Docker Compose end-to-end | VERIFICADO | Seis servicios saludables; `lb` publica 4200 y 18080 (API configurable con `API_PORT`); evidence/compose-config.txt |
| Balanceo Nginx de tres backends | VERIFICADO | evidence/balancing.json: 30/30 consultas, 10 por cada upstream, sin reintentos |
| Maven Wrapper 3.9.9 versionado | VERIFICADO | evidence/backend-summary-2026-10-30.txt; `./mvnw -v` usa Maven 3.9.9 y `./mvnw -B test` ejecuta 15 pruebas sin fallos con JDK 17 |
| Playwright dentro de Docker | VERIFICADO | Perfil `e2e` con `mcr.microsoft.com/playwright:v1.56.1-noble`; evidence/browser.log: 4 pruebas aprobadas, sin Node en el host |
| Pruebas UI de recarga, reintento y móvil | VERIFICADO | evidence/browser.log: persistencia tras recarga, error de conexión con reintento y 390 px sin scroll horizontal |
| Concurrencia de transición 200+409 en H2 | VERIFICADO | evidence/backend-summary-2026-10-30.txt; `ApiIntegrationTest.concurrentTransitionsAllowOneAndRejectTheOther` deja un solo seguimiento |
| Concurrencia contra PostgreSQL y tres backends | VERIFICADO | evidence/integration.json: dos PATCH simultáneos -> 200+409, un solo seguimiento persistido |
| Auditoría completa WCAG / dispositivos reales | NO_VERIFICADO | Hay etiquetas, nombres de selectores, foco, avisos y CSS responsive; no equivale a auditoría formal |

## Reproducción

Con Docker funcionando: `./scripts/verify-module.sh M04`. El verificador construye el backend, levanta los seis servicios, ejecuta `scripts/integration.py` (incluida la concurrencia), `scripts/test-balancing.py` y Playwright dentro del perfil `e2e`; el host no necesita Node ni navegadores. Si 8080 está ocupado, define `API_PORT=18080` en `.env`: Compose publica ese puerto y los scripts lo leen automáticamente. Alternativa sin Docker: seguir README con PostgreSQL y JDK 11, ejecutar `python3 scripts/integration.py`, `python3 scripts/test-balancing.py` y, si se dispone de Node 18, `npm run test:e2e` desde `FrontendP04` con `FRONTEND_URL` y `CHROMIUM_PATH` apuntando al navegador local. Para pruebas reales se requieren las cuentas del perfil demo y `.env` generado localmente.

Los tests escriben únicamente casos sintéticos y no borran datos existentes. La prueba de concurrencia usa dos PATCH simultáneos sobre la misma transición; el bloqueo optimista `@Version` debe devolver un `200` y un `409` sin duplicar el seguimiento.

## Interpretación

H2 prueba reglas, validación y seguridad en contexto Spring, pero no sustituye PostgreSQL. El recorrido HTTP con PostgreSQL añade verificación de migraciones, tipos y persistencia real. La prueba concurrente en H2 valida el bloqueo optimista dentro de Spring; `integration.py` confirmó el mismo comportamiento (200/409) contra PostgreSQL a través del balanceador. El navegador verifica el flujo, la recarga, el reintento, el layout móvil y el 401; no sustituye una auditoría formal de accesibilidad ni todos los tamaños de pantalla. La prueba HTTP completa las transiciones, filtros y evento ambiental.
