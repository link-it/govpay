import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UtilService } from '../../../../../../services/util.service';
import { Voce } from '../../../../../../services/voce.service';
import { GovpayService } from '../../../../../../services/govpay.service';
import { Standard } from '../../../../../../classes/view/standard';
import { Dato } from '../../../../../../classes/view/dato';
import { Parameters } from '../../../../../../classes/parameters';
import { ModalBehavior } from '../../../../../../classes/modal-behavior';
import { IModalDialog } from '../../../../../../classes/interfaces/IModalDialog';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

@Component({
  selector: 'link-applicazione-view',
  templateUrl: './applicazione-view.component.html',
  styleUrls: ['./applicazione-view.component.scss']
})
export class ApplicazioneViewComponent implements IModalDialog, IFormComponent, OnInit, AfterViewInit {

  @Input() fGroup: FormGroup;
  @Input() json: any;
  @Input() modified: boolean = false;

  protected voce = Voce;
  protected BASIC = UtilService.TIPI_AUTENTICAZIONE.basic;
  protected SSL = UtilService.TIPI_AUTENTICAZIONE.ssl;
  protected CLIENT = UtilService.TIPI_SSL.client;
  protected SERVER = UtilService.TIPI_SSL.server;

  protected versioni: any[] = UtilService.TIPI_VERSIONE_API;

  protected basicAuth: boolean = false;
  protected sslAuth: boolean = false;
  protected ruoli = [];
  protected domini = [];
  protected tipiPendenza = [];

  constructor(public gps: GovpayService, public us: UtilService) { }

