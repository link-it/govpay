import { AfterContentChecked, AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { Voce } from '../../../../../../services/voce.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UtilService } from '../../../../../../services/util.service';
import { MatSelectChange } from '@angular/material';
import { SimpleListItem } from '../../../../../simple-list-card/simple-list-card.component';

const SEPARATORE: string = ', ';

@Component({
  selector: 'link-connettore-govpay',
  templateUrl: './connettore-govpay.component.html',
  styleUrls: ['./connettore-govpay.component.scss']
})
export class ConnettoreGovpayComponent implements IFormComponent, OnInit, AfterViewInit, AfterContentChecked {

  _Voce = Voce;
  Util = UtilService;
  @Input() fGroup: FormGroup;
  @Input() json: any;
  @Input() parent: any;

  pattern: string = '^(|([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5}){1,25})+((,\\s)(([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5}){1,25})+)*$';
  govpayAbilitato: FormControl = new FormControl(false, { updateOn: 'change', validators: Validators.required });
  tipoConnettore: FormControl = new FormControl('');
  govpayModalita: string = '';
  _option: any = { hasOption: false, hasAllOption: false };
  _all: any = { descrizione: UtilService.TUTTI_TIPI_PENDENZA.label, idTipoPendenza: UtilService.TUTTI_TIPI_PENDENZA.value };
  _tipiPendenza: any[] = (UtilService.PROFILO_UTENTE.tipiPendenza || []);

  _isAllegatoEmail: boolean = false;

  // REST
  _contenuti: SimpleListItem[] = UtilService.CONTENUTI_NOTIFICA_CONNETTORE;
  protected BASIC = UtilService.TIPI_AUTENTICAZIONE.basic;
  protected SSL = UtilService.TIPI_AUTENTICAZIONE.ssl;
  protected CLIENT = UtilService.TIPI_SSL.client;
  protected SERVER = UtilService.TIPI_SSL.server;
  protected versioni: any[] = UtilService.TIPI_VERSIONE_API;
  protected basicAuth: boolean = false;
  protected sslAuth: boolean = false;
  protected sslTypeValue: string = '';

  constructor() { }

  ngOnInit() {
    this.fGroup.addControl('govpayAbilitato_ctrl', this.govpayAbilitato);
    this.fGroup.addControl('versioneZip_ctrl', new FormControl(''));
    this.fGroup.addControl('tipoConnettore_ctrl', this.tipoConnettore);
    this.fGroup.addControl('tipiPendenza_ctrl', new FormControl(''));
    this.fGroup.addControl('emailIndirizzi_ctrl', new FormControl(''));
    this.fGroup.addControl('emailSubject_ctrl', new FormControl(''));
    this.fGroup.addControl('emailAllegato_ctrl', new FormControl(false));
    this.fGroup.addControl('downloadBaseUrl_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('fileSystemPath_ctrl', new FormControl(''));
    this.fGroup.addControl('contenuti_ctrl', new FormControl(''));
    this.fGroup.addControl('url_ctrl', new FormControl(''));
    this.fGroup.addControl('versioneApi_ctrl', new FormControl('', null));
    this.fGroup.addControl('auth_ctrl', new FormControl(''));
  }

  ngAfterContentChecked() {
    this.govpayModalita = (this.fGroup && this.fGroup.controls)?this.fGroup.controls['tipoConnettore_ctrl'].value:'';
  }

  ngAfterViewInit() {
    setTimeout(() => {
      this.fGroup.controls['govpayAbilitato_ctrl'].setValue(false);
      if(this.json) {
        this.fGroup.controls['govpayAbilitato_ctrl'].setValue(this.json.abilitato || false);
        this.fGroup.controls['versioneZip_ctrl'].setValue(this.json.versioneZip || '');
        this.fGroup.controls['tipoConnettore_ctrl'].setValue(this.json.tipoConnettore || '');
        this.fGroup.controls['tipiPendenza_ctrl'].setValue(this.json.tipiPendenza || '');
        if (this.json.emailIndirizzi) {
          this.fGroup.controls['emailIndirizzi_ctrl'].setValue(this.json.emailIndirizzi.join(SEPARATORE) || '');
        }
        this.fGroup.controls['emailSubject_ctrl'].setValue(this.json.emailSubject || '');
        this.fGroup.controls['emailAllegato_ctrl'].setValue(this.json.emailAllegato || false);
        if (!this.json.emailAllegato) {
          this.fGroup.controls['downloadBaseUrl_ctrl'].setValue(this.json.downloadBaseUrl || '');
        }
        this.fGroup.controls['fileSystemPath_ctrl'].setValue(this.json.fileSystemPath || '');
        if (this.json.contenuti) {
          this.fGroup.controls['contenuti_ctrl'].setValue(this.json.contenuti || '');
          this.sslAuth = false;
          this.basicAuth = false;
          if (this.json.url) {
            this.fGroup.controls['url_ctrl'].setValue(this.json.url);
          }
          if (this.json.versioneApi) {
            this.fGroup.controls['versioneApi_ctrl'].setValue(this.json.versioneApi);
          }
          if(this.json.auth) {
            if(this.json.auth.hasOwnProperty('username')){
              this.basicAuth = true;
              this.fGroup.controls['auth_ctrl'].setValue(this.BASIC);
              this.addBasicControls();
              (this.json.auth.username)?this.fGroup.controls['username_ctrl'].setValue(this.json.auth.username):null;
              (this.json.auth.password)?this.fGroup.controls['password_ctrl'].setValue(this.json.auth.password):null;
            }
            if(this.json.auth.hasOwnProperty('tipo')){
              this.sslAuth = true;
              this.fGroup.controls['auth_ctrl'].setValue(this.SSL);
              this.addSslTypeControls(this.json.auth.tipo);
              this.addSslControls(this.json.auth.tipo);
              // (this.json.auth.tipo)?this.fGroup.controls['ssl_ctrl'].setValue(this.json.auth.tipo):null;
              (this.json.auth.ksLocation)?this.fGroup.controls['ksLocation_ctrl'].setValue(this.json.auth.ksLocation):null;
              (this.json.auth.ksPassword)?this.fGroup.controls['ksPassword_ctrl'].setValue(this.json.auth.ksPassword):null;
              (this.json.auth.tsLocation)?this.fGroup.controls['tsLocation_ctrl'].setValue(this.json.auth.tsLocation):null;
              (this.json.auth.tsPassword)?this.fGroup.controls['tsPassword_ctrl'].setValue(this.json.auth.tsPassword):null;
            }
          } else {
            this.fGroup.controls['auth_ctrl'].setValue('');
          }
        }
        this.__bools(this.json.tipiPendenza);
        this._allegatoChange({ checked: this.json.emailAllegato });
        this._onChangeGovpay({ checked: this.json.abilitato }, 'govpayAbilitato_ctrl');
        this._onChangeGovpay({ value: this.json.tipoConnettore }, 'tipoConnettore_ctrl');
      }
    });
  }

  protected _tipoChange(event: MatSelectChange) {
    this._option.hasAllOption = false;
    this._option.hasOption = false;
    this.__bools(event.value);
  }

  protected __bools(values: any[]) {
    (values || []).forEach(value => {
      if (value.idTipoPendenza === '*') {
        this._option.hasAllOption = true;
        this._option.hasOption = false;
      } else {
        this._option.hasAllOption = false;
        this._option.hasOption = true;
      }
    });
  }

  _onChangeGovpay(event: any, type: string) {
    if (type === 'govpayAbilitato_ctrl') {
      (event.checked)?this.fGroup.controls['tipoConnettore_ctrl'].setValidators(Validators.required):this.fGroup.controls['tipoConnettore_ctrl'].clearValidators();
      (event.checked)?this.fGroup.controls['tipiPendenza_ctrl'].setValidators(Validators.required):this.fGroup.controls['tipiPendenza_ctrl'].clearValidators();
      this.fGroup.controls['tipoConnettore_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
      this.fGroup.controls['tipiPendenza_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
      if (!event.checked) {
        this.fGroup.controls['versioneZip_ctrl'].clearValidators();
        this.fGroup.controls['versioneApi_ctrl'].clearValidators();
        this.fGroup.controls['emailIndirizzi_ctrl'].clearValidators();
        this.fGroup.controls['downloadBaseUrl_ctrl'].clearValidators();
        this.fGroup.controls['fileSystemPath_ctrl'].clearValidators();
        this.fGroup.controls['contenuti_ctrl'].clearValidators();
        this.fGroup.controls['url_ctrl'].clearValidators();
        this.removeBasicControls();
        this.removeSslControls();
      }
    }
    if (type === 'tipoConnettore_ctrl') {
      // EMAIL
      (this.govpayAbilitato.value && event.value === UtilService.CONNETTORE_MODALITA_EMAIL)?this.fGroup.controls['emailIndirizzi_ctrl'].setValidators([Validators.required, Validators.pattern(this.pattern)]):this.fGroup.controls['emailIndirizzi_ctrl'].clearValidators();
      (this.govpayAbilitato.value && !this._isAllegatoEmail && event.value === UtilService.CONNETTORE_MODALITA_EMAIL)?this.fGroup.controls['downloadBaseUrl_ctrl'].setValidators([Validators.required]):this.fGroup.controls['downloadBaseUrl_ctrl'].clearValidators();
      // EMAIL, FS
      this.fGroup.controls['versioneZip_ctrl'].clearValidators();
      if (this.govpayAbilitato.value) {
        if (event.value === UtilService.CONNETTORE_MODALITA_EMAIL || event.value === UtilService.CONNETTORE_MODALITA_FILESYSTEM) {
          this.fGroup.controls['versioneZip_ctrl'].setValidators(Validators.required);
        }
      }
      // FS
      (this.govpayAbilitato.value && event.value === UtilService.CONNETTORE_MODALITA_FILESYSTEM)?this.fGroup.controls['fileSystemPath_ctrl'].setValidators(Validators.required):this.fGroup.controls['fileSystemPath_ctrl'].clearValidators();
      // REST
      (this.govpayAbilitato.value && event.value === UtilService.CONNETTORE_MODALITA_REST)?this.fGroup.controls['contenuti_ctrl'].setValidators(Validators.required):this.fGroup.controls['contenuti_ctrl'].clearValidators();
      (this.govpayAbilitato.value && event.value === UtilService.CONNETTORE_MODALITA_REST)?this.fGroup.controls['url_ctrl'].setValidators(Validators.required):this.fGroup.controls['url_ctrl'].clearValidators();
      (this.govpayAbilitato.value && event.value === UtilService.CONNETTORE_MODALITA_REST)?this.fGroup.controls['versioneApi_ctrl'].setValidators(Validators.required):this.fGroup.controls['versioneApi_ctrl'].clearValidators();
    }
    this.fGroup.controls['versioneZip_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
    this.fGroup.controls['versioneApi_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
    this.fGroup.controls['emailIndirizzi_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
    this.fGroup.controls['downloadBaseUrl_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
    this.fGroup.controls['fileSystemPath_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
    this.fGroup.controls['contenuti_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
    this.fGroup.controls['url_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
  }

  protected _onAuthChange(target: any) {
    this.removeBasicControls();
    this.removeSslControls();
    this.sslAuth = false;
    this.basicAuth = false;
    switch(target.value) {
      case this.BASIC:
        this.addBasicControls();
        this.basicAuth = true;
        break;
      case this.SSL:
        this.addSslTypeControls(this.SERVER);
        this.addSslControls(this.SERVER);
        this.sslAuth = true;
        break;
    }
  }

  protected _onSslTypeChange(target: any) {
    this.sslTypeValue = target.value;
    this.fGroup.controls['ksLocation_ctrl'].clearValidators();
    this.fGroup.controls['ksPassword_ctrl'].clearValidators();
    if (target.value === this.CLIENT) {
      this.fGroup.controls['ksLocation_ctrl'].setValidators(Validators.required);
      this.fGroup.controls['ksPassword_ctrl'].setValidators(Validators.required);
    }
    this.fGroup.controls['ksLocation_ctrl'].updateValueAndValidity();
    this.fGroup.controls['ksPassword_ctrl'].updateValueAndValidity();
  }

  protected _allegatoChange(event: any) {
    this._isAllegatoEmail = event.checked || false;
    (event.checked)?this.fGroup.controls['downloadBaseUrl_ctrl'].clearValidators():this.fGroup.controls['downloadBaseUrl_ctrl'].setValidators(Validators.required);
    (event.checked)?this.fGroup.controls['downloadBaseUrl_ctrl'].disable():this.fGroup.controls['downloadBaseUrl_ctrl'].enable();
    this.fGroup.controls['downloadBaseUrl_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
  }

  protected addBasicControls() {
    this.fGroup.addControl('username_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('password_ctrl', new FormControl('', Validators.required));
    this.fGroup.controls['username_ctrl'].updateValueAndValidity();
    this.fGroup.controls['password_ctrl'].updateValueAndValidity();
  }

  protected addSslTypeControls(tipo: string) {
    this.fGroup.addControl('ssl_ctrl', new FormControl(tipo, Validators.required));
    this.fGroup.controls['ssl_ctrl'].updateValueAndValidity();
  }

  protected addSslControls(tipo: string) {
    this.fGroup.addControl('tsLocation_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('tsPassword_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('ksLocation_ctrl', new FormControl(''));
    this.fGroup.addControl('ksPassword_ctrl', new FormControl(''));
    this.fGroup.controls['tsLocation_ctrl'].updateValueAndValidity();
    this.fGroup.controls['tsPassword_ctrl'].updateValueAndValidity();
    this.fGroup.controls['ksLocation_ctrl'].updateValueAndValidity();
    this.fGroup.controls['ksPassword_ctrl'].updateValueAndValidity();
  }

  protected removeBasicControls() {
    this.fGroup.removeControl('username_ctrl');
    this.fGroup.removeControl('password_ctrl');
  }

  protected removeSslControls() {
    this.fGroup.removeControl('ssl_ctrl');
    this.fGroup.removeControl('ksLocation_ctrl');
    this.fGroup.removeControl('ksPassword_ctrl');
    this.fGroup.removeControl('tsLocation_ctrl');
    this.fGroup.removeControl('tsPassword_ctrl');
  }

  _pendenzaCmpFn(p1: any, p2: any): boolean {
    return (p1 && p2)?(p1.idTipoPendenza === p2.idTipoPendenza):(p1 === p2);
  }

  mapToJson(): any {
    let _info = this.fGroup.value;
    let _json:any = {};

    _json.abilitato = (_info['govpayAbilitato_ctrl'] || false);
    if (_json.abilitato) {
      _json.tipiPendenza = _info['tipiPendenza_ctrl']?_info['tipiPendenza_ctrl'].map((p: any) => {
        return {
          idTipoPendenza: p.idTipoPendenza,
          descrizione: p.descrizione,
        };
      }):null;
      _json.versioneZip = _info['versioneZip_ctrl'] || null;
      _json.tipoConnettore = _info['tipoConnettore_ctrl'] || null;
      if (_json.tipoConnettore === UtilService.CONNETTORE_MODALITA_REST) {
        _json.contenuti = _info['contenuti_ctrl'] || null;
        _json.auth = null;
        _json.url = _info['url_ctrl']?_info['url_ctrl']:null;
        _json.versioneApi = _info['versioneApi_ctrl']?_info['versioneApi_ctrl']:null;
        if(_info.hasOwnProperty('username_ctrl')) {
          _json.auth = {
            password: _info['password_ctrl'],
            username: _info['username_ctrl']
          };
        }
        if(_info.hasOwnProperty('ssl_ctrl')) {
          _json.auth = {
            tipo: _info['ssl_ctrl'],
            ksLocation: _info['ksLocation_ctrl'],
            ksPassword: _info['ksPassword_ctrl'],
            tsLocation: '',
            tsPassword: ''
          };
          if(_info.hasOwnProperty('tsLocation_ctrl')) {
            _json.auth.tsLocation = _info['tsLocation_ctrl'];
          }
          if(_info.hasOwnProperty('tsPassword_ctrl')) {
            _json.auth.tsPassword = _info['tsPassword_ctrl'];
          }
        }
        if(_json.auth == null) { delete _json.auth; }
        if(_json.url == null) { delete _json.url; }
      }
      if (_json.tipoConnettore === UtilService.CONNETTORE_MODALITA_EMAIL) {
        _json.emailIndirizzi = _info['emailIndirizzi_ctrl']?_info['emailIndirizzi_ctrl'].split(SEPARATORE):null;
        _json.emailSubject = _info['emailSubject_ctrl'] || null;
        _json.emailAllegato = _info['emailAllegato_ctrl'] || false;
        if (_info['emailAllegato_ctrl'] === false) {
          _json.downloadBaseUrl = _info['downloadBaseUrl_ctrl'] || null;
        }
      } else {
        _json.fileSystemPath = _info['fileSystemPath_ctrl'] || null;
      }
    }

    Object.keys(_json).forEach(key => {
      if (_json[key] === null) {
        delete _json[key];
      }
    });

    return _json;
  }

}
