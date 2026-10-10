# P04_EQUIPO_NN · PR07 Reportes ciudadanos

Implementación de la arquitectura Web 3.0 de PR07: Angular 16.2.12, Spring Boot 2.7.18 con objetivo Java 11 y PostgreSQL 16. Incluye registro/login JWT, bandeja con filtros y paginación, alta, detalle, evidencias ficticias, transición autorizada con seguimiento y eventos ambientales simulados. P04 evalúa arquitectura y contratos; la integración ejecutable se proporciona como continuidad hacia P05.

## Ejecución reproducible con Docker

Requisitos: Docker Engine/Desktop con Compose v2, Python 3 y espacio para imágenes. No utiliza infraestructura externa de ejecución.

```bash
./scripts/setup-env.sh
docker compose up --build -d
```

Abre **http://localhost:4200**. API: http://localhost:8080; salud: `/actuator/health`; Swagger: `/swagger-ui/index.html`; contrato: `/v3/api-docs`.

El perfil local `demo` crea, si faltan, tres cuentas sintéticas:

| Rol | Correo |
| --- | --- |
| Ciudadano | ciudadano@example.test |
| Operador | operador@example.test |
| Responsable | responsable_atencion@example.test |

Su contraseña local aleatoria está en `DEMO_PASSWORD` dentro de `.env`. No se incorpora al frontend ni a la entrega. El registro público solo crea ciudadanos. Cambiar `.env` no cambia la contraseña de usuarios que ya existen; conserva los valores mientras reutilices el volumen. Para producción haría falta otro mecanismo de aprovisionamiento; el perfil demo es exclusivo del laboratorio.

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

El verificador construye imágenes (incluyendo pruebas Spring), espera la API, ejecuta positivos/negativos contra PostgreSQL, exporta OpenAPI y ejecuta Playwright con Chromium. Para la parte navegador requiere Node 18.20.8/npm en el host; `FrontendP04/.nvmrc` fija el perfil docente. Genera evidencia en `docs/evidence`. Las pruebas agregan datos sintéticos. La limpieza detiene exclusivamente el proyecto Compose `pr07-p04` y conserva volumen y evidencia. No elimina bases ajenas.

## Desarrollo sin contenedores

Requisitos: JDK 11, Maven 3.9.9, Node 18.20.8 y PostgreSQL 16. Crea una base propia y configura en la terminal `DB_URL` (JDBC), `DB_USERNAME`, `DB_PASSWORD` y `JWT_SECRET` (mínimo 32 bytes aleatorios). Para cuentas locales activa `SPRING_PROFILES_ACTIVE=demo` y `DEMO_PASSWORD` (12–72 bytes). Flyway prepara el esquema automáticamente.

```bash
cd BackendP04
mvn clean test
mvn spring-boot:run
```

En otra terminal:

```bash
cd FrontendP04
npm ci
npm start
```

El proxy de desarrollo envía `/api` a `localhost:8080`. En contenedores Nginx envía la misma ruta al servicio `backend`. No hay credenciales ni URL absoluta de API compiladas en Angular. `CORS_ORIGINS` permite por defecto `http://localhost:4200` para acceso directo.

## Entrega y límites

Consulta [el índice de R04](docs/P04_EQUIPO_NN_INDICE.md), [arquitectura](docs/P04_EQUIPO_NN_ARQUITECTURA.md), [modelo](docs/P04_EQUIPO_NN_MODELO.md), [contratos](docs/P04_EQUIPO_NN_CONTRATOS.md) y [verificación](docs/verification-report.md).

Antes de entregar, sustituye `EQUIPO_NN` y completa el registro individual con nombres, explicación propia y commits reales. No se inventa autoría ni confirmación institucional de PR07/F02. Usa solo datos sintéticos; no hay emergencias, denuncias reales, geolocalización exacta, carga de fotos reales ni servicios externos. Se conservan seis entidades de dominio y dos tablas auxiliares de autenticación. Las versiones docentes antiguas se mantienen por la actividad; una actualización requiere otra decisión y pruebas.

## Repositorio

Frontend y backend se versionan juntos desde esta raíz. El archivo `.gitignore` excluye secretos locales, dependencias, compilados, resultados temporales y los documentos docentes de consulta. Los contratos, diagramas y evidencias revisadas de `docs/` sí forman parte del repositorio.
