# Bitácora técnica · 9 de octubre de 2026 (America/Mexico_City)

Registro de asistencia técnica, separado de la contribución académica individual.

| Acción / versión / comando | Resultado e interpretación |
| --- | --- |
| Inspección PR07, P04, M04, Eminus y BackendP04 | Original Boot 3.5.7/Java 21 incompatible con el perfil exigido; no había frontend en la raíz |
| Adaptación Boot 2.7.18/Java 11 | DTO inmutable con Lombok y anotaciones Jackson conserva JSON y accesores; imports javax y API de seguridad compatibles; Flyway 9.22.3 para PostgreSQL 16 |
| Primeras instalaciones Maven/npm | NO_VERIFICADO inicialmente: sandbox de caché y luego ENOSPC. Se eliminaron solo temporales generados; instalación limpia posterior completada |
| Compilación Maven incremental | Se detectó clase residual versión 65 del backend previo; `mvn clean package` elimina contaminación. La reproducción exige compilación limpia |
| `mvn clean package`, Maven 3.9.9 / JDK 17 con --release 11 | VERIFICADO: 14 pruebas sin fallos y JAR ejecutable compatible con Java 11 |
| Docker Desktop | NO_VERIFICADO despliegue completo Compose: daemon no pudo arrancar. Se usó instancia PostgreSQL 16.14 aislada para probar persistencia real |
| PostgreSQL 16.14 + HTTP | VERIFICADO: migraciones, alta, detalle, evidencia, transiciones, historial, evento simulado y rechazos 400/401/403/409 |
| Primer Playwright | Fallo de selección accesible: se añadieron nombres explícitos a selectores. No se ocultó con datos simulados de API |
| Playwright con Chromium local | VERIFICADO tras corrección: 2 pruebas, flujo Angular/API/PostgreSQL y error 401 visible. Capturas guardadas |
| Angular 16.2.12 / Node 18.20.8 | VERIFICADO build production; aproximadamente 245 kB iniciales. Se corrigió aviso CSS de align-items |
| JRE Temurin 11.0.32.1 | VERIFICADO arranque real del JAR y recorrido HTTP; contrato exportado desde esa ejecución |

El repositorio se unifica en la raíz por solicitud del usuario; se retira el Git interno de BackendP04 y se incorpora el código como archivos normales. El commit de integración no acredita por sí solo la contribución académica individual. Pendiente: equipo/nombres, explicación individual y referencias personales a commits reales después de revisión.
