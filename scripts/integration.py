"""Recorrido real HTTP/PostgreSQL. No imprime tokens ni contraseñas."""
import json, os, uuid, urllib.request, urllib.error
from concurrent.futures import ThreadPoolExecutor
from pathlib import Path
from threading import Barrier

env = dict(line.split('=', 1) for line in Path('.env').read_text().splitlines() if line and not line.startswith('#'))
base = os.environ.get('API_URL', 'http://localhost:' + env.get('API_PORT', '8080'))
checks = []


def call(method, path, body=None, token=None):
    """Ejecuta una petición y devuelve (status, json) sin lanzar por errores HTTP."""
    headers = {'Content-Type': 'application/json'}
    if token:
        headers['Authorization'] = 'Bearer ' + token
    request = urllib.request.Request(
        base + path,
        data=json.dumps(body).encode() if body is not None else None,
        method=method,
        headers=headers,
    )
    try:
        with urllib.request.urlopen(request, timeout=15) as response:
            return response.status, json.load(response)
    except urllib.error.HTTPError as error:
        return error.code, json.load(error)


def req(method, path, body=None, token=None, expected=200):
    status, data = call(method, path, body, token)
    assert status == expected, (method, path, status, data)
    checks.append({'method': method, 'path': path, 'status': status, 'result': 'VERIFICADO'})
    return data

def login(role):
 return req('POST','/api/v1/auth/login',{'correoElectronico':role+'@example.test','password':env['DEMO_PASSWORD']})['accessToken']
assert req('GET','/actuator/health')['status']=='UP'
req('GET','/api/v1/reportes',expected=401)
c=login('ciudadano');o=login('operador');r=login('responsable_atencion')
cats=req('GET','/api/v1/categorias',token=c);zones=req('GET','/api/v1/zonas',token=c)
body={'categoriaId':cats[0]['id'],'zonaId':zones[0]['id'],'titulo':'Incidencia sintética '+uuid.uuid4().hex[:6],'descripcion':'Lámpara ficticia en una zona general. Sin personas ni dirección exacta.','prioridad':'ALTA'}
req('POST','/api/v1/reportes',{**body,'titulo':''},c,400)
report=req('POST','/api/v1/reportes',body,c,201);rid=report['id'];path='/api/v1/reportes/'+str(rid)
req('POST',path+'/evidencias',{'tipo':'IMAGEN','referenciaFicticia':'simulada://pr07/luminaria','descripcion':'Ilustración ficticia sin archivo'},c,201)
req('PATCH',path+'/estado',{'estado':'EN_REVISION','comentario':'Intento no autorizado'},c,403)
req('PATCH',path+'/estado',{'estado':'CERRADO','comentario':'Salto inválido'},o,409)
assert req('GET',path,token=c)['reporte']['estado']=='REGISTRADO'

# Dos operadores intentan la misma transición a la vez. El bloqueo optimista de @Version
# deja ganar una petición; la otra recibe 409 y no crea un seguimiento duplicado.
barrier = Barrier(2)
def transition_once():
    barrier.wait(timeout=10)
    return call('PATCH', path + '/estado', {'estado': 'EN_REVISION', 'comentario': 'Cambio concurrente'}, o)[0]
with ThreadPoolExecutor(max_workers=2) as pool:
    futures = [pool.submit(transition_once) for _ in range(2)]
    concurrent = sorted(future.result() for future in futures)
assert concurrent == [200, 409], concurrent
checks.append({'method': 'PATCH', 'path': path + '/estado (2 concurrentes)', 'status': '200+409', 'result': 'VERIFICADO'})

for state,token in [('EN_ATENCION',o),('RESUELTO',r),('CERRADO',r)]:
 req('PATCH',path+'/estado',{'estado':state,'comentario':'Paso de demostración controlada'},token)
req('POST','/api/v1/eventos-contexto',{'reporteId':rid,'fuente':'SIMULADOR_AMBIENTAL_01','variable':'temperatura','unidad':'°C','valor':32.5,'interpretacion':'Medición ficticia que contextualiza la atención.'},r,201)
d=req('GET',path,token=c)
assert d['reporte']['estado']=='CERRADO' and len(d['seguimientos'])==4 and len(d['evidencias'])==1
assert d['eventosContexto'][0]['fuente']=='SIMULADOR_AMBIENTAL_01' and d['eventosContexto'][0]['instante']
filtered=req('GET','/api/v1/reportes?estado=CERRADO&prioridad=ALTA',token=c)
assert filtered['totalElements']>=1
print(json.dumps({'checks':checks,'reportId':rid,'result':'VERIFICADO','note':'Se conserva el reporte sintético y sus relaciones como evidencia.'},ensure_ascii=False,indent=2))
