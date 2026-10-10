import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { firstValueFrom, forkJoin } from 'rxjs';
import { ApiService } from './api.service';
import { Auth, Catalogo, Detalle, Estado, Pagina, Prioridad, Reporte } from './models';
@Component({selector:'app-root',standalone:true,imports:[CommonModule,FormsModule],templateUrl:'./app.component.html'})
export class AppComponent {
 api=inject(ApiService); busy=false; error=''; notice=''; register=false; view='list';
 credentials={nombre:'',correoElectronico:'',password:''}; categorias:Catalogo[]=[];zonas:Catalogo[]=[];
 estados:Estado[]=['REGISTRADO','EN_REVISION','EN_ATENCION','RESUELTO','CERRADO'];prioridades:Prioridad[]=['BAJA','MEDIA','ALTA'];
 filters={estado:'',prioridad:'',categoriaId:'',zonaId:''};page:Pagina={content:[],page:0,size:10,totalElements:0,totalPages:0};detail:Detalle|null=null;
 draft={titulo:'',descripcion:'',categoriaId:0,zonaId:0,prioridad:'MEDIA' as Prioridad};
 change={estado:'' as Estado|'',comentario:''}; evidence={tipo:'IMAGEN',referenciaFicticia:'simulada://',descripcion:''};
 event={fuente:'SIMULADOR_AMBIENTAL_01',variable:'temperatura',unidad:'°C',valor:32.5,interpretacion:'Temperatura simulada; contextualiza la atención sin cambiar estado ni prioridad.'};
 label(s:string){return s.replace(/_/g,' ').toLowerCase()}
 async run(action:()=>Promise<void>){if(this.busy)return;this.busy=true;this.error='';this.notice='';try{await action()}catch(e:any){this.error=e.status===0?'No se pudo conectar con la API. Comprueba que el backend esté iniciado.':`${e.error?.message || 'No fue posible completar la operación'} (HTTP ${e.status ?? 'desconocido'})`}finally{this.busy=false}}
 async authenticate(){await this.run(async()=>{
  const body=this.register?this.credentials:{correoElectronico:this.credentials.correoElectronico,password:this.credentials.password};
  this.api.accept(await firstValueFrom(this.api.post<Auth>('/auth/'+(this.register?'register':'login'),body)));this.credentials.password='';
  await this.initialize();
 })}
 async initialize(){const [cats,zones]=await firstValueFrom(forkJoin([this.api.get<Catalogo[]>('/categorias'),this.api.get<Catalogo[]>('/zonas')]));this.categorias=cats;this.zonas=zones;await this.load(0)}
 async load(n=0){const q=new URLSearchParams({page:String(n),size:'10'});Object.entries(this.filters).forEach(([k,v])=>{if(v)q.set(k,v)});this.page=await firstValueFrom(this.api.get<Pagina>('/reportes?'+q));this.view='list'}
 refresh(n=0){return this.run(()=>this.load(n))}
 async open(id:number){this.detail=await firstValueFrom(this.api.get<Detalle>('/reportes/'+id));this.change={estado:'',comentario:''};this.view='detail'}
 select(id:number){return this.run(()=>this.open(id))}
 create(){return this.run(async()=>{const report=await firstValueFrom(this.api.post<Reporte>('/reportes',this.draft));this.draft={titulo:'',descripcion:'',categoriaId:0,zonaId:0,prioridad:'MEDIA'};await this.open(report.id);this.notice='Reporte registrado y persistido correctamente.'})}
 get transitions():Estado[]{if(!this.detail)return [];const s=this.detail.reporte.estado;if(this.api.role==='OPERADOR'){if(s==='REGISTRADO')return ['EN_REVISION'];if(s==='EN_REVISION')return ['REGISTRADO','EN_ATENCION']}if(this.api.role==='RESPONSABLE_ATENCION'){if(s==='EN_ATENCION')return ['EN_REVISION','RESUELTO'];if(s==='RESUELTO')return ['EN_ATENCION','CERRADO']}return []}
 update(){return this.run(async()=>{const id=this.detail!.reporte.id;await firstValueFrom(this.api.patch('/reportes/'+id+'/estado',this.change));await this.open(id);this.notice='Estado actualizado; seguimiento guardado.'})}
 addEvidence(){return this.run(async()=>{const id=this.detail!.reporte.id;await firstValueFrom(this.api.post('/reportes/'+id+'/evidencias',this.evidence));await this.open(id);this.evidence={tipo:'IMAGEN',referenciaFicticia:'simulada://',descripcion:''};this.notice='Evidencia ficticia registrada.'})}
 addEvent(){return this.run(async()=>{const id=this.detail!.reporte.id;await firstValueFrom(this.api.post('/eventos-contexto',{...this.event,reporteId:id}));await this.open(id);this.notice='Evento simulado registrado.'})}
 name(items:Catalogo[],id:number){return items.find(i=>i.id===id)?.nombre ?? String(id)}
 logout(){this.api.logout();this.detail=null;this.page={content:[],page:0,size:10,totalElements:0,totalPages:0};this.view='list';this.error='';this.notice=''}
}
