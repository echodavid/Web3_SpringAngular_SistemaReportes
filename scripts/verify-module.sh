#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
[[ ${1:-M04} == M04 ]] || { echo 'Módulo soportado: M04'; exit 2; }
mkdir -p docs/evidence
set -o pipefail
if [[ ! -f .env ]]; then ./scripts/setup-env.sh; fi
API_PORT="$(sed -n 's/^API_PORT=//p' .env | tail -1)"
API_PORT="${API_PORT:-8080}"
docker compose config --quiet
docker compose --profile e2e build 2>&1 | tee docs/evidence/build.log
docker compose up -d --wait --wait-timeout 240
python3 scripts/integration.py | tee docs/evidence/integration.json
python3 scripts/test-balancing.py | tee docs/evidence/balancing.json
curl -fsS "http://localhost:${API_PORT}/v3/api-docs" > docs/openapi-runtime.json

# Playwright y Chromium se ejecutan dentro del perfil e2e; el host no necesita Node.
docker compose --profile e2e run --rm e2e 2>&1 | tee docs/evidence/browser.log
