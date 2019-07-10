import { AfterContentChecked, AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UtilService } from '../../../../../../services/util.service';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { Voce } from '../../../../../../services/voce.service';


@Component({
  selector: 'link-intermediario-view',
  templateUrl: './intermediario-view.component.html',
  styleUrls: ['./intermediario-view.component.scss']
})
export class IntermediarioViewComponent  implements IFormComponent, OnInit, AfterViewInit, AfterContentChecked {

  @Input() fGroup: FormGroup;
  @Input() json: any;

  protected _us = UtilService;

  protected BASIC = UtilService.TIPI_AUTENTICAZIONE.basic;
  protected SSL = UtilService.TIPI_AUTENTICAZIONE.ssl;
  protected CLIENT = UtilService.TIPI_SSL.client;
  protected SERVER = UtilService.TIPI_SSL.server;

  protected ph: any = { _RPT: Voce.SERVIZIO_RPT, _AVVISATURA: Voce.SERVIZIO_AVVISATURA };

  // protected versioni: any[] = UtilService.TIPI_VERSIONE_API;
  protected _isBasicAuth: boolean = false;
  protected _isSslAuth: boolean = false;
  protected _isFtpRequired: boolean = false;

  constructor() { }

  ngOnInit() {
    this.fGroup.addControl('denominazione_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('idIntermediario_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('principalPagoPa_ctrl', new FormControl(''));
    this.fGroup.addControl('abilita_ctrl', new FormControl());
    // Connettore SOAP: servizioPagoPa
    this.fGroup.addControl('urlRPT_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('urlAvvisatura_ctrl', new FormControl(''));
    // this.fGroup.addControl('versioneApi_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('auth_ctrl', new FormControl(''));
    // Connettore SFTP: servizioFtp - Lettura
    this.fGroup.addControl('hostnameL_ctrl', new FormControl(''));
    this.fGroup.addControl('portaL_ctrl', new FormControl(''));
    this.fGroup.addControl('usernameL_ctrl', new FormControl(''));
    this.fGroup.addControl('passwordL_ctrl', new FormControl(''));
    // Connettore SFTP: servizioFtp - Scrittura
    this.fGroup.addControl('hostnameS_ctrl', new FormControl(''));
    this.fGroup.addControl('portaS_ctrl', new FormControl(''));
    this.fGroup.addControl('usernameS_ctrl', new FormControl(''));
    this.fGroup.addControl('passwordS_ctrl', new FormControl(''));
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
        if(this.json.servizioPagoPa) {
          const _rpt = this.json.servizioPagoPa;
          this.fGroup.controls['urlRPT_ctrl'].setValue(_rpt.urlRPT?_rpt.urlRPT:'');
          this.fGroup.controls['urlAvvisatura_ctrl'].setValue(_rpt.urlAvvisatura && UtilService.TEMPORARY_DEPRECATED_CODE?_rpt.urlAvvisatura:'');
          // this.fGroup.controls['versioneApi_ctrl'].setValue(this.json.servizioPagoPa.versioneApi);
          if(_rpt.auth) {
            let _sppaa = _rpt.auth;
            if(_sppaa.hasOwnProperty('username')) {
              this.addBasicControls();
              this.fGroup.controls['auth_ctrl'].setValue(this.BASIC);
              this.fGroup.controls['username_ctrl'].setValue((_sppaa.username)?_sppaa.username:'');
              this.fGroup.controls['password_ctrl'].setValue((_sppaa.password)?_sppaa.password:'');
              this._isBasicAuth = true;
            }
            if(_sppaa.hasOwnProperty('tipo')) {
              this.fGroup.controls['auth_ctrl'].setValue(this.SSL);
              this.addSslControls();
              this.fGroup.controls['ssl_ctrl'].setValue((_sppaa.tipo)?_sppaa.tipo:'');
              this.fGroup.controls['ksLocation_ctrl'].setValue((_sppaa.ksLocation)?_sppaa.ksLocation:'');
              this.fGroup.controls['ksPassword_ctrl'].setValue((_sppaa.ksPassword)?_sppaa.ksPassword:'');
              this.fGroup.controls['tsLocation_ctrl'].setValue((_sppaa.tsLocation)?_sppaa.tsLocation:'');
              this.fGroup.controls['tsPassword_ctrl'].setValue((_sppaa.tsPassword)?_sppaa.tsPassword:'');
              this._isSslAuth = true;
            }
          }
        }
        if(this.json.servizioFtp) {
          if(this.json.servizioFtp.ftp_lettura) {
            const _sftpL = this.json.servizioFtp.ftp_lettura;
            this.fGroup.controls['hostnameL_ctrl'].setValue((_sftpL.host)?_sftpL.host:'');
            this.fGroup.controls['portaL_ctrl'].setValue((_sftpL.porta)?_sftpL.porta:'');
            this.fGroup.controls['usernameL_ctrl'].setValue((_sftpL.username)?_sftpL.username:'');
            this.fGroup.controls['passwordL_ctrl'].setValue((_sftpL.password)?_sftpL.password:'');
          }
          if(this.json.servizioFtp.ftp_scrittura) {
            const _sftpS = this.json.servizioFtp.ftp_scrittura;
            this.fGroup.controls['hostnameS_ctrl'].setValue((_sftpS.host)?_sftpS.host:'');
            this.fGroup.controls['portaS_ctrl'].setValue((_sftpS.porta)?_sftpS.porta:'');
            this.fGroup.controls['usernameS_ctrl'].setValue((_sftpS.username)?_sftpS.username:'');
            this.fGroup.controls['passwordS_ctrl'].setValue((_sftpS.password)?_sftpS.password:'');
          }
        }
      }
    });
  }

  ngAfterContentChecked() {
    this._isFtpRequired = this._checkRequiredSFTP();
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

  protected addBasicControls() {
    this.fGroup.addControl('username_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('password_ctrl', new FormControl('', Validators.required));
  }

  protected addSslControls() {
    this.fGroup.addControl('ssl_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('ksLocation_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('ksPassword_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('tsLocation_ctrl', new FormControl(''));
    this.fGroup.addControl('tsPassword_ctrl', new FormControl(''));
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

  _checkRequiredSFTP(): boolean {
    let _required: boolean = false;
    this.fGroup.controls['hostnameL_ctrl'].setValidators(null);
    this.fGroup.controls['portaL_ctrl'].setValidators(null);
    this.fGroup.controls['usernameL_ctrl'].setValidators(null);
    this.fGroup.controls['passwordL_ctrl'].setValidators(null);
    this.fGroup.controls['hostnameS_ctrl'].setValidators(null);
    this.fGroup.controls['portaS_ctrl'].setValidators(null);
    this.fGroup.controls['usernameS_ctrl'].setValidators(null);
    this.fGroup.controls['passwordS_ctrl'].setValidators(null);
    if(this.fGroup.controls['hostnameL_ctrl'].value || this.fGroup.controls['portaL_ctrl'].value ||
      this.fGroup.controls['usernameL_ctrl'].value || this.fGroup.controls['passwordL_ctrl'].value ||
      this.fGroup.controls['hostnameS_ctrl'].value || this.fGroup.controls['portaS_ctrl'].value ||
      this.fGroup.controls['usernameS_ctrl'].value || this.fGroup.controls['passwordS_ctrl'].value) {
      this.fGroup.controls['hostnameL_ctrl'].setValidators(Validators.required);
      this.fGroup.controls['portaL_ctrl'].setValidators(Validators.required);
      this.fGroup.controls['usernameL_ctrl'].setValidators(Validators.required);
      this.fGroup.controls['passwordL_ctrl'].setValidators(Validators.required);
      this.fGroup.controls['hostnameS_ctrl'].setValidators(Validators.required);
      this.fGroup.controls['portaS_ctrl'].setValidators(Validators.required);
      this.fGroup.controls['usernameS_ctrl'].setValidators(Validators.required);
      this.fGroup.controls['passwordS_ctrl'].setValidators(Validators.required);
      _required = true;
    }
    return _required;
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
        tipo: _info['ssl_ctrl'],
        ksLocation: _info['ksLocation_ctrl'],
        ksPassword: _info['ksPassword_ctrl'],
        tsLocation: '',
        tsPassword: ''
      };
      if(_info.hasOwnProperty('tsLocation_ctrl')) {
        _json.servizioPagoPa['auth'].tsLocation = _info['tsLocation_ctrl'];
      }
      if(_info.hasOwnProperty('tsPassword_ctrl')) {
        _json.servizioPagoPa['auth'].tsPassword = _info['tsPassword_ctrl'];
      }
    }
    if(_json.servizioPagoPa.auth == null) { delete _json.servizioPagoPa.auth; }
    if(_json.servizioPagoPa.urlAvvisatura == null) { delete _json.servizioPagoPa.urlAvvisatura; }

    // Connettore SFTP: servizioFtp - All fields required
    _json.servizioFtp = null;
    if(_info['hostnameL_ctrl'] && _info['hostnameS_ctrl']) {
      _json.servizioFtp = {};
      _json.servizioFtp.ftp_lettura = {};
      _json.servizioFtp.ftp_lettura = {
        host: _info['hostnameL_ctrl'],
        porta: _info['portaL_ctrl'],
        username: _info['usernameL_ctrl'],
        password: _info['passwordL_ctrl'],
      };
      _json.servizioFtp.ftp_scrittura = {};
      _json.servizioFtp.ftp_scrittura = {
        host: _info['hostnameS_ctrl'],
        porta: _info['portaS_ctrl'],
        username: _info['usernameS_ctrl'],
        password: _info['passwordS_ctrl'],
      };
    }
    if(_json.servizioFtp == null) { delete _json.servizioFtp; }

    return _json;
  }
}
