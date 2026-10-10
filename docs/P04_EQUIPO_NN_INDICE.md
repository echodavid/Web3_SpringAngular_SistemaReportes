# Índice de evidencia · P04 / M04 / R04 · PR07

| Criterio R04 | Puntos | Evidencia |
| --- | --- | --- |
| Diagrama de arquitectura | 3 | P04_EQUIPO_NN_ARQUITECTURA.md: componentes, despliegue y secuencia |
| Justificación Angular/Spring | 2.5 | P04_EQUIPO_NN_ARQUITECTURA.md: decisiones y alternativas |
| Modelo PostgreSQL | 2 | P04_EQUIPO_NN_MODELO.md + migraciones V1/V2 del backend |
| Endpoints y contratos | 2.5 | P04_EQUIPO_NN_CONTRATOS.md, openapi.json y BackendP04/RUTAS_API.md |
| Contribución individual | 2 | P04_APELLIDO_NOMBRE_INDIVIDUAL.md, a completar con explicación y commits reales |

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

CA01/03: scripts/integration.py crea y completa estados consultando lo persistido; CA02: vacíos 400, permiso 403 y salto 409 sin cambio; CA04: evento simulado persistido y visible; CA05: README y scripts de ejecución/limpieza; CA06: registro individual pendiente de completar por cada integrante. Estados concretos de ejecución en verification-report.md.

RNF01: versiones/lockfile/Compose/README. RNF02: JWT/BCrypt/validación y datos sintéticos. RNF03: etiquetas, foco, teclado y mensajes de estado. RNF04: FK/CHECK/transacciones. RNF05: este índice y bitácora; commits reales pendientes del equipo. RNF06: despliegue local, sin nube ni servicios de pago.

No se cambian puntos, fechas, modalidades ni políticas institucionales de Eminus. La plantilla de contribución no constituye evidencia de autoría completada.
