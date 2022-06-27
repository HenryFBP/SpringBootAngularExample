import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';
import { Status } from '../enum/status.enum';
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

  ping$ = (ipAddress: string) =>
    <Observable<CustomResponse>>(
      this.httpc
        .get<CustomResponse>(`${this.apiURL}/server/ping/${ipAddress}`)
        .pipe(tap(console.log), catchError(this.handleError))
    );

  filter$ = (status: Status, response: CustomResponse) =>
    new Observable<CustomResponse>((subscriber) => {
      console.log(response);

      if (status === Status.ALL) {
        subscriber.next({
          ...response, //spread operator allows us to add extra keys to this map.
          message: `Servers filtered by ${status} status`,
        });
        subscriber.complete();
        return;
      }

      var msg: string = '';
      var serversWithMatchingStatus = response.data.servers.filter(
        (server) => server.status === status
      );

      if (serversWithMatchingStatus.length > 0) {
        msg = `Servers filtered by ${status.replace('_', ' ')} status`;
      } else {
        msg = `No servers of ${status} found`;
      }

      subscriber.next({
        ...response,
        message: msg,
        data: {
          servers: serversWithMatchingStatus,
        },
      });

      subscriber.complete();
    })
    .pipe(
      tap(console.log),
      catchError(this.handleError)
    );

  delete$ = (serverId: number) =>
    <Observable<CustomResponse>>(
      this.httpc
        .delete<CustomResponse>(`${this.apiURL}/server/delete/${serverId}`)
        .pipe(tap(console.log), catchError(this.handleError))
    );

  private handleError(error: HttpErrorResponse): Observable<never> {
    console.log(error);
    return throwError(`An error occurred - Error code: ${error.status}`);
  }
}
