import { AfterViewInit, Component, Input, OnInit } from '@angular/core';

import { UtilService } from '../../services/util.service';
import { LinkService } from "../../services/link.service";
import { GovpayService } from '../../services/govpay.service';

import * as CryptoJS from 'crypto-js';

@Component({
  selector: 'link-oauth2-login-card',
  templateUrl: './oauth2-login-card.component.html',
  styleUrls: ['./oauth2-login-card.component.scss']
})
export class OAuth2LoginCardComponent implements OnInit, AfterViewInit {

  @Input('label') _label: string = 'Accedi';
  @Input('url') _url: string = '#';
  @Input('button-theme') _btnClass: string = '';

  protected OAUTH2Config: any = UtilService.OAUTH2;

  constructor() { }

  ngOnInit() {
  }

  ngAfterViewInit() {
  }

  goToLoginPage() {
    const state = UtilService.StrRandom(40);
    const codeVerifier = UtilService.StrRandom(128);

    const codeVerifierHash = CryptoJS.SHA256(codeVerifier).toString(CryptoJS.enc.Base64);

    const codeChallenge = codeVerifierHash
      .replace(/=/g, '')
      .replace(/\+/g, '-')
      .replace(/\//g, '_');

    window.localStorage.setItem(UtilService.STORAGE_VAR.STATE, state);
    window.localStorage.setItem(UtilService.STORAGE_VAR.CODE_VERIFIER, codeVerifier);
    window.localStorage.setItem(UtilService.STORAGE_VAR.CODE_CHALLENGE, codeChallenge);

    const params = [
      'response_type='+ this.OAUTH2Config.RESPONSE_TYPE,
      'state=' + state,
      'client_id=' + this.OAUTH2Config.CLIENT_ID,
      'scope='+ this.OAUTH2Config.SCOPE,
      'code_challenge=' + codeChallenge,
      'code_challenge_method='+ this.OAUTH2Config.CODE_CHALLENGE_METHOD,
      'redirect_uri=' + encodeURIComponent(this.OAUTH2Config.REDIRECT_URI + UtilService.URL_AUTH),
    ];

    window.location.href = this.OAUTH2Config.LOGIN_URL + '?' + params.join('&');
  }
}
