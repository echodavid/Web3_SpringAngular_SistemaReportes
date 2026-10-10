#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
docker compose down --remove-orphans
printf 'Servicios de pr07-p04 detenidos. Se conserva el volumen PostgreSQL y la evidencia.\n'
