# Estado de verificación · P04 / PR07

Fecha: **2026-10-09**, America/Mexico_City. Los resultados corresponden al código de esta entrega; no se reutilizan resultados del backend Java 21 original.

| Comprobación | Estado | Evidencia / alcance |
| --- | --- | --- |
| Compilación y paquete Spring Boot 2.7.18 con --release 11 | VERIFICADO | evidence/backend-summary.txt; Maven 3.9.9 ejecutado con JDK 17, objetivo de bytecode 11 |
| 14 pruebas automatizadas Spring/Mockito/política de transiciones | VERIFICADO | 12 integración MockMvc/H2, 1 servicio, 1 matriz de 75 combinaciones; cero fallos |
| Arranque del JAR en Java 11 | VERIFICADO | Temurin JRE 11.0.32.1, esquema PostgreSQL validado |
| Persistencia real con PostgreSQL 16.14 | VERIFICADO | evidence/integration-java11.json; seis entidades del dominio y seguimiento persistido |
| Positivos y negativos HTTP | VERIFICADO | 201 creación/evidencia/evento; 200 consulta/cambios; 400/401/403/409 controlados; salto inválido conserva estado |
| Build Angular 16.2.12 con Node 18.20.8 | VERIFICADO | evidence/frontend-build.txt; producción sin errores |
| Recorrido navegador Angular → API → PostgreSQL | VERIFICADO | evidence/browser-summary.txt y capturas ciudadano.png / operador.png; alta, evidencia y cambio de estado real |
| Error HTTP 401 visible en Angular | VERIFICADO | Segunda prueba Playwright; no usa mocks |
| OpenAPI exportado | VERIFICADO | openapi.json, obtenido de aplicación Java 11 real; esquemas y seguridad JWT |
| Docker Compose end-to-end | NO_VERIFICADO | Docker Desktop no pudo arrancar en el entorno; Dockerfiles/Compose están preparados, no se afirma que sus imágenes se hayan ejecutado |
| Auditoría completa WCAG / dispositivos reales | NO_VERIFICADO | Hay etiquetas, nombres de selectores, foco, avisos y CSS responsive; no equivale a auditoría formal |
| Confirmación F02, equipo, explicación y commits individuales | PENDIENTE | Completar plantilla individual con evidencia propia |

## Reproducción

Con Docker funcionando: `./scripts/verify-module.sh M04`. Alternativa: seguir README con PostgreSQL y herramientas locales, ejecutar `python3 scripts/integration.py` y desde FrontendP04 `npm run test:e2e`. Para pruebas reales se requieren las cuentas del perfil demo y `.env` generado localmente.

Se usó Chromium ya instalado mediante `CHROMIUM_PATH`; en entorno limpio `npx playwright install chromium` instala la versión correspondiente al lockfile. Los tests escriben únicamente casos sintéticos y no borran datos existentes.

## Interpretación

H2 prueba reglas, validación y seguridad en contexto Spring, pero no sustituye PostgreSQL. El recorrido HTTP con PostgreSQL añade verificación de migraciones, tipos y persistencia real. El navegador verifica la integración y mensajes, sin demostrar todos los filtros ni todos los tamaños de pantalla. La prueba HTTP completa las cuatro transiciones, filtros y evento ambiental. La evidencia individual requiere revisión y explicación humana; permanece pendiente.

## Revisión previa a publicación

El 2026-10-09 se repitieron `mvn clean test` (14 pruebas sin fallos), build Angular con Node 18.20.8, recorrido HTTP (20 comprobaciones) y Playwright (2 pruebas sin fallos). Se unificó Git en la raíz y se revisaron los archivos a publicar para excluir secretos, cachés, compilados y material docente. Docker y contribución académica individual conservan los estados declarados arriba.
