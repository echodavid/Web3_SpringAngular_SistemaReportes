#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
[[ ${1:-M04} == M04 ]] || { echo 'Módulo soportado: M04'; exit 2; }
mkdir -p docs/evidence
set -o pipefail
docker compose config --quiet
docker compose build 2>&1 | tee docs/evidence/build.log
docker compose up -d
for ((i=0;i<90;i++)); do
  if curl -fsS http://localhost:8080/actuator/health > /dev/null; then break; fi
  sleep 2
done
python3 scripts/integration.py | tee docs/evidence/integration.json
curl -fsS http://localhost:8080/v3/api-docs > docs/openapi-runtime.json
cd FrontendP04
npm ci --no-audit --no-fund
npx playwright install chromium
npm run test:e2e 2>&1 | tee ../docs/evidence/browser.log
