import { AfterContentChecked, AfterViewInit, Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { IFormComponent } from '../../../../classes/interfaces/IFormComponent';
import { UtilService } from '../../../../services/util.service';
import { Voce } from '../../../../services/voce.service';

@Component({
  selector: 'link-ssl-config',
  templateUrl: './ssl-config.component.html',
  styles: []
})
export class SslConfigComponent implements IFormComponent, OnInit, OnChanges, AfterContentChecked, AfterViewInit {
  @Input() fGroup: FormGroup;
  @Input() json: any;
  @Input() required: boolean = false;

  protected voce = Voce;
  protected NESSUNA = 'Nessuna';
  protected BASIC = UtilService.TIPI_AUTENTICAZIONE.basic;
  protected SSL = UtilService.TIPI_AUTENTICAZIONE.ssl;
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
    setTimeout(() => {
      this._isRequired = this.required;
    }, 100);
  }

  ngAfterContentChecked() {
  }

  ngAfterViewInit() {
    setTimeout(() => {
      this._isSslAuth = false;
      this._isBasicAuth = false;
      this.fGroup.controls['auth_ctrl'].setValue(this.NESSUNA);
      if (this.json) {
        if (this.json.hasOwnProperty('username')) {
          this._isBasicAuth = true;
          this.fGroup.controls['auth_ctrl'].setValue(this.BASIC);
          this.addBasicControls();
          this.fGroup.controls['username_ctrl'].setValue(this.json.username);
          this.fGroup.controls['password_ctrl'].setValue(this.json.password);
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
    this._isBasicAuth = false;
    this._isSslClient = false;
    this._isSslAuth = false;
    this.removeBasicControls();
    this.removeSslControls();
    switch(target.value) {
      case this.BASIC:
        this._isBasicAuth = true;
        this.addBasicControls();
        break;
      case this.SSL:
        this._isSslAuth = true;
        this.addSslControls();
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

  protected addBasicControls() {
    if (this.fGroup) {
      this.fGroup.addControl('username_ctrl', new FormControl('', (this._isBasicAuth && this.required)?[Validators.required]:[]));
      this.fGroup.addControl('password_ctrl', new FormControl('', (this._isBasicAuth && this.required)?[Validators.required]:[]));
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

  resetSslConfig() {
    this._isSslAuth = false;
    this._isBasicAuth = false;
    this.resetControllers();
  }

  resetControllers() {
    this.authCtrl.setValue(this.NESSUNA);
    this.removeBasicControls();
    this.removeSslControls();
  }

  clearValidators() {
    this.authCtrl.clearValidators();
    if (this.fGroup) {
      const controls: any = this.fGroup.controls;
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
