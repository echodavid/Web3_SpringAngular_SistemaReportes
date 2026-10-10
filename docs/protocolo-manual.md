# Protocolo de navegador

URL: http://localhost:4200. Usar únicamente cuentas @example.test y la contraseña local de .env. Guardar capturas sin contraseñas ni tokens.

| Caso | Entrada / acción | Resultado esperado |
| --- | --- | --- |
| Positivo 1 | Ciudadano crea reporte sintético con categoría, zona y prioridad | Detalle con ID, REGISTRADO, datos correctos; GET conserva registro |
| Positivo 2 | Operador abre reporte y selecciona EN_REVISION con comentario | Estado actualizado y seguimiento visible tras volver a consultar |
| Negativo 1 | Login con cuenta inexistente | Mensaje HTTP 401; no se abre bandeja |
| Negativo 2 | Título vacío / catálogo no seleccionado | Botón de alta deshabilitado; API también rechaza entrada inválida |
| Negativo 3 | Detener API propia e intentar consultar | Error de conexión visible; botón deja de indicar carga |
| Flujo principal | Ciudadano registra y agrega evidencia; operador revisa/atiende; responsable resuelve/cierra | Historial de cuatro cambios persistido; CERRADO sin transiciones |
| Contexto | Responsable registra SIMULADOR_AMBIENTAL_01, temperatura, °C, 32.5 e interpretación | Fuente, instante servidor, variable, unidad, valor y explicación visibles |
| Filtros | Aplicar estado CERRADO y prioridad ALTA, cambiar página | Resultados del filtro; total y paginación coherentes; vacío informado |
| Accesibilidad | Tab/Shift+Tab/Enter desde login y formulario | Foco visible, etiquetas, controles utilizables; revisar contraste visual |

Registrar fecha, versiones, resultado real y limitaciones. Una captura sola no sustituye comandos ni explicación.
