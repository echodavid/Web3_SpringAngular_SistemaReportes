# Backend P04 · PR07

Spring Boot **2.7.18**, objetivo **Java 11**, JPA, Spring Security JWT, BCrypt, Flyway y PostgreSQL 16. El backend original Java 21/Boot 3 fue adaptado para cumplir M04; no se conservan records ni imports Jakarta.

Desde la raíz de la entrega, sigue [README general](../README.md) para despliegue reproducible junto a Angular. No se requiere un contenedor preexistente ni infraestructura de otra práctica.

Para ejecución directa, configura `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET` y ejecuta `mvn clean test` seguido de `./scripts/run-local.sh`. El perfil `demo` y `DEMO_PASSWORD` habilitan cuentas sintéticas de los tres roles exclusivamente para el laboratorio; el registro público sigue creando solo ciudadanos.

Consulta [RUTAS_API.md](RUTAS_API.md) para contratos detallados y [verification-report.md](../docs/verification-report.md) para los resultados de esta entrega. Swagger está en `/swagger-ui/index.html`, OpenAPI en `/v3/api-docs`, salud en `/actuator/health`.
