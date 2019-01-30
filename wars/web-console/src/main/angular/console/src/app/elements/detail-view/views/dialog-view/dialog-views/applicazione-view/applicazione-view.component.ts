import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UtilService } from '../../../../../../services/util.service';

@Component({
  selector: 'link-applicazione-view',
  templateUrl: './applicazione-view.component.html',
  styleUrls: ['./applicazione-view.component.scss']
})
export class ApplicazioneViewComponent implements IFormComponent, OnInit, AfterViewInit {

  @Input() fGroup: FormGroup;
  @Input() json: any;

  protected BASIC = UtilService.TIPI_AUTENTICAZIONE.basic;
  protected SSL = UtilService.TIPI_AUTENTICAZIONE.ssl;
  protected CLIENT = UtilService.TIPI_SSL.client;
  protected SERVER = UtilService.TIPI_SSL.server;

  protected versioni: any[] = UtilService.TIPI_VERSIONE_API;
  protected services: any[] = [
    { title: 'Servizio verifica', property: 'servizioVerifica', basicAuth: false, sslAuth: false, json: null, required: false },
    { title: 'Servizio notifica', property: 'servizioNotifica', basicAuth: false, sslAuth: false, json: null, required: false }
  ];

  constructor() { }

  ngOnInit() {
    this.fGroup.addControl('idA2A_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('principal_ctrl', new FormControl(''));
    this.fGroup.addControl('abilita_ctrl', new FormControl(false));
    this.fGroup.addControl('codificaIuv_ctrl', new FormControl(''));
    this.fGroup.addControl('regExpIuv_ctrl', new FormControl(''));
    this.fGroup.addControl('generazioneIuvInterna_ctrl', new FormControl(false));
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
        if(this.json.codificaAvvisi) {
          (this.json.codificaAvvisi.codificaIuv)?this.fGroup.controls['codificaIuv_ctrl'].setValue(this.json.codificaAvvisi.codificaIuv):null;
          (this.json.codificaAvvisi.regExpIuv)?this.fGroup.controls['regExpIuv_ctrl'].setValue(this.json.codificaAvvisi.regExpIuv):null;
          (this.json.codificaAvvisi.generazioneIuvInterna)?this.fGroup.controls['generazioneIuvInterna_ctrl'].setValue(this.json.codificaAvvisi.generazioneIuvInterna):null;
        }
        if(this.json.servizioVerifica) {
          this.services[0].json = this.json.servizioVerifica;
        }
        if(this.json.servizioNotifica) {
          this.services[1].json = this.json.servizioNotifica;
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
      }
    });
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
    _json.domini = (this.fGroup.controls['idA2A_ctrl'].disabled)?this.json.domini:[];
    _json.entrate = (this.fGroup.controls['idA2A_ctrl'].disabled)?this.json.entrate:[];
    _json.acl = (this.fGroup.controls['idA2A_ctrl'].disabled)?this.json.acl:[];

    return _json;
  }
}
