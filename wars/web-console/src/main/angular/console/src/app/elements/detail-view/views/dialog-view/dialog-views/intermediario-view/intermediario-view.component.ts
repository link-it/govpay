import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UtilService } from '../../../../../../services/util.service';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { Voce } from '../../../../../../services/voce.service';


@Component({
  selector: 'link-intermediario-view',
  templateUrl: './intermediario-view.component.html',
  styleUrls: ['./intermediario-view.component.scss']
})
export class IntermediarioViewComponent  implements IFormComponent, OnInit, AfterViewInit {

  @Input() fGroup: FormGroup;
  @Input() json: any;

  protected _us = UtilService;

  protected BASIC = UtilService.TIPI_AUTENTICAZIONE.basic;
  protected SSL = UtilService.TIPI_AUTENTICAZIONE.ssl;
  protected CLIENT = UtilService.TIPI_SSL.client;
  protected SERVER = UtilService.TIPI_SSL.server;

  voce = Voce;

  // protected versioni: any[] = UtilService.TIPI_VERSIONE_API;
  protected _isBasicAuth: boolean = false;
  protected _isSslAuth: boolean = false;
  protected _isSslClient: boolean = false;
  protected _isFtpRequired: boolean = false;

  constructor() { }

  ngOnInit() {
    this.fGroup.addControl('denominazione_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('idIntermediario_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('abilita_ctrl', new FormControl());
    // Connettore SOAP: servizioPagoPa
    this.fGroup.addControl('principalPagoPa_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('urlRPT_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('urlAvvisatura_ctrl', new FormControl(''));
    // this.fGroup.addControl('versioneApi_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('auth_ctrl', new FormControl(''));
  }

  ngAfterViewInit() {
    setTimeout(() => {
      if(this.json) {
        this.fGroup.controls['idIntermediario_ctrl'].disable();
        this.fGroup.controls['idIntermediario_ctrl'].setValue(this.json.idIntermediario);
        this.fGroup.controls['denominazione_ctrl'].setValue(this.json.denominazione);
        this.fGroup.controls['principalPagoPa_ctrl'].setValue(this.json.principalPagoPa);
        this.fGroup.controls['abilita_ctrl'].setValue(this.json.abilitato);
        this.fGroup.controls['auth_ctrl'].setValue('');
        if (this.json.servizioPagoPa) {
          const _rpt = this.json.servizioPagoPa;
          this.fGroup.controls['urlRPT_ctrl'].setValue(_rpt.urlRPT?_rpt.urlRPT:'');
          this.fGroup.controls['urlAvvisatura_ctrl'].setValue(_rpt.urlAvvisatura && UtilService.TEMPORARY_DEPRECATED_CODE?_rpt.urlAvvisatura:'');
          // this.fGroup.controls['versioneApi_ctrl'].setValue(this.json.servizioPagoPa.versioneApi);
          if(_rpt.auth) {
            let _sppaa = _rpt.auth;
            if (_sppaa.hasOwnProperty('username')) {
              this.addBasicControls();
              this.fGroup.controls['auth_ctrl'].setValue(this.BASIC);
              this.fGroup.controls['username_ctrl'].setValue((_sppaa.username)?_sppaa.username:'');
              this.fGroup.controls['password_ctrl'].setValue((_sppaa.password)?_sppaa.password:'');
              this._isBasicAuth = true;
            }
            if (_sppaa.hasOwnProperty('tipo')) {
              this.fGroup.controls['auth_ctrl'].setValue(this.SSL);
              this.addSslControls();
              this.fGroup.controls['ssl_ctrl'].setValue(_sppaa.tipo === this.CLIENT);
              this.fGroup.controls['tsLocation_ctrl'].setValue((_sppaa.tsLocation)?_sppaa.tsLocation:'');
              this.fGroup.controls['tsPassword_ctrl'].setValue((_sppaa.tsPassword)?_sppaa.tsPassword:'');
              this._isSslAuth = true;
              if (_sppaa.tipo === this.CLIENT) {
                this.addSslClientControls();
                this.fGroup.controls['ksLocation_ctrl'].setValue((_sppaa.ksLocation)?_sppaa.ksLocation:'');
                this.fGroup.controls['ksPassword_ctrl'].setValue((_sppaa.ksPassword)?_sppaa.ksPassword:'');
                this._isSslClient = true;
              }
            }
          }
        }
      }
    });
  }

  protected _onAuthChange(target) {
    this._isBasicAuth = false;
    this._isSslAuth = false;
    this.removeBasicControls();
    this.removeSslControls();
    switch(target.value) {
      case this.BASIC:
        this.addBasicControls();
        this._isBasicAuth = true;
        break;
      case this.SSL:
        this.addSslControls();
        this._isSslAuth = true;
        break;
    }
  }

  protected _onTypeChange(target) {
    this._isSslClient = false;
    this.removeSslClientControls();
    if (target.checked === true) {
      this._isSslClient = true;
      this.addSslClientControls();
    }
  }

  protected addBasicControls() {
    this.fGroup.addControl('username_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('password_ctrl', new FormControl('', Validators.required));
  }

  protected addSslControls() {
    this.fGroup.addControl('ssl_ctrl', new FormControl(false, Validators.required));
    this.fGroup.addControl('tsLocation_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('tsPassword_ctrl', new FormControl('', Validators.required));
  }

  protected addSslClientControls() {
    this.fGroup.addControl('ksLocation_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('ksPassword_ctrl', new FormControl('', Validators.required));
  }

  protected removeSslClientControls() {
    this.fGroup.removeControl('ksLocation_ctrl');
    this.fGroup.removeControl('ksPassword_ctrl');
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

  /**
   * Interface IFormComponent: Form controls to json object
   * @returns {any}
   */
  mapToJson(): any {
    let _info = this.fGroup.value;
    let _json:any = {};
    _json.idIntermediario = (!this.fGroup.controls['idIntermediario_ctrl'].disabled)?_info['idIntermediario_ctrl']:this.json.idIntermediario;
    _json.abilitato = _info['abilita_ctrl'] || false;
    _json.denominazione = (_info['denominazione_ctrl'])?_info['denominazione_ctrl']:null;
    _json.principalPagoPa = (_info['principalPagoPa_ctrl'])?_info['principalPagoPa_ctrl']:null;
    _json.servizioPagoPa = {
      auth: null,
      urlRPT: _info['urlRPT_ctrl'],
      urlAvvisatura: _info['urlAvvisatura_ctrl']?_info['urlAvvisatura_ctrl']:null,
      // versioneApi: _info['versioneApi_ctrl']
    };
    if(_info.hasOwnProperty('username_ctrl')) {
      _json.servizioPagoPa['auth'] = {
        username: _info['username_ctrl'],
        password: _info['password_ctrl']
      };
    }
    if(_info.hasOwnProperty('ssl_ctrl')) {
      _json.servizioPagoPa['auth'] = {
        tipo: _info['ssl_ctrl']?this.CLIENT:this.SERVER,
        ksLocation: '',
        ksPassword: '',
        tsLocation: _info['tsLocation_ctrl'],
        tsPassword: _info['tsPassword_ctrl']
      };
      if(_info['ssl_ctrl'] === true) {
        _json.servizioPagoPa['auth'].ksLocation = _info['ksLocation_ctrl'];
        _json.servizioPagoPa['auth'].ksPassword = _info['ksPassword_ctrl'];
      } else {
        delete _json.servizioPagoPa.auth.ksLocation;
        delete _json.servizioPagoPa.auth.ksPassword;
      }
    }
    if(_json.servizioPagoPa.auth == null) { delete _json.servizioPagoPa.auth; }
    if(_json.servizioPagoPa.urlAvvisatura == null) { delete _json.servizioPagoPa.urlAvvisatura; }

    return _json;
  }
}
