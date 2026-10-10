#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
: "${DB_URL:?Configura la URL JDBC de tu PostgreSQL local}"
: "${DB_USERNAME:?Configura el usuario de tu base local}"
: "${DB_PASSWORD:?Configura la contraseña en el entorno}"
: "${JWT_SECRET:?Configura una clave aleatoria de al menos 32 bytes}"
if [[ $# == 0 ]]; then set -- spring-boot:run; fi
exec mvn "$@"
