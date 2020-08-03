import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';

import { UtilService } from './util.service';

import { Observable } from 'rxjs/Observable';

import 'rxjs/add/operator/map';
import 'rxjs/add/operator/timeout';
import { forkJoin } from 'rxjs/observable/forkJoin';

import { of } from 'rxjs/observable/of';

@Injectable()
export class GovpayService {

  spinner: boolean = false;

  protected spinnerCount: number = 0;

  constructor(private http: HttpClient, private us: UtilService) { }

  updateSpinner(value: boolean) {
    this.spinnerCount = (value)?this.spinnerCount + 1:this.spinnerCount - 1;
    if (!this.spinner || this.spinnerCount <= 0) {
      this.spinner = value;
      (this.spinnerCount < 0)?this.spinnerCount = 0:null;
    }
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
    let url = UtilService.RootByTOA() + service;
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
   * @param {HttpHeaders} customHeaders
   * @param {string} responseDataType
   * @returns {Observable<any>}
   */
  saveData(service: string, body: any, query?: string, method?: string, autoHeaders: boolean = false, customHeaders: HttpHeaders = null, responseDataType = 'json'): Observable<any> {
    method = (method || UtilService.METHODS.PUT);
    let url = UtilService.RootByTOA() + service;
    let headers = new HttpHeaders();
    if(!autoHeaders) {
      headers = headers.set('Content-Type', 'application/json');
      headers = headers.set('Accept', '*/*');
    }
    if(customHeaders) {
      headers = customHeaders;
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
    let _request;
    switch(responseDataType) {
      case 'blob':
        _request = this.http.request(method, url, { body: body, headers: headers, observe: 'response', params: _params, responseType: 'blob' });
        break;
      default:
        _request = this.http.request(method, url, { body: body, headers: headers, observe: 'response', params: _params });
    }
    return _request.timeout(UtilService.TIMEOUT).map((response) => { return response; });
  }

  multiGetService(services: string[], properties: any[], content: any) {
    let headers = new HttpHeaders();
    headers = headers.set('Content-Type', 'application/json');
    headers = headers.set('Accept', '*/*');
    let methods = services.map((service) => {
      let url = UtilService.RootByTOA() + service;
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
      let url = UtilService.RootByTOA() + service;
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
    let headers = new HttpHeaders();
    headers = headers.set('Content-Type', 'application/json');
    let fullMethods: any[] = [];
    const methods = [];
    if(UtilService.BASIC.ENABLED) {
      methods.push({ service: UtilService.BASIC.HTTP_ROOT_SERVICE, type: UtilService.ACCESS_BASIC });
    }
    if(UtilService.SPID.ENABLED) {
      methods.push({ service: UtilService.SPID.HTTPS_ROOT_SERVICE, type: UtilService.ACCESS_SPID });
    }

    methods.forEach((_method) => {
      fullMethods.push(this.http.get(_method.service + service, { headers: headers, observe: 'response' }).catch(error => of(error)));
    });

    this.updateSpinner(true);
    return forkJoin(fullMethods)
      .map((responses: any) => {
        UtilService.TOA.Basic = false;
        UtilService.TOA.Spid = false;
        let _result;
        const _validResponse = responses.filter((response, index) => {
          UtilService.TOA[methods[index].type] = (response.status === 200);
          return response.status === 200;
        });
        if(_validResponse && _validResponse.length != 0) {
          _result = _validResponse[0];
        } else {
          throw(responses[0].error);
        }
        return _result;
      });
  }

  authenticate(data: any): Observable<any> {
    this.updateSpinner(true);
    let _headers = new HttpHeaders();
    _headers = _headers.set('Authorization', 'Basic ' + btoa(data.username + ':' + data.password));
    const options = { headers: _headers, withCredentials: true };

    return this.http.get(UtilService.BASIC.HTTP_ROOT_SERVICE + UtilService.URL_LOGIN_SERVICE, options);
  }

  exit(): Observable<any> {
    this.updateSpinner(true);
    let _headers = new HttpHeaders();
    _headers = _headers.set('Content-Type', 'application/json');

    return this.http.get(UtilService.LogoutByTOA(), { headers: _headers, observe: 'response' });
  }

  /**
   * FORK DATA SERVICES
   */
  forkService(methods: any[]): Observable<any> {
    let headers = new HttpHeaders();
    headers = headers.set('Content-Type', 'application/json');
    let fullMethods: any[] = [];
    methods.forEach((_method) => {
      fullMethods.push(this.http.get(UtilService.RootByTOA() + _method, { headers: headers, observe: 'response' }).timeout(UtilService.TIMEOUT));
    });

    return forkJoin(fullMethods)
      .map((response) => {
        return response;
      });
  }

}
