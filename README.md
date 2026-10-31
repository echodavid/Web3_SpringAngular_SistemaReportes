# P04_DSW-E05 · PR07 Reportes ciudadanos

Implementación de la arquitectura Web 3.0 de PR07: Angular 16.2.12, Spring Boot 2.7.18 con objetivo Java 11 y PostgreSQL 16, con tres instancias del backend balanceadas por Nginx. Incluye registro/login JWT, bandeja con filtros y paginación, alta, detalle, evidencias ficticias, transición autorizada con seguimiento y eventos ambientales simulados. P04 evalúa arquitectura y contratos; P05 verifica la integración ejecutable con pruebas positivas/negativas, persistencia real en PostgreSQL, recorrido Angular → API, OpenAPI y pruebas de navegador/concurrencia.

## Ejecución reproducible con Docker

Requisitos: Docker Engine/Desktop con Compose v2, Python 3 y espacio para imágenes. La aplicación, Maven y Playwright se ejecutan en contenedores; no necesitas Java, Maven ni Node en el host. No utiliza infraestructura externa de ejecución.

```bash
./scripts/setup-env.sh
docker compose up --build -d --wait
```

Abre **http://localhost:4200**. API: http://localhost:8080; salud: `/actuator/health`; Swagger: `/swagger-ui/index.html`; contrato: `/v3/api-docs`. El balanceador Nginx publica ambos puertos en loopback y reparte `/api/`, la salud y la documentación entre `backend1`, `backend2` y `backend3`. Si el puerto 8080 ya está ocupado, define `API_PORT=18080` en `.env` antes de levantar; `scripts/integration.py` y `scripts/test-balancing.py` leen ese valor y el frontend sigue en 4200.

### Balanceo de carga

El servicio `lb` es un Nginx separado. Sirve el frontend estático desde `frontend` y envía las rutas de API a un `upstream` con las tres IP fijas de los backends. Las tres instancias comparten PostgreSQL y el mismo `JWT_SECRET`; como el token vive solo en memoria y es stateless, no hacen falta sesiones pegajosas.

```bash
docker compose ps
docker compose logs -f lb
python3 scripts/test-balancing.py
```

`scripts/test-balancing.py` envía 30 consultas y comprueba en los registros de Nginx que las tres IP hayan respondido. El balanceo es round-robin y el failover es pasivo (`max_fails`/`fail_timeout`); no se reintentan POST ni PATCH. `backend1` arranca primero para aplicar Flyway y el seed demo; `backend2` y `backend3` esperan su salud y luego se incorporan al balanceo.

La red usa `172.28.90.0/24`. Si esa subred choca con otra red de tu equipo, cambia la subred y las IP en `compose.yaml`, `nginx/default.conf` y `scripts/test-balancing.py`.

El perfil local `demo` crea, si faltan, tres cuentas sintéticas:

| Rol | Correo |
| --- | --- |
| Ciudadano | ciudadano@example.test |
| Operador | operador@example.test |
| Responsable | responsable_atencion@example.test |

Su contraseña local aleatoria está en `DEMO_PASSWORD` dentro de `.env`. No se incorpora al frontend ni a la entrega. El registro público solo crea ciudadanos. Cambiar `.env` no cambia la contraseña de usuarios que ya existen; conserva los valores mientras reutilices el volumen. El seed demo es idempotente: si las tres instancias arrancan a la vez, la restricción única decide cuál inserta y las demás continúan. Para producción haría falta otro mecanismo de aprovisionamiento; el perfil demo es exclusivo del laboratorio.

1. Entra como ciudadano y crea un reporte con categoría, zona general y prioridad.
2. Abre el detalle y añade una referencia `simulada://...` de evidencia.
3. Entra como operador: cambia `REGISTRADO → EN_REVISION → EN_ATENCION` con comentarios.
4. Entra como responsable: añade un evento simulado y cambia `EN_ATENCION → RESUELTO → CERRADO`.
5. Vuelve como ciudadano: el detalle conserva evidencias, estados e historial. Recargar el navegador exige login, porque el JWT se mantiene únicamente en memoria.

## Verificación y limpieza

```bash
./scripts/verify-module.sh M04
./scripts/cleanup.sh
```

El verificador construye las imágenes de los tres backends (incluyendo pruebas Spring), levanta el conjunto con `--wait`, ejecuta positivos/negativos contra PostgreSQL, comprueba el reparto entre las tres instancias, exporta OpenAPI y ejecuta Playwright con Chromium dentro del perfil `e2e`. El host no necesita Node ni navegadores: la imagen `mcr.microsoft.com/playwright:v1.56.1-noble` incluye Chromium y las dependencias del sistema. Genera evidencia en `docs/evidence`. Las pruebas agregan datos sintéticos. La limpieza detiene exclusivamente el proyecto Compose `pr07-p04` y conserva volumen y evidencia. No elimina bases ajenas.

**Estado 2026-10-30:** verificado con Docker real. Pasaron 15 pruebas Spring, 20 comprobaciones HTTP/PostgreSQL, 30/30 consultas balanceadas (10 por backend) y 4/4 pruebas Playwright. Como 8080 estaba ocupado por otro proyecto, la API se publicó en **18080** con `API_PORT=18080`; el frontend permaneció en **4200**.

Las pruebas de navegador cubren el flujo completo, la persistencia del reporte y su evidencia después de recargar, el error de conexión con reintento, la ausencia de desbordamiento horizontal en móvil y el error 401. `scripts/integration.py` agrega una prueba de concurrencia: dos `PATCH /api/v1/reportes/{id}/estado` simultáneos deben producir un `200` y un `409`, sin duplicar el seguimiento. Para ejecutar solo Playwright con el conjunto ya levantado:

```bash
docker compose --profile e2e run --build --rm e2e
```

## Desarrollo sin contenedores

Requisitos: JDK 11, Node 18.20.8 y PostgreSQL 16. Maven no es necesario en el host: `BackendP04/mvnw` descarga Maven 3.9.9 desde el wrapper versionado. Crea una base propia y configura en la terminal `DB_URL` (JDBC), `DB_USERNAME`, `DB_PASSWORD` y `JWT_SECRET` (mínimo 32 bytes aleatorios). Para cuentas locales activa `SPRING_PROFILES_ACTIVE=demo` y `DEMO_PASSWORD` (12–72 bytes). Flyway prepara el esquema automáticamente.

```bash
cd BackendP04
./mvnw clean test
./mvnw spring-boot:run
```

En otra terminal:

```bash
cd FrontendP04
npm ci
npm start
```

El proxy de desarrollo envía `/api` a `localhost:8080`. En contenedores, el Nginx balanceador recibe `/api` y reparte la petición entre `backend1`, `backend2` y `backend3`; el frontend solo entrega archivos estáticos. No hay credenciales ni URL absoluta de API compiladas en Angular. `CORS_ORIGINS` permite por defecto `http://localhost:4200` para acceso directo.
