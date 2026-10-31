# Catálogo HTTP/JSON

Contrato OpenAPI exportable: `GET http://localhost:8080/v3/api-docs` (o `http://localhost:${API_PORT}/v3/api-docs` si se define `API_PORT` en `.env`); copia de entrega `docs/openapi.json`. Documentación detallada con campos, entradas y respuestas: [RUTAS_API](../BackendP04/RUTAS_API.md). Base `/api/v1`; Content-Type `application/json`; Bearer JWT salvo registro y login. Salud pública `/actuator/health` responde `{"status":"UP"}` cuando la base está disponible.

| Método / ruta relativa | Entrada | Salida exitosa | Acceso |
| --- | --- | --- | --- |
| POST /auth/register | nombre, correoElectronico, password | 201 AuthResponse | Público; rol fijo ciudadano |
| POST /auth/login | correoElectronico, password | 200 AuthResponse | Público |
| GET /categorias | — | 200 CategoriaResponse[] | Autenticado |
| GET /categorias/{id} | id | 200 CategoriaResponse | Autenticado |
| GET /zonas | — | 200 ZonaResponse[] | Autenticado |
| GET /zonas/{id} | id | 200 ZonaResponse | Autenticado |
| POST /reportes | categoriaId, zonaId, titulo, descripcion, prioridad | 201 ReporteResponse + Location | Ciudadano |
| GET /reportes | page, size, sort, estado, prioridad, categoriaId, zonaId | 200 PageResponse | Ciudadano: propios; personal: todos |
| GET /reportes/mis-reportes | page, size | 200 PageResponse | Propios |
| GET /reportes/{id} | id | 200 ReporteDetailResponse | Propietario / personal |
| PATCH /reportes/{id}/estado | estado, comentario | 200 ReporteResponse | Personal según transición |
| GET /reportes/{id}/evidencias | id | 200 EvidenciaResponse[] | Propietario / personal |
| POST /reportes/{id}/evidencias | tipo, referenciaFicticia, descripcion opcional | 201 EvidenciaResponse + Location | Propietario ciudadano |
| GET /reportes/{id}/seguimientos | id | 200 SeguimientoResponse[] | Propietario / personal |
| POST /eventos-contexto | reporteId, fuente, variable, unidad, valor, interpretacion opcional | 201 EventoContextoResponse + Location | Responsable |
| GET /reportes/{id}/eventos-contexto | id | 200 EventoContextoResponse[] | Propietario / personal |

## Ejemplos del flujo

POST `/reportes`:
```json
{"categoriaId":1,"zonaId":1,"titulo":"Luminaria ficticia apagada","descripcion":"Incidencia sintética en zona general","prioridad":"MEDIA"}
```
201 (fechas e ID ilustrativos):
```json
{"id":12,"categoriaId":1,"zonaId":1,"titulo":"Luminaria ficticia apagada","descripcion":"Incidencia sintética en zona general","estado":"REGISTRADO","prioridad":"MEDIA","fechaCreacion":"2026-10-09T12:00:00","fechaActualizacion":"2026-10-09T12:00:00"}
```
GET `/reportes?page=0&size=10`:
```json
{"content":[{"id":12,"titulo":"Luminaria ficticia apagada","estado":"REGISTRADO","prioridad":"MEDIA","fechaCreacion":"2026-10-09T12:00:00"}],"page":0,"size":10,"totalElements":1,"totalPages":1}
```
PATCH `/reportes/12/estado` como operador:
```json
{"estado":"EN_REVISION","comentario":"Se revisa el caso sintético"}
```
Devuelve ReporteResponse actualizado. GET detalle entrega `reporte`, `evidencias`, `seguimientos`, `eventosContexto`. Las colecciones vacías se devuelven como `[]`.

## Validaciones y errores

Título obligatorio ≤200; descripción ≤4000; categoría/zona positivas existentes activas; prioridad BAJA/MEDIA/ALTA. Comentario obligatorio ≤2000. Paginación page≥0 y size 1–100. Registro contraseña 12–72 caracteres y ≤72 bytes UTF-8. Campos JSON desconocidos se rechazan. No se puede enviar propietario, estado inicial ni instante de evento.

| HTTP | Significado / ejemplo |
| --- | --- |
| 400 | Vacío, enum desconocido, tipo incorrecto, size=101 o campo no permitido |
| 401 | Token ausente, expirado o inválido; login inválido |
| 403 | Ciudadano intenta cambiar estado, consulta ajena o transición reservada a otro rol |
| 404 | Reporte o catálogo inexistente |
| 409 | Correo duplicado, catálogo inactivo, salto de estado o conflicto de versión |
| 500 | Error inesperado con mensaje genérico, sin stacktrace público |

Formato común:
```json
{"code":"INVALID_INPUT","message":"Los datos enviados no son válidos","timestamp":"2026-10-09T12:00:00","path":"/api/v1/reportes"}
```

## Estados autorizados

Operador: REGISTRADO→EN_REVISION; EN_REVISION→REGISTRADO/EN_ATENCION. Responsable: EN_ATENCION→EN_REVISION/RESUELTO; RESUELTO→EN_ATENCION/CERRADO. CERRADO es terminal. El servidor rechaza repetir el mismo estado y saltos. El historial registra anterior, nuevo, comentario, actorRol y fecha; no publica identidad personal del actor.
