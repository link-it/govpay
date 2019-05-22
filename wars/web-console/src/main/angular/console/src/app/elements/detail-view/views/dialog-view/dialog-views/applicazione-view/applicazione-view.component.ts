import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UtilService } from '../../../../../../services/util.service';
import { Voce } from '../../../../../../services/voce.service';
import { GovpayService } from '../../../../../../services/govpay.service';

@Component({
  selector: 'link-applicazione-view',
  templateUrl: './applicazione-view.component.html',
  styleUrls: ['./applicazione-view.component.scss']
})
export class ApplicazioneViewComponent implements IFormComponent, OnInit, AfterViewInit {

  @Input() fGroup: FormGroup;
  @Input() json: any;

  protected voce = Voce;
  protected BASIC = UtilService.TIPI_AUTENTICAZIONE.basic;
  protected SSL = UtilService.TIPI_AUTENTICAZIONE.ssl;
  protected CLIENT = UtilService.TIPI_SSL.client;
  protected SERVER = UtilService.TIPI_SSL.server;

  protected versioni: any[] = UtilService.TIPI_VERSIONE_API;
  protected services: any[] = [
    { title: 'API Integrazione', property: 'servizioIntegrazione', basicAuth: false, sslAuth: false, json: null, required: false }
  ];
  protected acl = [];
  protected domini = [];
  protected tipiPendenza = [];

  constructor(public gps: GovpayService, public us: UtilService) { }

