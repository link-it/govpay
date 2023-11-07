import { AfterViewInit, Component, Input, OnInit } from '@angular/core';

import { LinkService } from "../../services/link.service";
import { GovpayService } from '../../services/govpay.service';
import { UtilService } from '../../services/util.service';
import { Voce } from '../../services/voce.service';

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

        const state = this.strRandom(40);
        const codeVerifier = this.strRandom(128);

//        this.storage.set('state', state);
//        this.storage.set('codeVerifier', codeVerifier);

//        const codeVerifierHash = CryptoJS.SHA256(codeVerifier).toString(CryptoJS.enc.Base64);
//
//        const codeChallenge = codeVerifierHash
//            .replace(/=/g, '')
//            .replace(/\+/g, '-')
//            .replace(/\//g, '_');
            
       const codeChallenge = '8eBD9a4G8v8570qhszOOoyLrc28EVVUsmkyy8_AgBd8';

        const params = [
            'response_type='+ this.OAUTH2Config.RESPONSE_TYPE,
            'state=' + state,
            'client_id=' + this.OAUTH2Config.CLIENT_ID,
            'scope='+ this.OAUTH2Config.SCOPE,
            'code_challenge=' + codeChallenge,
            'code_challenge_method='+ this.OAUTH2Config.CODE_CHALLENGE_METHOD,
            'redirect_uri=' + encodeURIComponent(this.OAUTH2Config.REDIRECT_URI),
        ];

        window.location.href = this.OAUTH2Config.LOGIN_URL + '?' + params.join('&');
    }

    private strRandom(length: number) {
        let result = '';
        const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
        const charactersLength = characters.length;
        for (let i = 0; i < length; i++) {
            result += characters.charAt(Math.floor(Math.random() * charactersLength));
        }
        return result;
    }
}
