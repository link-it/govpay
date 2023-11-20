import { AfterViewInit, Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { IFormComponent } from '../../../../classes/interfaces/IFormComponent';
import { UtilService } from '../../../../services/util.service';
import { Voce } from '../../../../services/voce.service';

@Component({
  selector: 'link-ssl-config',
  templateUrl: './ssl-config.component.html',
  styles: []
})
export class SslConfigComponent implements IFormComponent, OnInit, OnChanges, AfterViewInit {
  @Input() fGroup: FormGroup;
  @Input() json: any;
  @Input() disabled: boolean = true;
  @Input() required: boolean = false;

  protected voce = Voce;
  protected NESSUNA = 'Nessuna';
  protected BASIC = UtilService.TIPI_AUTENTICAZIONE.basic;
  protected SSL = UtilService.TIPI_AUTENTICAZIONE.ssl;
  protected HEADER = UtilService.TIPI_AUTENTICAZIONE.header;
  protected API_KEY = UtilService.TIPI_AUTENTICAZIONE.apiKey;
  protected OAUTH2_CLIENT_CREDENTIALS = UtilService.TIPI_AUTENTICAZIONE.oauth2ClientCredentials;
  protected CLIENT = UtilService.TIPI_SSL.client;
  protected SERVER = UtilService.TIPI_SSL.server;
  protected tipiCfgSSL: any[] = [
    { value: 'SSL', label: 'SSL' },
    { value: 'SSLv3', label: 'SSL v3' },
    { value: 'TLS', label: 'TLS' },
    { value: 'TLSv1', label: 'TLS v1' },
    { value: 'TLSv1.1', label: 'TLS v1.1' },
    { value: 'TLSv1.2', label: 'TLS v1.2' }
  ];
  protected tipiKeystore: any[] = [
    { label: 'JKS', value: 'JKS' }
  ];
  protected tipiTruststore: any[] = [
    { label: 'JKS', value: 'JKS' }
  ];

  protected _isOAuth2ClientCredentialsAuth: boolean = false;
  protected _isApiKeyAuth: boolean = false;
  protected _isHeaderAuth: boolean = false;
  protected _isBasicAuth: boolean = false;
  protected _isSslAuth: boolean = false;
  protected _isSslClient: boolean = false;
  protected _isRequired: boolean = false;

  protected authCtrl: FormControl = new FormControl('');

  constructor() {}

  ngOnInit() {
    this.fGroup.addControl('auth_ctrl', this.authCtrl);
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.required && changes.required.currentValue ) {
      this.setValidatorsRequired();
      this.updateValueAndValidity();
    }
    if (changes.required && !changes.required.currentValue ) {
      this.clearValidators();
      this.updateValueAndValidity();
    }
    if (changes.disabled && changes.disabled.currentValue ) {
      this.resetSslConfig();
      this.authCtrl.disable();
    }
    if (changes.disabled && !changes.disabled.currentValue ) {
      this.authCtrl.enable();
    }
    setTimeout(() => {
      this._isRequired = this.required;
    }, 100);
  }

  ngAfterViewInit() {
    setTimeout(() => {
      this._isSslAuth = false;
      this._isBasicAuth = false;
      this._isHeaderAuth = false;
      this._isApiKeyAuth = false;
      this._isOAuth2ClientCredentialsAuth = false;
      this.fGroup.controls['auth_ctrl'].setValue(this.NESSUNA);
      if (this.json) {
        this.authCtrl.enable();
        if (this.json.hasOwnProperty('clientId')) {
          this._isOAuth2ClientCredentialsAuth = true;
          this.fGroup.controls['auth_ctrl'].setValue(this.OAUTH2_CLIENT_CREDENTIALS);
          this.addOAuth2ClientCredentialsControls();
          this.fGroup.controls['clientId_ctrl'].setValue(this.json.clientId);
          this.fGroup.controls['clientSecret_ctrl'].setValue(this.json.clientSecret);
          this.fGroup.controls['scope_ctrl'].setValue(this.json.scope);
          this.fGroup.controls['urlTokenEndpoint_ctrl'].setValue(this.json.urlTokenEndpoint);
        }
        if (this.json.hasOwnProperty('apiId')) {
          this._isApiKeyAuth = true;
          this.fGroup.controls['auth_ctrl'].setValue(this.API_KEY);
          this.addApiKeyControls();
          this.fGroup.controls['apiId_ctrl'].setValue(this.json.apiId);
          this.fGroup.controls['apiKey_ctrl'].setValue(this.json.apiKey);
        }
        if (this.json.hasOwnProperty('username')) {
          this._isBasicAuth = true;
          this.fGroup.controls['auth_ctrl'].setValue(this.BASIC);
          this.addBasicControls();
          this.fGroup.controls['username_ctrl'].setValue(this.json.username);
          this.fGroup.controls['password_ctrl'].setValue(this.json.password);
        }
        if (this.json.hasOwnProperty('headerName')) {
          this._isHeaderAuth = true;
          this.fGroup.controls['auth_ctrl'].setValue(this.HEADER);
          this.addHeaderControls();
          this.fGroup.controls['headerName_ctrl'].setValue(this.json.headerName);
          this.fGroup.controls['headerValue_ctrl'].setValue(this.json.headerValue);
        }
        if (this.json.hasOwnProperty('tipo')) {
          this._isSslAuth = true;
          this.fGroup.controls['auth_ctrl'].setValue(this.SSL);
          this.addSslControls();
          this.fGroup.controls['ssl_ctrl'].setValue(this.json.tipo === this.CLIENT);
          this.fGroup.controls['sslType_ctrl'].setValue(this.json.sslType);
          this.fGroup.controls['tsType_ctrl'].setValue(this.json.tsType);
          this.fGroup.controls['tsLocation_ctrl'].setValue(this.json.tsLocation);
          this.fGroup.controls['tsPassword_ctrl'].setValue(this.json.tsPassword);
          if (this.json.tipo === this.CLIENT) {
            this.addSslClientControls();
            this.fGroup.controls['ksType_ctrl'].setValue(this.json.ksType);
            this.fGroup.controls['ksLocation_ctrl'].setValue(this.json.ksLocation);
            this.fGroup.controls['ksPassword_ctrl'].setValue(this.json.ksPassword);
            this.fGroup.controls['ksPKeyPasswd_ctrl'].setValue(this.json.ksPKeyPasswd);
            this._isSslClient = true;
          }
        }
        this.setValidatorsRequired();
        this.updateValueAndValidity();
      }
    });
  }

  protected _onAuthChange(target: any) {
	this._isApiKeyAuth = false;
    this._isBasicAuth = false;
    this._isHeaderAuth = false;
    this._isSslClient = false;
    this._isSslAuth = false;
    this._isOAuth2ClientCredentialsAuth = false;
    this.removeBasicControls();
    this.removeSslControls();
    this.removeHeaderControls();
    this.removeApiKeyControls();
    this.removeOAuth2ClientCredentialsControls();
    switch(target.value) {
      case this.BASIC:
        this._isBasicAuth = true;
        this.addBasicControls();
        break;
      case this.SSL:
        this._isSslAuth = true;
        this.addSslControls();
        break;
     case this.HEADER:
        this._isHeaderAuth = true;
        this.addHeaderControls();
        break;
     case this.API_KEY:
        this._isApiKeyAuth = true;
        this.addApiKeyControls();
        break;
     case this.OAUTH2_CLIENT_CREDENTIALS:
        this._isOAuth2ClientCredentialsAuth = true;
        this.addOAuth2ClientCredentialsControls();
        break;
    }
    this.updateValueAndValidity();
  }

  protected _onTypeChange(target) {
    this._isSslClient = false;
    this.removeSslClientControls();
    if (target.checked === true) {
      this._isSslClient = true;
      this.addSslClientControls();
    }
    this.updateValueAndValidity();
  }
  
  protected addOAuth2ClientCredentialsControls() {
    if (this.fGroup) {
      this.fGroup.addControl('clientId_ctrl', new FormControl('', (this._isOAuth2ClientCredentialsAuth && this.required)?[Validators.required]:[]));
      this.fGroup.addControl('clientSecret_ctrl', new FormControl('', (this._isOAuth2ClientCredentialsAuth && this.required)?[Validators.required]:[]));
      this.fGroup.addControl('scope_ctrl', new FormControl(''));
      this.fGroup.addControl('urlTokenEndpoint_ctrl', new FormControl('', (this._isOAuth2ClientCredentialsAuth && this.required)?[Validators.required]:[]));
    }
  }

  protected addBasicControls() {
    if (this.fGroup) {
      this.fGroup.addControl('username_ctrl', new FormControl('', (this._isBasicAuth && this.required)?[Validators.required]:[]));
      this.fGroup.addControl('password_ctrl', new FormControl('', (this._isBasicAuth && this.required)?[Validators.required]:[]));
    }
  }
  
  protected addHeaderControls() {
    if (this.fGroup) {
      this.fGroup.addControl('headerName_ctrl', new FormControl('', (this._isHeaderAuth && this.required)?[Validators.required]:[]));
      this.fGroup.addControl('headerValue_ctrl', new FormControl('', (this._isHeaderAuth && this.required)?[Validators.required]:[]));
    }
  }
  
  protected addApiKeyControls() {
    if (this.fGroup) {
      this.fGroup.addControl('apiId_ctrl', new FormControl('', (this._isApiKeyAuth && this.required)?[Validators.required]:[]));
      this.fGroup.addControl('apiKey_ctrl', new FormControl('', (this._isApiKeyAuth && this.required)?[Validators.required]:[]));
    }
  }

  protected addSslControls() {
    if (this.fGroup) {
      this.fGroup.addControl('ssl_ctrl', new FormControl(false));
      this.fGroup.addControl('sslType_ctrl', new FormControl('', (this._isSslAuth && this.required)?[Validators.required]:[]));
      this.fGroup.addControl('tsType_ctrl', new FormControl('', (this._isSslAuth && this.required)?[Validators.required]:[]));
      this.fGroup.addControl('tsLocation_ctrl', new FormControl('', (this._isSslAuth && this.required)?[Validators.required]:[]));
      this.fGroup.addControl('tsPassword_ctrl', new FormControl('', (this._isSslAuth && this.required)?[Validators.required]:[]));
    }
  }

  protected addSslClientControls() {
    if (this.fGroup) {
      this.fGroup.addControl('ksType_ctrl', new FormControl('', (this._isSslClient && this.required)?[Validators.required]:[]));
      this.fGroup.addControl('ksLocation_ctrl', new FormControl('', (this._isSslClient && this.required)?[Validators.required]:[]));
      this.fGroup.addControl('ksPassword_ctrl', new FormControl('', (this._isSslClient && this.required)?[Validators.required]:[]));
      this.fGroup.addControl('ksPKeyPasswd_ctrl', new FormControl('', (this._isSslClient && this.required)?[Validators.required]:[]));
    }
  }

  protected removeSslClientControls() {
    if (this.fGroup) {
      this.fGroup.removeControl('ksType_ctrl');
      this.fGroup.removeControl('ksLocation_ctrl');
      this.fGroup.removeControl('ksPassword_ctrl');
      this.fGroup.removeControl('ksPKeyPasswd_ctrl');
    }
  }

  protected removeSslControls() {
    if (this.fGroup) {
      this.fGroup.removeControl('ssl_ctrl');
      this.fGroup.removeControl('sslType_ctrl');
      this.fGroup.removeControl('tsType_ctrl');
      this.fGroup.removeControl('tsLocation_ctrl');
      this.fGroup.removeControl('tsPassword_ctrl');
      this.fGroup.removeControl('ksType_ctrl');
      this.fGroup.removeControl('ksLocation_ctrl');
      this.fGroup.removeControl('ksPassword_ctrl');
      this.fGroup.removeControl('ksPKeyPasswd_ctrl');
    }
  }

  protected removeBasicControls() {
    if (this.fGroup) {
      this.fGroup.removeControl('username_ctrl');
      this.fGroup.removeControl('password_ctrl');
    }
  }
  
  protected removeHeaderControls() {
    if (this.fGroup) {
      this.fGroup.removeControl('headerName_ctrl');
      this.fGroup.removeControl('headerValue_ctrl');
    }
  }
  
  protected removeApiKeyControls() {
    if (this.fGroup) {
      this.fGroup.removeControl('apiId_ctrl');
      this.fGroup.removeControl('apiKey_ctrl');
    }
  }
  
  protected removeOAuth2ClientCredentialsControls() {
    if (this.fGroup) {
      this.fGroup.removeControl('clientId_ctrl');
      this.fGroup.removeControl('clientSecret_ctrl');
      this.fGroup.removeControl('scope_ctrl');
      this.fGroup.removeControl('urlTokenEndpoint_ctrl');
    }
  }

  resetSslConfig() {
    this._isSslAuth = false;
    this._isBasicAuth = false;
    this._isHeaderAuth = false;
    this._isApiKeyAuth = false;
    this._isOAuth2ClientCredentialsAuth = false;
    this.resetControllers();
  }

  resetControllers() {
    this.authCtrl.setValue(this.NESSUNA);
    this.removeBasicControls();
    this.removeSslControls();
    this.removeHeaderControls();
    this.removeApiKeyControls();
    this.removeOAuth2ClientCredentialsControls();
  }

  clearValidators() {
    this.authCtrl.clearValidators();
    if (this.fGroup) {
      const controls: any = this.fGroup.controls;
      if (this._isOAuth2ClientCredentialsAuth) {
        controls['clientId_ctrl'].clearValidators();
        controls['clientId_ctrl'].setErrors(null);
        controls['clientSecret_ctrl'].clearValidators();
        controls['clientSecret_ctrl'].setErrors(null);
//        controls['scope_ctrl'].clearValidators();
//        controls['scope_ctrl'].setErrors(null);
        controls['urlTokenEndpoint_ctrl'].clearValidators();
        controls['urlTokenEndpoint_ctrl'].setErrors(null);
      }
      if (this._isApiKeyAuth) {
        controls['apiId_ctrl'].clearValidators();
        controls['apiId_ctrl'].setErrors(null);
        controls['apiKey_ctrl'].clearValidators();
        controls['apiKey_ctrl'].setErrors(null);
      }
      if (this._isHeaderAuth) {
        controls['headerName_ctrl'].clearValidators();
        controls['headerName_ctrl'].setErrors(null);
        controls['headerValue_ctrl'].clearValidators();
        controls['headerValue_ctrl'].setErrors(null);
      }
      if (this._isBasicAuth) {
        controls['username_ctrl'].clearValidators();
        controls['username_ctrl'].setErrors(null);
        controls['password_ctrl'].clearValidators();
        controls['password_ctrl'].setErrors(null);
      }
      if (this._isSslAuth) {
        controls['ssl_ctrl'].clearValidators();
        controls['ssl_ctrl'].setErrors(null);
        controls['sslType_ctrl'].clearValidators();
        controls['sslType_ctrl'].setErrors(null);
        controls['tsType_ctrl'].clearValidators();
        controls['tsType_ctrl'].setErrors(null);
        controls['tsLocation_ctrl'].clearValidators();
        controls['tsLocation_ctrl'].setErrors(null);
        controls['tsPassword_ctrl'].clearValidators();
        controls['tsPassword_ctrl'].setErrors(null);
        if (this._isSslClient) {
          controls['ksType_ctrl'].clearValidators();
          controls['ksType_ctrl'].setErrors(null);
          controls['ksLocation_ctrl'].clearValidators();
          controls['ksLocation_ctrl'].setErrors(null);
          controls['ksPassword_ctrl'].clearValidators();
          controls['ksPassword_ctrl'].setErrors(null);
          controls['ksPKeyPasswd_ctrl'].clearValidators();
          controls['ksPKeyPasswd_ctrl'].setErrors(null);
        }
      }
    }
  }

  setValidatorsRequired() {
    this.authCtrl.setValidators(Validators.required);
    if (this.fGroup) {
      const controls: any = this.fGroup.controls;
      if (this._isOAuth2ClientCredentialsAuth) {
        controls['clientId_ctrl'].setValidators(Validators.required);
        controls['clientSecret_ctrl'].setValidators(Validators.required);
        controls['urlTokenEndpoint_ctrl'].setValidators(Validators.required);
      }
      if (this._isApiKeyAuth) {
        controls['apiId_ctrl'].setValidators(Validators.required);
        controls['apiKey_ctrl'].setValidators(Validators.required);
      }
      if (this._isHeaderAuth) {
        controls['headerName_ctrl'].setValidators(Validators.required);
        controls['headerValue_ctrl'].setValidators(Validators.required);
      }
      if (this._isBasicAuth) {
        controls['username_ctrl'].setValidators(Validators.required);
        controls['password_ctrl'].setValidators(Validators.required);
      }
      if (this._isSslAuth) {
        controls['ssl_ctrl'].setValidators(Validators.required);
        controls['sslType_ctrl'].setValidators(Validators.required);
        controls['tsType_ctrl'].setValidators(Validators.required);
        controls['tsLocation_ctrl'].setValidators(Validators.required);
        controls['tsPassword_ctrl'].setValidators(Validators.required);
        if (this._isSslClient) {
          controls['ksType_ctrl'].setValidators(Validators.required);
          controls['ksLocation_ctrl'].setValidators(Validators.required);
          controls['ksPassword_ctrl'].setValidators(Validators.required);
          controls['ksPKeyPasswd_ctrl'].setValidators(Validators.required);
        }
      }
    }
  }

  updateValueAndValidity() {
    setTimeout(() => {
      if (this.fGroup) {
        const controls: any = this.fGroup.controls;
        if (this._isOAuth2ClientCredentialsAuth) {
          controls['clientId_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
          controls['clientSecret_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
          controls['scope_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
          controls['urlTokenEndpoint_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
        }
        if (this._isApiKeyAuth) {
          controls['apiId_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
          controls['apiKey_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
        }
        if (this._isHeaderAuth) {
          controls['headerName_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
          controls['headerValue_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
        }
        if (this._isBasicAuth) {
          controls['username_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
          controls['password_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
        }
        if (this._isSslAuth) {
          controls['ssl_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
          controls['sslType_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
          controls['tsType_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
          controls['tsLocation_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
          controls['tsPassword_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
          if (this._isSslClient) {
            controls['ksType_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
            controls['ksLocation_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
            controls['ksPassword_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
            controls['ksPKeyPasswd_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
          }
        }
      }
      this.authCtrl.updateValueAndValidity({ onlySelf: false, emitEvent: true });
    });
  }

  mapToJson(): any {
    const _info = this.fGroup.value;
    let _json: any = null;

    if(_info.hasOwnProperty('clientId_ctrl')) {
      _json = {
        clientId: _info['clientId_ctrl'],
        clientSecret: _info['clientSecret_ctrl'],
        scope: (_info['scope_ctrl'])?_info['scope_ctrl']:null,
        urlTokenEndpoint: _info['urlTokenEndpoint_ctrl']
      };
    }
    if(_info.hasOwnProperty('apiId_ctrl')) {
      _json = {
        apiId: _info['apiId_ctrl'],
        apiKey: _info['apiKey_ctrl']
      };
    }
    if(_info.hasOwnProperty('headerName_ctrl')) {
      _json = {
        headerName: _info['headerName_ctrl'],
        headerValue: _info['headerValue_ctrl']
      };
    }
    if(_info.hasOwnProperty('username_ctrl')) {
      _json = {
        password: _info['password_ctrl'],
        username: _info['username_ctrl']
      };
    }
    if(_info.hasOwnProperty('ssl_ctrl')) {
      _json = {
        tipo: _info['ssl_ctrl']?this.CLIENT:this.SERVER,
        tsType: _info['tsType_ctrl'],
        sslType: _info['sslType_ctrl'],
        tsLocation: _info['tsLocation_ctrl'],
        tsPassword: _info['tsPassword_ctrl'],
        ksType: '',
        ksLocation: '',
        ksPassword: '',
        ksPKeyPasswd: ''
      };
      if(_info['ssl_ctrl'] === true) {
        _json.ksType = _info['ksType_ctrl'];
        _json.ksLocation = _info['ksLocation_ctrl'];
        _json.ksPassword = _info['ksPassword_ctrl'];
        _json.ksPKeyPasswd = _info['ksPKeyPasswd_ctrl'];
      } else {
        delete _json.ksType;
        delete _json.ksLocation;
        delete _json.ksPassword;
        delete _json.ksPKeyPasswd;
      }
    }

    return _json;
  }

}
