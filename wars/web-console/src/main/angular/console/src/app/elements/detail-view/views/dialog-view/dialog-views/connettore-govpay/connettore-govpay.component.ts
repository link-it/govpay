import { AfterViewInit, Component, Input, OnInit, ViewChild } from '@angular/core';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { Voce } from '../../../../../../services/voce.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UtilService } from '../../../../../../services/util.service';
import { MatSelectChange } from '@angular/material';
import { SimpleListItem } from '../../../../../simple-list-card/simple-list-card.component';
import { SslConfigComponent } from '../../../ssl-config/ssl-config.component';

const SEPARATORE: string = ', ';

@Component({
  selector: 'link-connettore-govpay',
  templateUrl: './connettore-govpay.component.html',
  styleUrls: ['./connettore-govpay.component.scss']
})
export class ConnettoreGovpayComponent implements IFormComponent, OnInit, AfterViewInit {
  @ViewChild('sslConfig') sslConfig: SslConfigComponent;

  _Voce = Voce;
  Util = UtilService;
  @Input() fGroup: FormGroup;
  @Input() json: any;
  @Input() parent: any;

  pattern: string = '^(|([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5}){1,25})+((,\\s)(([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5}){1,25})+)*$';
  govpayAbilitato: boolean;
  tipoConnettore: FormControl = new FormControl('');
  govpayModalita: string = '';
  _option: any = { hasOption: false, hasAllOption: false };
  _all: any = { descrizione: UtilService.TUTTI_TIPI_PENDENZA.label, idTipoPendenza: UtilService.TUTTI_TIPI_PENDENZA.value };
  _tipiPendenza: any[] = (UtilService.PROFILO_UTENTE.tipiPendenza || []);

  _isAllegatoEmail: boolean = false;

  // REST
  _contenuti: SimpleListItem[] = UtilService.CONTENUTI_NOTIFICA_CONNETTORE;
  protected versioni: any[] = UtilService.TIPI_VERSIONE_API;
  protected sslTypeValue: string = '';

  constructor() { }

  ngOnInit() {
    this.fGroup.addControl('govpayAbilitato_ctrl', new FormControl(false));
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
  }

  ngAfterViewInit() {
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
      }
      if (this.json.url) {
        this.fGroup.controls['url_ctrl'].setValue(this.json.url);
      }
      if (this.json.versioneApi) {
        this.fGroup.controls['versioneApi_ctrl'].setValue(this.json.versioneApi);
      }
      this.__bools(this.json.tipiPendenza);
      this._allegatoChange({ checked: this.json.emailAllegato });
      setTimeout(() => {
        this._onChangeGovpay({ checked: this.json.abilitato }, 'govpayAbilitato_ctrl');
        this._onChangeGovpay({ value: this.json.tipoConnettore }, 'tipoConnettore_ctrl');
      });
    }
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
      this.govpayAbilitato = event.checked;
    }
    if (type === 'tipoConnettore_ctrl') {
      this.govpayModalita = event.value;
    }
    this.fGroup.controls['tipoConnettore_ctrl'].clearValidators();
    this.fGroup.controls['tipiPendenza_ctrl'].clearValidators();
    // REST
    this.fGroup.controls['contenuti_ctrl'].clearValidators();
    this.fGroup.controls['url_ctrl'].clearValidators();
    this.fGroup.controls['versioneApi_ctrl'].clearValidators();
    if (this.sslConfig) {
      this.sslConfig.clearValidators();
    }
    // EMAIL
    this.fGroup.controls['emailIndirizzi_ctrl'].clearValidators();
    this.fGroup.controls['downloadBaseUrl_ctrl'].clearValidators();
    // EMAIL, FS
    this.fGroup.controls['versioneZip_ctrl'].clearValidators();
    // FS
    this.fGroup.controls['fileSystemPath_ctrl'].clearValidators();
    if (this.govpayAbilitato) {
      this.fGroup.controls['tipoConnettore_ctrl'].setValidators(Validators.required);
      this.fGroup.controls['tipiPendenza_ctrl'].setValidators(Validators.required);
      // EMAIL
      if (this.govpayModalita === UtilService.CONNETTORE_MODALITA_EMAIL) {
        this.fGroup.controls['emailIndirizzi_ctrl'].setValidators([Validators.required, Validators.pattern(this.pattern)]);
        if (!this._isAllegatoEmail) {
          this.fGroup.controls['downloadBaseUrl_ctrl'].setValidators([Validators.required]);
        }
      }
      // EMAIL, FS
      if (this.govpayAbilitato) {
        if (this.govpayModalita === UtilService.CONNETTORE_MODALITA_EMAIL || this.govpayModalita === UtilService.CONNETTORE_MODALITA_FILESYSTEM) {
          this.fGroup.controls['versioneZip_ctrl'].setValidators(Validators.required);
        }
      }
      // FS
      if (this.govpayModalita === UtilService.CONNETTORE_MODALITA_FILESYSTEM) {
        this.fGroup.controls['fileSystemPath_ctrl'].setValidators(Validators.required);
      }
      // REST
      if (this.govpayModalita === UtilService.CONNETTORE_MODALITA_REST) {
        this.fGroup.controls['contenuti_ctrl'].setValidators(Validators.required);
        this.fGroup.controls['url_ctrl'].setValidators(Validators.required)
        this.fGroup.controls['versioneApi_ctrl'].setValidators(Validators.required);
        if (this.sslConfig) {
          this.sslConfig.setValidatorsRequired();
        }
      }
    }
    this.fGroup.controls['tipoConnettore_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
    this.fGroup.controls['tipiPendenza_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
    this.fGroup.controls['versioneZip_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
    this.fGroup.controls['versioneApi_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
    this.fGroup.controls['emailIndirizzi_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
    this.fGroup.controls['downloadBaseUrl_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
    this.fGroup.controls['fileSystemPath_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
    this.fGroup.controls['contenuti_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
    this.fGroup.controls['url_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });

  }

  protected _allegatoChange(event: any) {
    this._isAllegatoEmail = event.checked || false;
    (event.checked)?this.fGroup.controls['downloadBaseUrl_ctrl'].clearValidators():this.fGroup.controls['downloadBaseUrl_ctrl'].setValidators(Validators.required);
    (event.checked)?this.fGroup.controls['downloadBaseUrl_ctrl'].disable():this.fGroup.controls['downloadBaseUrl_ctrl'].enable();
    this.fGroup.controls['downloadBaseUrl_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
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
        _json.auth = this.sslConfig.mapToJson();
        _json.url = _info['url_ctrl']?_info['url_ctrl']:null;
        _json.versioneApi = _info['versioneApi_ctrl']?_info['versioneApi_ctrl']:null;

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
