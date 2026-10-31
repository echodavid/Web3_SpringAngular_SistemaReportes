#!/usr/bin/env python3
"""Comprueba que el balanceador reparta 30 consultas entre los tres backends."""
import collections
import json
import os
import subprocess
import time
import urllib.request
import uuid
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
env = dict(line.split('=', 1) for line in (ROOT / '.env').read_text().splitlines() if line and not line.startswith('#'))
BASE = os.environ.get('BASE_URL', 'http://localhost:' + env.get('API_PORT', '8080')).rstrip('/')
EXPECTED = {'172.28.90.21:8080', '172.28.90.22:8080', '172.28.90.23:8080'}
marker = 'balanceo=' + uuid.uuid4().hex

for _ in range(30):
    with urllib.request.urlopen(f'{BASE}/actuator/health?{marker}', timeout=15) as response:
        assert response.status == 200

# Espera breve para que el recolector de logs de Docker reciba las últimas líneas.
deadline = time.monotonic() + 5
while True:
    logs = subprocess.check_output(
        ['docker', 'compose', 'logs', '--no-color', '--no-log-prefix', '--since', '5m', 'lb'],
        cwd=ROOT,
        text=True,
    )
    records = []
    for line in logs.splitlines():
        if marker in line and line.startswith('{'):
            records.append(json.loads(line))
    if len(records) >= 30 or time.monotonic() >= deadline:
        break
    time.sleep(0.2)

assert len(records) == 30, f'Se esperaban 30 registros; se encontraron {len(records)}.'
assert all(record['status'] == 200 for record in records), records
assert all(record['upstream_status'] == '200' for record in records), 'Hubo reintentos o errores en un backend.'
counts = collections.Counter(record['upstream'] for record in records)
assert set(counts) == EXPECTED, f'No participaron las tres instancias: {dict(counts)}'

result = {
    'checks': len(records),
    'upstreams': dict(sorted(counts.items())),
    'result': 'VERIFICADO',
}
print(json.dumps(result, ensure_ascii=False, indent=2))
