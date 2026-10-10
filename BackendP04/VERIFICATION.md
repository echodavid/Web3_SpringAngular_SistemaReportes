# Verificación P04

La verificación actual está centralizada en [docs/verification-report.md](../docs/verification-report.md). El backend se adaptó a Spring Boot 2.7.18 y objetivo Java 11; los resultados anteriores con Java 21/Boot 3 no prueban esta versión.

Pruebas unitarias/integración H2: `mvn clean test`. Recorrido real con PostgreSQL y navegador: desde la raíz `./scripts/verify-module.sh M04`.
