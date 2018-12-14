import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';

import { UtilService } from './util.service';

import { Observable } from 'rxjs/Observable';
import { ErrorObservable } from 'rxjs/observable/ErrorObservable';
import { catchError } from 'rxjs/operators';

import 'rxjs/add/operator/map';
import 'rxjs/add/operator/timeout';
import { forkJoin } from 'rxjs/observable/forkJoin';

@Injectable()
export class GovpayService {

  spinner: boolean = false;

  progress: boolean = false;
  progressValue: number = 0;

  protected spinnerCount: number = 0;

  constructor(private http: HttpClient, private us: UtilService) { }

  updateSpinner(value: boolean) {
    this.spinnerCount = (value)?this.spinnerCount + 1:this.spinnerCount - 1;
    if (!this.spinner || this.spinnerCount <= 0) {
      this.spinner = value;
      (this.spinnerCount < 0)?this.spinnerCount = 0:null;
    }
  }

  updateProgress(show: boolean, value: number = 0) {
    this.progress = show;
    (show)?this.progressValue = value:null;
  }

  /**
   * Logout
   */
  logoutService() {
    location.href = UtilService.LOGOUT_SERVICE;
  }

  /**
   * Unauthorized Service
   */
  unauthorizedService() {
    location.href = UtilService.BASE_HREF;
  }

  /**
   * GET SERVICE
   * @param {string} service: service URL
   * @param {string} query: query string
   * @returns {Observable<any>}
   */
  getDataService(service: string, query?: string): Observable<any> {
    this.updateSpinner(true);
    let url = UtilService.ROOT_SERVICE + service;
    let headers = new HttpHeaders();
    headers = headers.set('Content-Type', 'application/json');
    headers = headers.set('Accept', '*/*');
    let _params = null;
    if(query) {
      _params = new HttpParams();
      let _qlist = query.split('&');
      _qlist.forEach((q) => {
        let p = q.split('=');
        _params = _params.set(p[0], p[1]);
      });
    }
    return this.http.get(url, { headers: headers, observe: 'response', params: _params })
      .timeout(UtilService.TIMEOUT)
      .map((response) => {
        return response;
      })
      .pipe(catchError(this.handleError.bind(this, service)));
  }

  /**
   * PUT/POST SERVICE
   * @param {string} service: service URL
   * @param {any} body: body object
   * @param {string} query: query string
   * @param {string} method: POST/PUT
   * @param {boolean} autoHeaders: true|false
   * @returns {Observable<any>}
   */
  saveData(service: string, body: any, query?: string, method?: string, autoHeaders: boolean = false): Observable<any> {
    method = (method || UtilService.METHODS.PUT);
    let url = UtilService.ROOT_SERVICE + service;
    let headers = new HttpHeaders();
    if(!autoHeaders) {
      headers = headers.set('Content-Type', 'application/json');
      headers = headers.set('Accept', '*/*');
    }
    let _params = null;
    if(query) {
      _params = new HttpParams();
      let _qlist = query.split('&');
      _qlist.forEach((q) => {
        let p = q.split('=');
        _params = _params.set(p[0], p[1]);
      });
    }
    this.updateSpinner(true);

    return this.http.request(method, url, { body: body, headers: headers, observe: 'response', params: _params })
      .timeout(UtilService.TIMEOUT)
      .map((response) => { return response; })
      .pipe(catchError(this.handleError.bind(this, service)));
  }

  multiGetService(services: string[], properties: any[], content: any) {
    let headers = new HttpHeaders();
    headers = headers.set('Content-Type', 'application/json');
    headers = headers.set('Accept', '*/*');
    let methods = services.map((service) => {
      let url = UtilService.ROOT_SERVICE + service;
      let method = this.http.get(url, { headers: headers, observe: 'response' }).timeout(UtilService.TIMEOUT);
      return method;
    });
    this.updateSpinner(true);
    forkJoin(methods).subscribe(results => {
      results.forEach((result, index) => {
        content[properties[index]] = result.body;
      });
      this.updateSpinner(false);
    },
    (error) => {
      this.updateSpinner(false);
      this.us.onError(error);
    });
  }

  multiExportService(services: string[], contents: string[], types: string[]): Observable<any> {
    let methods = services.map((service, index) => {
      let url = UtilService.ROOT_SERVICE + service;
      let headers = new HttpHeaders();
      headers = headers.set('Content-Type', contents[index]);
      headers = headers.set('Accept', contents[index]);
      let method;
      switch(types[index]) {
        case 'blob':
          method = this.http.get(url, { headers: headers, observe: 'response', responseType: 'blob' });
          break;
        case 'text':
          method = this.http.get(url, { headers: headers, observe: 'response', responseType: 'text' });
          break;
        default:
          method = this.http.get(url, { headers: headers, observe: 'response', responseType: 'json' });
      }
      return method.timeout(UtilService.TIMEOUT);
    });
    return forkJoin(methods)
      .map((response) => {
        return response;
      })
      .pipe(catchError(this.handleExportError.bind(this)));
  }


  /**
   * FORK DATA SERVICES
   */
  forkService(methods: any[]): Observable<any> {
    let headers = new HttpHeaders();
    headers = headers.set('Content-Type', 'application/json');
    let fullMethods: any[] = [];
    methods.forEach((_method) => {
      fullMethods.push(this.http.get(UtilService.ROOT_SERVICE + _method, { headers: headers, observe: 'response' }));
    });

    return forkJoin(fullMethods)
      .map((response) => {
        return response;
      })
      .pipe(catchError(this.handleExportError.bind(this)));
  }

  private handleExportError(error: HttpErrorResponse): ErrorObservable {
    try {
      if(error.headers && error.headers.get('content-type') === 'text/html') {
        this.unauthorizedService();
        return new ErrorObservable({ message: 'Session expired.', code: 503, instance: error });
      }
    } catch(e) {
      console.log(e);
    }

    return new ErrorObservable({ message: 'Si è verificato un problema. Esportazione interrotta.', code: error.status, instance: error });
  }

  private handleError(service: string, error: HttpErrorResponse): ErrorObservable {
    try {
      if(error.headers && error.headers.get('content-type') === 'text/html') {
        this.unauthorizedService();
        return new ErrorObservable({ message: 'Session expired.', code: 503, instance: error });
      }
      if(service == UtilService.URL_PROFILO) {
        this.logoutService();
        return new ErrorObservable({ message: 'Profilo utente non presente.', code: 503, instance: error });
      }
    } catch(e) {
      console.log(e);
    }

    return new ErrorObservable({ message: error.message?error.message:'Error non previsto.', code: error.status, instance: error });
  }


}