  ngOnInit() {
    this.elencoDominiPendenze();

    this.fGroup.addControl('idA2A_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('principal_ctrl', new FormControl(''));
    this.fGroup.addControl('abilita_ctrl', new FormControl(false));
    this.fGroup.addControl('codificaIuv_ctrl', new FormControl(''));
    this.fGroup.addControl('regExpIuv_ctrl', new FormControl(''));
    this.fGroup.addControl('generazioneIuvInterna_ctrl', new FormControl(false));
    this.fGroup.addControl('apiPagamenti_ctrl', new FormControl(false));
    this.fGroup.addControl('apiPendenze_ctrl', new FormControl(false));
    this.fGroup.addControl('apiRagioneria_ctrl', new FormControl(false));
    this.services.forEach((item, index) => {
      this.fGroup.addControl('url_ctrl_'+index, new FormControl('', null));
      this.fGroup.addControl('versioneApi_ctrl_'+index, new FormControl('', null));
      this.fGroup.addControl('auth_ctrl_'+index, new FormControl(''));
      if(item.required) {
        this.fGroup.controls['url_ctrl_'+index].setValidators(Validators.required);
        this.fGroup.controls['url_ctrl_'+index].updateValueAndValidity();
        this.fGroup.controls['versioneApi_ctrl_'+index].setValidators(Validators.required);
        this.fGroup.controls['versioneApi_ctrl_'+index].updateValueAndValidity();
      }
    });
    this.fGroup.addControl('dominio_ctrl', new FormControl(''));
    this.fGroup.addControl('tipoPendenza_ctrl', new FormControl(''));

    if(this.json) {
      this.acl = this.json.acl.slice(0);
    }
    // Mappatura autorizzazioni
    const _sauth = this.acl.map((item) => {
      return item.servizio;
    });
    UtilService.SERVIZI.forEach((_servizio, index) => {
      if(_sauth.indexOf(_servizio) == -1) {
        this.acl.push({ servizio: _servizio, autorizzazioni: [] });
      }
    });
    // Map original ACL labels
    this.acl = this.acl.map((item) => {
      item.mapACL = UtilService.MAP_ACL(item.servizio);
      return item;
    });
    // Sort original ACL
    this.acl.sort((item1, item2) => {
      return (item1.mapACL>item2.mapACL)?1:(item1.mapACL<item2.mapACL)?-1:0;
    });

    this.acl.forEach((item, index) => {
      this.fGroup.addControl('autorizzazioni_ctrl_' + index, new FormControl(''));
    });
  }

  ngAfterViewInit() {
    setTimeout(() => {
      if(this.json) {
        if(this.json.idA2A) {
          this.fGroup.controls['idA2A_ctrl'].disable();
          this.fGroup.controls['idA2A_ctrl'].setValue(this.json.idA2A);
        }
        (this.json.principal)?this.fGroup.controls['principal_ctrl'].setValue(this.json.principal):null;
        (this.json.abilitato)?this.fGroup.controls['abilita_ctrl'].setValue(this.json.abilitato):null;
        (this.json.apiPagamenti)?this.fGroup.controls['apiPagamenti_ctrl'].setValue(this.json.apiPagamenti):null;
        (this.json.apiPendenze)?this.fGroup.controls['apiPendenze_ctrl'].setValue(this.json.apiPendenze):null;
        (this.json.apiRagioneria)?this.fGroup.controls['apiRagioneria_ctrl'].setValue(this.json.apiRagioneria):null;
        if(this.json.codificaAvvisi) {
          (this.json.codificaAvvisi.codificaIuv)?this.fGroup.controls['codificaIuv_ctrl'].setValue(this.json.codificaAvvisi.codificaIuv):null;
          (this.json.codificaAvvisi.regExpIuv)?this.fGroup.controls['regExpIuv_ctrl'].setValue(this.json.codificaAvvisi.regExpIuv):null;
          (this.json.codificaAvvisi.generazioneIuvInterna)?this.fGroup.controls['generazioneIuvInterna_ctrl'].setValue(this.json.codificaAvvisi.generazioneIuvInterna):null;
        }
        if(this.json.servizioIntegrazione) {
          this.services[0].json = this.json.servizioIntegrazione;
        }
        this.services.forEach((item, index) => {
          if(item.json) {
            item.sslAuth = false;
            item.basicAuth = false;
            (item.json.url)?this.fGroup.controls['url_ctrl_' + index].setValue(item.json.url):null;
            (item.json.versioneApi)?this.fGroup.controls['versioneApi_ctrl_' + index].setValue(item.json.versioneApi):null;
            if(item.json.auth) {
              if(item.json.auth.hasOwnProperty('username')){
                item.basicAuth = true;
                this.fGroup.controls['auth_ctrl_' + index].setValue(this.BASIC);
                this.addBasicControls(index);
                (item.json.auth.username)?this.fGroup.controls['username_ctrl_' + index].setValue(item.json.auth.username):null;
                (item.json.auth.password)?this.fGroup.controls['password_ctrl_' + index].setValue(item.json.auth.password):null;
              }
              if(item.json.auth.hasOwnProperty('tipo')){
                item.sslAuth = true;
                this.fGroup.controls['auth_ctrl_' + index].setValue(this.SSL);
                this.addSslControls(index);
                (item.json.auth.tipo)?this.fGroup.controls['ssl_ctrl_' + index].setValue(this.json.auth.tipo):null;
                (item.json.auth.ksLocation)?this.fGroup.controls['ksLocation_ctrl_' + index].setValue(item.json.auth.ksLocation):null;
                (item.json.auth.ksPassword)?this.fGroup.controls['ksPassword_ctrl_' + index].setValue(item.json.auth.ksPassword):null;
                (item.json.auth.tsLocation)?this.fGroup.controls['tsLocation_ctrl_' + index].setValue(item.json.auth.tsLocation):null;
                (item.json.auth.tsPassword)?this.fGroup.controls['tsPassword_ctrl_' + index].setValue(item.json.auth.tsPassword):null;
              }
            } else {
              this.fGroup.controls['auth_ctrl_' + index].setValue('');
            }
          }
        });
        if(this.json.domini) {
          this.fGroup.controls['dominio_ctrl'].setValue(this.json.domini);
        }
        if(this.json.tipiPendenza) {
          this.fGroup.controls[ 'tipoPendenza_ctrl' ].setValue(this.json.tipiPendenza);
        }
        if(this.acl.length != 0) {
          this.acl.forEach((item, index) => {
            this.fGroup.controls['autorizzazioni_ctrl_' + index].setValue(item.autorizzazioni.sort().toString());
          });
        }
      }
    });
  }

  protected elencoDominiPendenze() {
    let _services: string[] = [];
    _services.push(UtilService.URL_DOMINI);
    _services.push(UtilService.URL_TIPI_PENDENZA);
    this.gps.updateSpinner(true);
    this.gps.forkService(_services).subscribe(function (_response) {
        if(_response) {
          this.domini = _response[0].body.risultati;
          this.domini.unshift({ ragioneSociale: UtilService.TUTTI_DOMINI.label, idDominio: UtilService.TUTTI_DOMINI.value });
          this.tipiPendenza = _response[1].body.risultati;
          this.tipiPendenza.unshift({ descrizione: UtilService.TUTTI_TIPI_PENDENZA.label, idTipoPendenza: UtilService.TUTTE_ENTRATE.value });
          this.tipiPendenza.unshift({ descrizione: UtilService.AUTODETERMINAZIONE_TIPI_PENDENZA.label, idTipoPendenza: UtilService.AUTODETERMINAZIONE_TIPI_PENDENZA.value });
          this.gps.updateSpinner(false);
        }
      }.bind(this),
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

  protected dominioCmpFn(d1: any, d2: any): boolean {
    return (d1 && d2)?(d1.idDominio === d2.idDominio):(d1 === d2);
  }

  protected pendenzaCmpFn(p1: any, p2: any): boolean {
    return (p1 && p2)?(p1.idTipoPendenza === p2.idTipoPendenza):(p1 === p2);
  }

  protected _onUrlChange(trigger, target, index) {
    if(!this.services[index].required) {
      let _tc = this.fGroup.controls[target + index];
      _tc.clearValidators();
      if(trigger.value.trim() !== '') {
        _tc.setValidators(Validators.required);
      }
      _tc.updateValueAndValidity();
    }
  }

  protected _onAuthChange(target, index) {
    this.removeBasicControls(index);
    this.removeSslControls(index);
    this.services[index].sslAuth = false;
    this.services[index].basicAuth = false;
    switch(target.value) {
      case this.BASIC:
        this.addBasicControls(index);
        this.services[index].basicAuth = true;
        break;
      case this.SSL:
        this.addSslControls(index);
        this.services[index].sslAuth = true;
        break;
    }
  }

  protected addBasicControls(index: number) {
    this.fGroup.addControl('username_ctrl_' + index, new FormControl('', Validators.required));
    this.fGroup.addControl('password_ctrl_' + index, new FormControl('', Validators.required));
  }

  protected addSslControls(index: number) {
    this.fGroup.addControl('ssl_ctrl_' + index, new FormControl('', Validators.required));
    this.fGroup.addControl('ksLocation_ctrl_' + index, new FormControl('', Validators.required));
    this.fGroup.addControl('ksPassword_ctrl_' + index, new FormControl('', Validators.required));
    this.fGroup.addControl('tsLocation_ctrl_' + index, new FormControl(''));
    this.fGroup.addControl('tsPassword_ctrl_' + index, new FormControl(''));
  }

  protected removeBasicControls(index: number) {
    this.fGroup.removeControl('username_ctrl_' + index);
    this.fGroup.removeControl('password_ctrl_' + index);
  }

  protected removeSslControls(index: number) {
    this.fGroup.removeControl('ssl_ctrl_' + index);
    this.fGroup.removeControl('ksLocation_ctrl_' + index);
    this.fGroup.removeControl('ksPassword_ctrl_' + index);
    this.fGroup.removeControl('tsLocation_ctrl_' + index);
    this.fGroup.removeControl('tsPassword_ctrl_' + index);
  }

  /**
   * Interface IFormComponent: Form controls to json object
   * @returns {any}
   */
  mapToJson(): any {
    let _info = this.fGroup.value;
    let _json:any = {};
    _json.idA2A = (!this.fGroup.controls['idA2A_ctrl'].disabled)?_info['idA2A_ctrl']:this.json.idA2A;
    _json.abilitato = _info['abilita_ctrl'];
    _json.apiPagamenti = _info['apiPagamenti_ctrl'];
    _json.apiPendenze = _info['apiPendenze_ctrl'];
    _json.apiRagioneria = _info['apiRagioneria_ctrl'];
    _json.principal = (_info['principal_ctrl'])?_info['principal_ctrl']:null;
    _json.codificaAvvisi = {
      codificaIuv: (_info['codificaIuv_ctrl'])?_info['codificaIuv_ctrl']:null,
      regExpIuv: (_info['regExpIuv_ctrl'])?_info['regExpIuv_ctrl']:null,
      generazioneIuvInterna: (_info['generazioneIuvInterna_ctrl'])?_info['generazioneIuvInterna_ctrl']:false
    };
    this.services.forEach((item, index) => {
      _json[item.property] = {
        auth: null,
        url: _info['url_ctrl_' + index]?_info['url_ctrl_' + index]:null,
        versioneApi: _info['versioneApi_ctrl_' + index]?_info['versioneApi_ctrl_' + index]:null
      };
      if(_info.hasOwnProperty('username_ctrl_' + index)) {
        _json[item.property].auth = {
          password: _info['username_ctrl_' + index],
          username: _info['password_ctrl_' + index]
        };
      }
      if(_info.hasOwnProperty('ssl_ctrl_' + index)) {
        _json[item.property].auth = {
          tipo: _info['ssl_ctrl_' + index],
          ksLocation: _info['ksLocation_ctrl_' + index],
          ksPassword: _info['ksPassword_ctrl_' + index],
          tsLocation: '',
          tsPassword: ''
        };
        if(_info.hasOwnProperty('tsLocation_ctrl_' + index)) {
          _json[item.property].auth.tsLocation = _info['tsLocation_ctrl_' + index];
        }
        if(_info.hasOwnProperty('tsPassword_ctrl_' + index)) {
          _json[item.property].auth.tsPassword = _info['tsPassword_ctrl_' + index];
        }
      }
      if(_json[item.property].auth == null) { delete _json[item.property].auth; }
      if(_json[item.property].url == null) {
        _json[item.property] = null;
      }
    });
    _json.domini = (_info['dominio_ctrl'])?_info['dominio_ctrl']:[];
    _json.tipiPendenza = (_info['tipoPendenza_ctrl'])?_info['tipoPendenza_ctrl']:[];
    _json.acl = this.acl.map((item, index) => {
      item.autorizzazioni = (_info['autorizzazioni_ctrl_' + index])?_info['autorizzazioni_ctrl_' + index].split(','):[];
      delete item.mapACL;
      return item;
    }).filter(item => {
      return item.autorizzazioni.length != 0;
    });

    return _json;
  }
}
