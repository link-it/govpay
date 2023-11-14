import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';

import { UtilService } from '../../services/util.service';

@Component({
  selector: 'link-auth-view',
  templateUrl: './auth-view.component.html',
  styleUrls: ['./auth-view.component.scss']
})
export class AuthViewComponent implements OnInit {

  _isLoading:boolean = true;

  state: string;
  codeVerifier: string;
  codeChallenge: string;

  error: boolean = false;
  errorMessage: string = '';

  protected OAUTH2Config: any = UtilService.OAUTH2;

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private http: HttpClient
  ) { }

  ngOnInit() {
    this.state = window.localStorage.getItem(UtilService.STORAGE_VAR.TOKEN);
    this.codeVerifier = window.localStorage.getItem(UtilService.STORAGE_VAR.CODE_VERIFIER);
    this.codeChallenge = window.localStorage.getItem(UtilService.STORAGE_VAR.CODE_CHALLENGE);

    this.activatedRoute.queryParams.subscribe(params => {
      if (params.code) {
        this.getAccessToken(params.code, params.state);
      }
    });
  }

  getAccessToken(code: string, state: string) {
    if (state !== this.state) {
      this.error = true;
      this.errorMessage = 'Invalid state';
      this.router.navigate(['/dashboard']);
      return;
    }

    const payload = new HttpParams()
      .append('grant_type', 'authorization_code')
      .append('code', code)
      .append('code_verifier', this.codeVerifier)
      .append('redirect_uri', this.OAUTH2Config.REDIRECT_URI)
      .append('client_id', this.OAUTH2Config.CLIENT_ID);

    const headers = {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded'}
    };

    this.http.post(this.OAUTH2Config.TOKEN_URL, payload, headers)
      .subscribe(
        (response: any) => {
          this._clearStorage();
          window.localStorage.setItem(UtilService.STORAGE_VAR.TOKEN, response.access_token);
          this._isLoading = false;
          this.router.navigate(['/dashboard']);
        },
        (error: any) => {
          console.log('error', error);
          this._clearStorage();
          this._isLoading = false;
          this.router.navigate(['/dashboard']);
        }
      );
  }

  _clearStorage() {
    window.localStorage.removeItem(UtilService.STORAGE_VAR.TOKEN);
    window.localStorage.removeItem(UtilService.STORAGE_VAR.STATE);
    window.localStorage.removeItem(UtilService.STORAGE_VAR.CODE_VERIFIER);
    window.localStorage.removeItem(UtilService.STORAGE_VAR.CODE_CHALLENGE);
  }
}
