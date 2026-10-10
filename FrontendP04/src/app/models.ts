export type Estado = 'REGISTRADO'|'EN_REVISION'|'EN_ATENCION'|'RESUELTO'|'CERRADO';
export type Prioridad = 'BAJA'|'MEDIA'|'ALTA';
export interface Catalogo {id:number; nombre:string; descripcion?:string; referencia?:string}
export interface Resumen {id:number; titulo:string; estado:Estado; prioridad:Prioridad; fechaCreacion:string}
export interface Reporte extends Resumen {categoriaId:number; zonaId:number; descripcion:string; fechaActualizacion:string}
export interface Pagina {content:Resumen[];page:number;size:number;totalElements:number;totalPages:number}
export interface Detalle {reporte:Reporte;evidencias:{id:number;tipo:string;referenciaFicticia:string;descripcion:string}[];seguimientos:{id:number;estadoAnterior:Estado;estadoNuevo:Estado;comentario:string;actorRol:string;fecha:string}[];eventosContexto:{id:number;fuente:string;instante:string;variable:string;unidad:string;valor:number;interpretacion:string}[]}
export interface Auth {accessToken:string;tokenType:string;expiresIn:number}
