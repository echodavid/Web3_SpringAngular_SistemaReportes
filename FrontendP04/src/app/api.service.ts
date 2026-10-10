import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';
import { Auth } from './models';
@Injectable({providedIn:'root'})
export class ApiService {
  private http=inject(HttpClient);
  token=''; role='';
  get<T>(path:string){return this.http.get<T>('/api/v1'+path)}
  post<T>(path:string,body:unknown){return this.http.post<T>('/api/v1'+path,body)}
  patch<T>(path:string,body:unknown){return this.http.patch<T>('/api/v1'+path,body)}
  accept(auth:Auth){
    const payload=JSON.parse(atob(auth.accessToken.split('.')[1].replace(/-/g,'+').replace(/_/g,'/')));
    this.token=auth.accessToken;this.role=payload.roles[0];
  }
  logout(){this.token='';this.role=''}
}
export const authInterceptor:HttpInterceptorFn=(req,next)=>{
  const api=inject(ApiService);
  const local=req.url.startsWith('/api/v1');
  return next(local && api.token ? req.clone({setHeaders:{Authorization:'Bearer '+api.token}}):req).pipe(catchError((error:HttpErrorResponse)=>{
    if(local && error.status===401 && !req.url.includes('/auth/')) api.logout();
    return throwError(()=>error);
  }));
};