  ngOnInit() {
    this.elencoDominiPendenzeRuoli();

    this.fGroup.addControl('idA2A_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('principal_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('abilita_ctrl', new FormControl(false));
    this.fGroup.addControl('codificaIuv_ctrl', new FormControl(''));
    this.fGroup.addControl('regExpIuv_ctrl', new FormControl(''));
    this.fGroup.addControl('generazioneIuvInterna_ctrl', new FormControl(false));
    this.fGroup.addControl('apiPagamenti_ctrl', new FormControl(false));
    this.fGroup.addControl('apiPendenze_ctrl', new FormControl(false));
    this.fGroup.addControl('apiRagioneria_ctrl', new FormControl(false));

    this.fGroup.addControl('url_ctrl', new FormControl(''));
    this.fGroup.addControl('versioneApi_ctrl', new FormControl({ value: '', disabled: true }, null));
    this.fGroup.addControl('auth_ctrl', new FormControl({ value: '', disabled: true }));

// this.fGroup.addControl('dominio_ctrl', new FormControl(''));
    this.fGroup.addControl('tipoPendenza_ctrl', new FormControl(''));
    this.fGroup.addControl('ruoli_ctrl', new FormControl(''));
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
          const item = this.json.servizioIntegrazione;

          this.sslAuth = false;
          this.basicAuth = false;
          if (item.url) {
            this.fGroup.controls['url_ctrl'].setValue(item.url);
            this.fGroup.controls['versioneApi_ctrl'].enable();
            this.fGroup.controls['auth_ctrl'].enable();
          }
          if (item.versioneApi) {
            this.fGroup.controls['versioneApi_ctrl'].setValue(item.versioneApi);
          }
          if(item.auth) {
            if(item.auth.hasOwnProperty('username')){
              this.basicAuth = true;
              this.fGroup.controls['auth_ctrl'].setValue(this.BASIC);
              this.addBasicControls();
              (item.auth.username)?this.fGroup.controls['username_ctrl'].setValue(item.auth.username):null;
              (item.auth.password)?this.fGroup.controls['password_ctrl'].setValue(item.auth.password):null;
            }
            if(item.auth.hasOwnProperty('tipo')){
              this.sslAuth = true;
              this.fGroup.controls['auth_ctrl'].setValue(this.SSL);
              this.addSslControls();
              (item.auth.tipo)?this.fGroup.controls['ssl_ctrl'].setValue(item.auth.tipo):null;
              (item.auth.ksLocation)?this.fGroup.controls['ksLocation_ctrl'].setValue(item.auth.ksLocation):null;
              (item.auth.ksPassword)?this.fGroup.controls['ksPassword_ctrl'].setValue(item.auth.ksPassword):null;
              (item.auth.tsLocation)?this.fGroup.controls['tsLocation_ctrl'].setValue(item.auth.tsLocation):null;
              (item.auth.tsPassword)?this.fGroup.controls['tsPassword_ctrl'].setValue(item.auth.tsPassword):null;
            }
          } else {
            this.fGroup.controls['auth_ctrl'].setValue('');
          }
        }
  /*if(this.json.domini) {
    this.fGroup.controls['dominio_ctrl'].setValue(this.json.domini);
  }*/
        if(this.json.tipiPendenza) {
          this.fGroup.controls[ 'tipoPendenza_ctrl' ].setValue(this.json.tipiPendenza);
        }
        if(this.json.ruoli) {
          this.fGroup.controls[ 'ruoli_ctrl' ].setValue(this.json.ruoli);
        }
      }
    });
  }

  protected elencoDominiPendenzeRuoli() {
    let _services: string[] = [];
    _services.push(UtilService.URL_DOMINI);
    _services.push(UtilService.URL_TIPI_PENDENZA);
    _services.push(UtilService.URL_RUOLI);
    this.gps.updateSpinner(true);
    this.gps.forkService(_services).subscribe(function (_response) {
        if(_response) {
// this.domini = _response[0].body.risultati;
// this.domini.unshift({ ragioneSociale: UtilService.TUTTI_DOMINI.label, idDominio: UtilService.TUTTI_DOMINI.value });
          this.domini = (this.json)?this.elencoDominiMap(this.json.domini || []):[];
          this.tipiPendenza = _response[1].body.risultati;
          this.tipiPendenza.unshift({ descrizione: UtilService.TUTTI_TIPI_PENDENZA.label, idTipoPendenza: UtilService.TUTTE_ENTRATE.value });
          this.tipiPendenza.unshift({ descrizione: UtilService.AUTODETERMINAZIONE_TIPI_PENDENZA.label, idTipoPendenza: UtilService.AUTODETERMINAZIONE_TIPI_PENDENZA.value });
          this.ruoli = _response[2].body.risultati;
          this.gps.updateSpinner(false);
        }
      }.bind(this),
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

  protected elencoDominiMap(data: any[]) {
    return data.map(function(item) {
      let p = new Parameters();
      p.jsonP = item;
      p.model = this.mapNewItem(item);
      return p;
    }, this);
  }

  /**
   * Map item Dominio
   * @param item
   * @returns {Standard}
   */
  protected mapNewItem(item: any): Standard {
    let _values = (item.unitaOperative || []).map(uo => {
      return uo.ragioneSociale;
    });
    let _std = new Standard();
    let _st = new Dato({
      label: `${Voce.UNITA_OPERATIVE}: `
    });
    if (_values.length !== 0) {
      _st.value = _values.join(', ');
    } else {
      _st.value = (item.idDominio === '*')?Voce.TUTTE:Voce.NESSUNA
    }
    _std.titolo = new Dato({ label: item.ragioneSociale });
    _std.sottotitolo = _st;

    return  _std;
  }

  /**
   * Add (Enti,Unità operative)
   * @param {boolean} mode
   * @param _viewModel
   * @private
   */
  protected _addEnteUO(mode: boolean = false, _viewModel?: any) {
    let _mb: ModalBehavior = new ModalBehavior();
    _mb.editMode = mode;
    _mb.info = {
      viewModel: _viewModel,
      parent: this,
      dialogTitle: 'Nuova autorizzazione',
      templateName: UtilService.AUTORIZZAZIONE_ENTE_UO
    };
    _mb.closure = this.refresh.bind(this);
    UtilService.dialogBehavior.next(_mb);
  }

  protected _iconClick(ref: any, event: any) {
    let _ivm = ref.getItemViewModel();
    switch(event.type) {
      case 'delete':
        this.domini = this.domini.filter(d => {
          return d.jsonP.idDominio !== _ivm.jsonP.idDominio;
        });
        break;
    }
  }

  refresh(mb: ModalBehavior) {
    this.modified = false;
    if(mb && mb.info && mb.info.viewModel) {
      this.modified = true;
      let p = new Parameters();
      let json = mb.info.viewModel;
      const _newItem = {
        idDominio: json.dominio.idDominio,
        ragioneSociale: json.dominio.ragioneSociale,
        unitaOperative: json.unitaOperative || []
      };
      p.jsonP = _newItem;
      p.model = this.mapNewItem(_newItem);
      (_newItem.idDominio === '*')?this.domini = [p]:this.domini.push(p);
      if (this.domini.length > 1) {
        this.domini = this.domini.filter(el => {
          return el.jsonP.idDominio !== '*';
        });
      }
    }
  }

  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {}

  /*protected dominioCmpFn(d1: any, d2: any): boolean {
    return (d1 && d2)?(d1.idDominio === d2.idDominio):(d1 === d2);
  }*/

  protected pendenzaCmpFn(p1: any, p2: any): boolean {
    return (p1 && p2)?(p1.idTipoPendenza === p2.idTipoPendenza):(p1 === p2);
  }

  protected ruoliCmpFn(p1: any, p2: any): boolean {
    return (p1 && p2)?(p1.id === p2.id):(p1 === p2);
  }

  protected _onUrlChange(trigger: any, targets: string) {
    targets.split('|').forEach((_target, _ti) => {
      const _tc = this.fGroup.controls[_target];
      _tc.clearValidators();
      if(trigger.value.trim() !== '') {
        _tc.enable();
        _tc.setValidators((_ti==0)?Validators.required:null);
      } else {
        _tc.setValue('');
        _tc.disable({ onlySelf: true });
        this.sslAuth = false;
        this.basicAuth = false;
        this.removeBasicControls();
        this.removeSslControls();
      }
      _tc.updateValueAndValidity();
    });
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
        this.addSslControls();
        this.sslAuth = true;
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

      _json.servizioIntegrazione = {
        auth: null,
        url: _info['url_ctrl']?_info['url_ctrl']:null,
        versioneApi: _info['versioneApi_ctrl']?_info['versioneApi_ctrl']:null
      };
      if(_info.hasOwnProperty('username_ctrl')) {
        _json.servizioIntegrazione.auth = {
          password: _info['username_ctrl'],
          username: _info['password_ctrl']
        };
      }
      if(_info.hasOwnProperty('ssl_ctrl')) {
        _json.servizioIntegrazione.auth = {
          tipo: _info['ssl_ctrl'],
          ksLocation: _info['ksLocation_ctrl'],
          ksPassword: _info['ksPassword_ctrl'],
          tsLocation: '',
          tsPassword: ''
        };
        if(_info.hasOwnProperty('tsLocation_ctrl')) {
          _json.servizioIntegrazione.auth.tsLocation = _info['tsLocation_ctrl'];
        }
        if(_info.hasOwnProperty('tsPassword_ctrl')) {
          _json.servizioIntegrazione.auth.tsPassword = _info['tsPassword_ctrl'];
        }
      }
      if(_json.servizioIntegrazione.auth == null) { delete _json.servizioIntegrazione.auth; }
      if(_json.servizioIntegrazione.url == null) {
        _json.servizioIntegrazione = null;
      }

    //_json.domini = (_info['dominio_ctrl'])?_info['dominio_ctrl']:[];
    _json.domini = this.domini || [];
    _json.tipiPendenza = (_info['tipoPendenza_ctrl'])?_info['tipoPendenza_ctrl']:[];
    _json.ruoli = (_info['ruoli_ctrl'])?_info['ruoli_ctrl']:[];
    _json.acl = this.json?(this.json.acl || []):[];

    return _json;
  }
}
