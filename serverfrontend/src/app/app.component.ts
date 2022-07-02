import {Component, OnInit} from '@angular/core';
import {BehaviorSubject, catchError, map, Observable, of, startWith} from 'rxjs';
import {DataState} from './enum/data-state.enum';
import {Status} from './enum/status.enum';
import {AppState} from './interface/app-state';
import {CustomResponse} from './interface/custom-response';
import {ServerService} from './service/server.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  appState$: Observable<AppState<CustomResponse>>;

  //to use enum in ui
  readonly DataState = DataState;
  readonly Status = Status;

  //to use filter in ui
  private filterSubject = new BehaviorSubject<string>("")
  filterStatus$ = this.filterSubject.asObservable();
  private dataSubject = new BehaviorSubject<CustomResponse>(null)

  constructor(private serverService: ServerService) {
  }

  ngOnInit(): void {
    this.appState$ = this
      .serverService
      .servers$
      .pipe(
        map((response) => {

          //this saves response inside dataSubject so we can have a copy in our component
          this.dataSubject.next(response)

          return {
            dataState: DataState.LOADED_STATE,
            appData: response,
          };
        }),
        startWith({
          dataState: DataState.LOADING_STATE,
        }),
        catchError((error: string) => {
          return of({dataState: DataState.ERROR_STATE, error});
        })
      );
  }

  pingServer(ipAddress: string): void {

    //causes an update on the app state, to trigger spinners etc
    this.filterSubject.next(ipAddress)

    //ask service to call backend to ping a server
    this.appState$ = this
      .serverService
      .ping$(ipAddress)
      .pipe(
        map((response) => {
          //update a specific server
          this.dataSubject.value.data.servers[
            this.dataSubject.value.data.servers.findIndex(
              server => server.id === response.data.server.id)
            ] = response.data.server;

          //stop showing the spinner
          this.filterSubject.next('')

          return {
            dataState: DataState.LOADED_STATE,
            appData: response,
          };
        }),

        startWith({
          dataState: DataState.LOADED_STATE,

          //we get our existing appData - we must use this.dataSubject because the startWith method does not pass a response.
          appData: this.dataSubject.value,
        }),

        catchError((error: string) => {

          //stop showing the spinner
          this.filterSubject.next('')

          return of({dataState: DataState.ERROR_STATE, error});
        })
      );
  }
}
