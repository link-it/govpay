import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';

import { UtilService } from './util.service';

import { Observable } from 'rxjs/Observable';
import { ErrorObservable } from 'rxjs/observable/ErrorObservable';
import { catchError } from 'rxjs/operators';

import 'rxjs/add/operator/map';

@Injectable()
export class GovpayService {

  spinner: boolean = false;

  protected spinnerCount: number = 0;

  constructor(private http: HttpClient) { }

  updateSpinner(value: boolean) {
    this.spinnerCount = (value)?this.spinnerCount + 1:this.spinnerCount - 1;
    if (!this.spinner || this.spinnerCount <= 0) {
      this.spinner = value;
      (this.spinnerCount < 0)?this.spinnerCount = 0:null;
    }
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
    headers = headers.set('Authorization', 'Basic ' + UtilService.AUTHORIZATION);
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
      .map((response) => { return response; })
      .pipe(catchError(this.handleError));
  }

  /**
   * PUT/POST SERVICE
   * @param {string} service: service URL
   * @param {any} body: body object
   * @param {string} query: query string
   * @param {string} method: POST/PUT
   * @returns {Observable<any>}
   */
  saveData(service: string, body: any, query?: string, method?: string): Observable<any> {
    method = (method || UtilService.METHODS.PUT);
    let url = UtilService.ROOT_SERVICE + service;
    let headers = new HttpHeaders();
    headers = headers.set('Authorization', 'Basic ' + UtilService.AUTHORIZATION);
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
    this.updateSpinner(true);

    return this.http.request(method, url, { body, headers: headers, observe: 'response', params: _params })
      .map((response) => { return response; })
      .pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse): ErrorObservable {
    return new ErrorObservable({ message: error.message?error.message:'Error non previsto.', code: error.status, instance: error });
  };


}
