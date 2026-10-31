#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
if [[ -e .env ]]; then echo '.env ya existe; se conserva.'; exit 0; fi
umask 077
python3 - <<'PY'
import secrets
from pathlib import Path
Path('.env').write_text('DB_PASSWORD='+secrets.token_hex(24)+'\nJWT_SECRET='+secrets.token_hex(48)+'\nDEMO_PASSWORD='+secrets.token_hex(16)+'\nAPI_PORT=8080\n')
PY
echo 'Configuración local creada. Consulta DEMO_PASSWORD en .env para iniciar sesión.'
