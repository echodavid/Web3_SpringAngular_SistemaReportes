# Índice de evidencia · P04/P05 · PR07

| Criterio R04 | Puntos | Evidencia |
| --- | --- | --- |
| Diagrama de arquitectura | 3 | P04_DSW-E05_ARQUITECTURA.md: componentes, despliegue y secuencia |
| Justificación Angular/Spring | 2.5 | P04_DSW-E05_ARQUITECTURA.md: decisiones y alternativas |
| Modelo PostgreSQL | 2 | P04_DSW-E05_MODELO.md + migraciones V1/V2 del backend |
| Endpoints y contratos | 2.5 | P04_DSW-E05_CONTRATOS.md, openapi.json y BackendP04/RUTAS_API.md |
| Contribución individual | 2 | P04_APELLIDO_NOMBRE_INDIVIDUAL.md |

## Trazabilidad funcional

| Requisito | Componente / persistencia | Comprobación |
| --- | --- | --- |
| RF01 reporte | Formulario/detalle Angular → ReporteController/Service → reporte | Alta 201 y detalle 200 |
| RF02 categoría | Catálogo Angular → CategoriaService → categoria | Catálogo y FK; inexistente 404 |
| RF03 ubicación no precisa | Selector zona → ZonaService → zona | Zona textual general sin coordenadas |
| RF04 evidencia ficticia | Detalle → EvidenciaService → evidencia | POST 201 y referencia visible |
| RF05 estado | Badge/formulario → TransitionPolicy → reporte | Matriz válida y rechazo 409 |
| RF06 prioridad/seguimiento | Captura/filtros/timeline → ReporteService → seguimiento | Filtro ALTA y cuatro cambios persistidos |
| RF07 seis entidades relacionadas | JPA, Flyway, PostgreSQL | FK, migraciones e integración |
| RF08 consulta/cambio/errores | HttpClient, interceptor → REST | GET/PATCH desde navegador; error 401 visible |
| RF09 evento controlado | Formulario responsable → EventoContextoService | Fuente, instante, variable, unidad, valor e interpretación |

## Criterios de aceptación

CA01/03: scripts/integration.py crea y completa estados consultando lo persistido; CA02: vacíos 400, permiso 403 y salto 409 sin cambio; CA04: evento simulado persistido y visible; CA05: README y scripts de ejecución/limpieza; CA06: registro individual en P04_APELLIDO_NOMBRE_INDIVIDUAL.md. Estados concretos de ejecución en verification-report.md.

RNF01: versiones/lockfile/Compose/README. RNF02: JWT/BCrypt/validación y datos sintéticos. RNF03: etiquetas, foco, teclado y mensajes de estado. RNF04: FK/CHECK/transacciones. RNF05: este índice y bitácora. RNF06: despliegue local, sin nube ni servicios de pago.

## Continuidad P05

| Práctica P05 | Evidencia ejecutable |
| --- | --- |
| Positivos y negativos | `scripts/integration.py`: 201/200/400/401/403/409 y estados persistidos |
| PostgreSQL real | Migraciones Flyway y entidades consultadas por HTTP; sin mocks de base |
| Flujo Angular → API | `FrontendP04/e2e/reportes.spec.ts`: alta, evidencia, recarga, operador y 401 |
| Reintento y móvil | Pruebas Playwright de fallo de conexión/reintento y `scrollWidth` en 390 px |
| Concurrencia | Dos PATCH simultáneos: un 200 y un 409 por `@Version` |
| Reproducibilidad | Maven Wrapper 3.9.9, Compose con healthchecks y Playwright en contenedor |
| OpenAPI | `docs/openapi.json` y `docs/openapi-runtime.json` |
| Evidencia | `docs/evidence/` y `docs/verification-report.md` con estados y evidencia trazables |

No se cambian puntos, fechas, modalidades ni políticas institucionales de Eminus.
