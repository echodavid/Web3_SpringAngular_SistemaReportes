# Bitácora técnica · 9 de octubre de 2026 (America/Mexico_City)

Registro de asistencia técnica, separado de la contribución académica individual.

| Acción / versión / comando | Resultado e interpretación |
| --- | --- |
| Inspección PR07, P04, M04, Eminus y BackendP04 | Original Boot 3.5.7/Java 21 incompatible con el perfil exigido; no había frontend en la raíz |
| Adaptación Boot 2.7.18/Java 11 | DTO inmutable con Lombok y anotaciones Jackson conserva JSON y accesores; imports javax y API de seguridad compatibles; Flyway 9.22.3 para PostgreSQL 16 |
| Primeras instalaciones Maven/npm | NO_VERIFICADO inicialmente: sandbox de caché y luego ENOSPC. Se eliminaron solo temporales generados; instalación limpia posterior completada |
| Compilación Maven incremental | Se detectó clase residual versión 65 del backend previo; `mvn clean package` elimina contaminación. La reproducción exige compilación limpia |
| `mvn clean package`, Maven 3.9.9 / JDK 17 con --release 11 | VERIFICADO: 14 pruebas sin fallos y JAR ejecutable compatible con Java 11 |
| Docker Desktop | NO_VERIFICADO inicialmente: daemon no arrancó y se usó PostgreSQL 16.14 aislado. El 30-oct-2026 el daemon quedó disponible y se verificó el stack Compose completo |
| Stack Compose completo (30-oct-2026) | VERIFICADO: db, backend1/2/3, frontend y lb saludables. 8080 estaba ocupado por otro proyecto, así que se publicó la API en 18080 mediante `API_PORT`; el frontend quedó en 4200 |
| Balanceador Nginx y tres backends (29-oct-2026) | Implementado en `compose.yaml` y `nginx/default.conf`: tres instancias, healthchecks, seed demo idempotente y prueba `scripts/test-balancing.py`. VERIFICADO el 30-oct-2026: 30/30 consultas, 10 por cada upstream |
| Maven Wrapper (30-oct-2026) | VERIFICADO `./mvnw -v`: wrapper 3.3.4 tipo only-script descarga y ejecuta Maven 3.9.9; `./mvnw -B test` ejecuta 15 pruebas sin fallos |
| Playwright en Docker (30-oct-2026) | VERIFICADO: perfil `e2e` con `mcr.microsoft.com/playwright:v1.56.1-noble`; 4/4 pruebas aprobadas, sin Node en el host. UID del contenedor ajustado al host para escribir evidencias |
| Pruebas UI ampliadas (30-oct-2026) | Añadidas recarga con persistencia, error de conexión con reintento y desbordamiento horizontal móvil, además del flujo principal y 401. VERIFICADO: 4/4 Playwright en Docker (`evidence/browser.log`) |
| Layout móvil y reintento con API simulada (30-oct-2026) | VERIFICADO a nivel de interfaz con Chromium local: login, lista y detalle a 390 px sin scroll horizontal; el reintento oculta la alerta. Evidencia `evidence/ui-mock-layout.txt`; la prueba E2E real también pasó |
| Concurrencia de transición (30-oct-2026) | VERIFICADO en H2 y en PostgreSQL: dos PATCH simultáneos producen 200/409 y dejan un solo seguimiento. Evidencia `evidence/integration.json` |
| PostgreSQL 16.14 + HTTP | VERIFICADO: migraciones, alta, detalle, evidencia, transiciones, historial, evento simulado y rechazos 400/401/403/409 |
| Primer Playwright | Fallo de selección accesible: se añadieron nombres explícitos a selectores. No se ocultó con datos simulados de API |
| Playwright con Chromium local | VERIFICADO tras corrección: 2 pruebas, flujo Angular/API/PostgreSQL y error 401 visible. Capturas guardadas |
| Angular 16.2.12 / Node 18.20.8 | VERIFICADO build production; aproximadamente 245 kB iniciales. Se corrigió aviso CSS de align-items |
| JRE Temurin 11.0.32.1 | VERIFICADO arranque real del JAR y recorrido HTTP; contrato exportado desde esa ejecución |
| Verificación de un solo comando (30-oct-2026) | VERIFICADO `./scripts/verify-module.sh M04` de principio a fin: compose válido, stack saludable, 20 comprobaciones HTTP/PostgreSQL, 30/30 balanceadas y 4/4 Playwright; evidencias regeneradas a las 19:25 |

El repositorio se unifica en la raíz por solicitud del usuario; se retira el Git interno de BackendP04 y se incorpora el código como archivos normales. El commit de integración no acredita por sí solo la contribución académica individual.
