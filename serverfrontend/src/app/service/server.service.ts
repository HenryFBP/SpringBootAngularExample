import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';
import { CustomResponse } from '../interface/custom-response';
import { Server } from '../interface/server';

@Injectable({
  providedIn: 'root',
})
export class ServerService {
  private readonly apiURL = 'http://localhost:8080';

  constructor(private httpc: HttpClient) {}

  //procedural approach
  getServersProcedural(): Observable<CustomResponse> {
    return this.httpc.get<CustomResponse>(`${this.apiURL}/server/list`);
  }

  //reactive approach
  servers$ = <Observable<CustomResponse>>(
    this.httpc
      .get<CustomResponse>(`${this.apiURL}/server/list`)
      .pipe(tap(console.log), catchError(this.handleError))
  );

  save$ = (server: Server) =>
    <Observable<CustomResponse>>(
      this.httpc
        .post<CustomResponse>(`${this.apiURL}/server/save`, server)
        .pipe(tap(console.log), catchError(this.handleError))
    );

  ping$ = (ipAddress: String) =>
    <Observable<CustomResponse>>(
      this.httpc
        .get<CustomResponse>(`${this.apiURL}/server/ping/${ipAddress}`)
        .pipe(tap(console.log), catchError(this.handleError))
    );

  handleError(handleError: any): Observable<never> {
    return throwError('NYI');
  }
}
