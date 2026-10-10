"""Empaqueta fuentes y evidencia, excluyendo secretos, cachés e historial Git."""
from pathlib import Path
from zipfile import ZipFile, ZIP_DEFLATED
root=Path(__file__).resolve().parent.parent
output=root/'P04_EQUIPO_NN_PR07.zip'
exclude={'.git','node_modules','target','dist','.angular','test-results','playwright-report','__pycache__','out-tsc'}
include=['README.md','.gitignore','.env.example','compose.yaml','BackendP04','FrontendP04','docs','scripts']
with ZipFile(output,'w',ZIP_DEFLATED) as archive:
 for name in include:
  source=root/name
  files=[source] if source.is_file() else sorted(source.rglob('*'))
  for path in files:
   relative=path.relative_to(root)
   if path.is_file() and not any(p in exclude for p in relative.parts) and path.name not in {'.env','.DS_Store'} and path.suffix!='.pyc':
    archive.write(path,Path('P04_EQUIPO_NN_PR07')/relative)
print(output.name)
