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
    this.exit().subscribe(
      (_response) => {
        this.updateSpinner(false);
        UtilService.cleanUser();
      },
      (error) => {
        this.updateSpinner(false);
        this.us.onError(error);
      });
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
      });
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
      .map((response) => { return response; });
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
      });
  }

  isAuthenticated(service): Observable<any> {
    this.updateSpinner(true);
    let url = UtilService.ROOT_SERVICE + service;
    let _headers = new HttpHeaders();
    _headers = _headers.set('Content-Type', 'application/json');
    _headers = _headers.set('Accept', '*/*');
    return this.http.get(url, { headers: _headers, observe: 'response' })
      .timeout(UtilService.TIMEOUT)
      .map((response) => {
        return response;
      })
  }

  authenticate(data: any): Observable<any> {
    this.updateSpinner(true);
    let _headers = new HttpHeaders();
    _headers = _headers.set('Authorization', 'Basic ' + btoa(data.username + ':' + data.password));
    const options = { headers: _headers, withCredentials: true };

    return this.http.get(UtilService.ROOT_SERVICE + UtilService.URL_LOGIN_SERVICE, options);
  }

  exit(): Observable<any> {
    this.updateSpinner(true);
    let _headers = new HttpHeaders();
    _headers = _headers.set('Content-Type', 'application/json');

    return this.http.get(UtilService.URL_LOGOUT_SERVICE, { headers: _headers, observe: 'response' });
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
      });
  }

}
