import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { IModalDialog } from '../../../../classes/interfaces/IModalDialog';
import { GovpayService } from '../../../../services/govpay.service';
import { UtilService } from '../../../../services/util.service';
import { Voce } from "../../../../services/voce.service";
import { Dato } from '../../../../classes/view/dato';
import { Standard } from '../../../../classes/view/standard';
import { Parameters } from '../../../../classes/parameters';
import { ModalBehavior } from '../../../../classes/modal-behavior';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { FormInput } from '../../../../classes/view/form-input';

@Component({
  selector: 'link-applicazioni-view',
  templateUrl: './applicazioni-view.component.html',
  styleUrls: ['./applicazioni-view.component.scss']
})
export class ApplicazioniViewComponent implements IModalDialog, OnInit, AfterViewInit{

  @Input() domini = [];
  @Input() entrate = [];
  @Input() acls = [];
  @Input() informazioni = [];
  @Input() avvisi = [];
  @Input() verifiche = [];
  @Input() notifiche = [];

  @Input() json: any;
  @Input() modified: boolean = false;

  protected DOMINIO = UtilService.DOMINIO;
  protected ENTRATA = UtilService.ENTRATA;
  protected ACL = UtilService.ACL;
  protected ADD = UtilService.PATCH_METHODS.ADD;
  protected _OPERAZIONI = Voce.OPERAZIONI;

  constructor(public gps: GovpayService, public us: UtilService) { }

  ngOnInit() {
    this.dettaglioApplicazione();
    //this.elencoDominiEntrate();
  }

  ngAfterViewInit() {
  }

  protected dettaglioApplicazione() {
    let _url = UtilService.URL_APPLICAZIONI+'/'+encodeURIComponent(this.json.idA2A);
    this.gps.getDataService(_url).subscribe(
      function (_response) {
        this.json = _response.body;
        this.mapJsonDetail();
        this.gps.updateSpinner(false);
      }.bind(this),
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

  protected mapJsonDetail() {
    let _dettaglio = { info: [], avviso: [], verifica: [], notifica: [] };
    _dettaglio.info.push(new Dato({ label: Voce.PRINCIPAL, value: this.json.principal }));
    _dettaglio.info.push(new Dato({ label: Voce.ID_A2A, value: this.json.idA2A }));
    _dettaglio.info.push(new Dato({ label: Voce.ABILITATO, value: UtilService.ABILITA[this.json.abilitato.toString()] }));
    if(this.json.codificaAvvisi) {
      _dettaglio.avviso.push(new Dato({ label: Voce.IUV_CODEC, value: this.json.codificaAvvisi.codificaIuv }));
      _dettaglio.avviso.push(new Dato({ label: Voce.IUV_REGEX, value: this.json.codificaAvvisi.regExpIuv }));
      _dettaglio.avviso.push(new Dato({ label: Voce.IUV_GENERATION, value: UtilService.ABILITA[this.json.codificaAvvisi.generazioneIuvInterna.toString()] }));
    }
    if(this.json.servizioVerifica) {
      _dettaglio.verifica.push(new Dato({ label: Voce.URL, value: this.json.servizioVerifica.url }));
      _dettaglio.verifica.push(new Dato({ label: Voce.VERSIONE_API, value: this.json.servizioVerifica.versioneApi }));
      if(this.json.servizioVerifica.auth) {
        _dettaglio.verifica.push(new Dato({ label: Voce.TIPO_AUTH, value: '' }));
        if(this.json.servizioVerifica.auth.username) {
          _dettaglio.verifica.push(new Dato({label: Voce.USERNAME, value: this.json.servizioVerifica.auth.username }));
          _dettaglio.verifica.push(new Dato({label: Voce.PASSWORD, value: this.json.servizioVerifica.auth.password }));
        }
        if(this.json.servizioVerifica.auth.tipo) {
          _dettaglio.verifica.push(new Dato({label: Voce.TIPO, value: this.json.servizioVerifica.auth.tipo }));
          _dettaglio.verifica.push(new Dato({label: Voce.KEY_STORE_LOC, value: this.json.servizioVerifica.auth.ksLocation }));
          _dettaglio.verifica.push(new Dato({label: Voce.KEY_STORE_PWD, value: this.json.servizioVerifica.auth.ksPassword }));
          if(this.json.servizioVerifica.auth.tsLocation) {
            _dettaglio.verifica.push(new Dato({label: Voce.TRUST_STORE_LOC, value: this.json.servizioVerifica.auth.tsLocation }));
          }
          if(this.json.servizioVerifica.auth.tsPassword) {
            _dettaglio.verifica.push(new Dato({label: Voce.TRUST_STORE_PWD, value: this.json.servizioVerifica.auth.tsPassword }));
          }
        }
      }
    }
    if(this.json.servizioNotifica) {
      _dettaglio.notifica.push(new Dato({ label: Voce.URL, value: this.json.servizioNotifica.url }));
      _dettaglio.notifica.push(new Dato({ label: Voce.VERSIONE_API, value: this.json.servizioNotifica.versioneApi }));
      if(this.json.servizioNotifica.auth) {
        _dettaglio.notifica.push(new Dato({ label: Voce.TIPO_AUTH, value: '' }));
        if(this.json.servizioNotifica.auth.username) {
          _dettaglio.notifica.push(new Dato({label: Voce.USERNAME, value: this.json.servizioNotifica.auth.username }));
          _dettaglio.notifica.push(new Dato({label: Voce.PASSWORD, value: this.json.servizioNotifica.auth.password }));
        }
        if(this.json.servizioNotifica.auth.tipo) {
          _dettaglio.notifica.push(new Dato({label: Voce.TIPO, value: this.json.servizioNotifica.auth.tipo }));
          _dettaglio.notifica.push(new Dato({label: Voce.KEY_STORE_LOC, value: this.json.servizioNotifica.auth.ksLocation }));
          _dettaglio.notifica.push(new Dato({label: Voce.KEY_STORE_PWD, value: this.json.servizioNotifica.auth.ksPassword }));
          if(this.json.servizioNotifica.auth.tsLocation) {
            _dettaglio.notifica.push(new Dato({label: Voce.TRUST_STORE_LOC, value: this.json.servizioNotifica.auth.tsLocation }));
          }
          if(this.json.servizioNotifica.auth.tsPassword) {
            _dettaglio.notifica.push(new Dato({label: Voce.TRUST_STORE_PWD, value: this.json.servizioNotifica.auth.tsPassword }));
          }
        }
      }
    }

    let p: Parameters;
    let dea = {
      domini: this.json.domini?this.json.domini:[],
      entrate: this.json.entrate?this.json.entrate:[],
      acls: this.json.acl?this.json.acl:[]
    };
    //Domini
    let _d = dea.domini.map(function(item) {
      let _std = new Standard();
      _std.titolo = new Dato({ value: item.ragioneSociale });
      _std.sottotitolo = new Dato({ label: Voce.ID_DOMINIO+': ', value: item.idDominio });
      p = new Parameters();
      p.jsonP = item;
      p.model = _std;
      return p;
    });
    //Entrate
    let _e = dea.entrate.map(function(item) {
      let _std = new Standard();
      _std.titolo = new Dato({ value: item.descrizione });
      _std.sottotitolo = new Dato({ label: Voce.ID_ENTRATA+': ', value: item.idEntrata });
      p = new Parameters();
      p.jsonP = item;
      p.model = _std;
      return p;
    });
    let _a = dea.acls.map(function(item) {
      let auths = item.autorizzazioni.map((s) => {
        let codes = UtilService.DIRITTI_CODE.filter((a) => {
          return (a.code == s);
        });
        return (codes.length!=0)?codes[0].label:'';
      });
      auths = (auths.length != 0)?auths.join(', '):'Nessuna.';
      let _std = new Standard();
      _std.titolo = new Dato({ label: item.servizio, value: '' });
      _std.sottotitolo = new Dato({ label: Voce.AUTORIZZAZIONI+': ', value: auths });
      p = new Parameters();
      p.jsonP = item;
      p.model = _std;
      return p;
    });

    this.informazioni = _dettaglio.info.slice(0);
    this.avvisi = _dettaglio.avviso.slice(0);
    this.verifiche = _dettaglio.verifica.slice(0);
    this.notifiche = _dettaglio.notifica.slice(0);

    this.domini = _d.slice(0);
    this.entrate = _e.slice(0);
    this.acls = _a.slice(0);
  }

  protected _editApplicazione(event: any) {
    let _mb = new ModalBehavior();
    _mb.editMode = true;
    _mb.info = {
      viewModel: this.json,
      dialogTitle: 'Modifica applicazione',
      templateName: UtilService.APPLICAZIONE
    };
    _mb.async_callback = this.save.bind(this);
    _mb.closure = this.refresh.bind(this);
    UtilService.blueDialogBehavior.next(_mb);
  }

  protected _iconClick(type: string, ref: any, event: any) {
    let _json;
    let _ivm = ref.getItemViewModel();
    switch(event.type) {
      case 'edit':
        console.log('edit');
      break;
      case 'delete':
        switch(type) {
          case this.DOMINIO:
            _json = [ { op: UtilService.PATCH_METHODS.DELETE, path: UtilService.URL_DOMINI, value: _ivm.jsonP.idDominio } ];
          break;
          case this.ENTRATA:
            _json = [ { op: UtilService.PATCH_METHODS.DELETE, path: UtilService.URL_ENTRATE, value: _ivm.jsonP.idEntrata }];
          break;
          case this.ACL:
            _json = [ { op: UtilService.PATCH_METHODS.DELETE, path: UtilService.URL_ACLS, value: _ivm.jsonP } ];
            break;
        }
      if(_json) {
        this.updateElements(type, _json);
      }
      break;
    }
  }

  /**
   * Update json list elements
   * @param {string} type
   * @param json
   */
  protected updateElements(type: string, json: any) {
    let _service = UtilService.URL_APPLICAZIONI+'/'+encodeURIComponent(this.json['idA2A']);
    this.gps.saveData(_service, json, null, UtilService.METHODS.PATCH).subscribe(
      (response) => {
        this.gps.updateSpinner(false);
        switch(type) {
          case this.DOMINIO:
          case this.ENTRATA:
          case this.ACL:
            this.json = response.body;
            this.mapJsonDetail();
            this.us.alert('Operazione completata.');
            break;
        }
      },
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

  protected _asyncDetail(type: string) {
    let _service = '';
    switch(type) {
      case this.DOMINIO:
        _service = UtilService.URL_DOMINI;
        break;
      case this.ENTRATA:
        _service = UtilService.URL_ENTRATE;
        break;
    }
    this.gps.getDataService(_service).subscribe(
    (response) => {
      this.gps.updateSpinner(false);
      this._addEdit(type, this.ADD, true, response.body);
    },
    (error) => {
      this.gps.updateSpinner(false);
      this.us.onError(error);
    });
  }

  protected _addEdit(type: string, patchOperation: string, mode: boolean = false, _viewModel?: any) {
    let _mb: ModalBehavior = new ModalBehavior();
    _mb.editMode = mode;
    _mb.async_callback = this.save.bind(this);
    _mb.closure = this.refresh.bind(this);
    switch(type) {
      case this.DOMINIO:
        _mb.operation = patchOperation;
        _mb.info.dialogTitle = 'Nuovo dominio';
        _mb.info.viewModel = new FormInput({ values: this.filterAndMapByList(_viewModel['risultati'], this.domini, 'idDominio', 'ragioneSociale') });
        _mb.info.templateName = this.DOMINIO;
        break;
      case this.ENTRATA:
        _mb.operation = patchOperation;
        _mb.info.dialogTitle = 'Nuova entrata';
        _mb.info.viewModel = new FormInput({ values: this.filterAndMapByList(_viewModel['risultati'], this.entrate, 'idEntrata', 'descrizione') });
        _mb.info.templateName = this.ENTRATA;
        break;
      case this.ACL:
        _mb.operation = patchOperation;
        _mb.info.dialogTitle = 'Nuova operazione';
        _mb.info.viewModel = this.json;
        _mb.info.templateName = this.ACL;
        break;
    }
    UtilService.dialogBehavior.next(_mb);
  }

  /**
   * Filter by list and key to {label,value} object mapped list
   * @param {any[]} fullList
   * @param {Parameters[]} checkList
   * @param {string} key
   * @param {string} label
   * @returns { { label: '@label', value: '@key' }[] }
   */
  protected filterAndMapByList(fullList: any[], checkList: Parameters[], key: string, label: string): any[] {
    let _fl = fullList.filter((item) => {
      let _keep: boolean = true;
      checkList.forEach((el) => {
        if(el.jsonP[key] == item[key]) {
          _keep = false;
        }
      });
      return _keep;
    });
    return _fl.map((item) => {
      return { label: item[label], value: item[key] };
    });
  }

  refresh(mb: ModalBehavior) {
    this.modified = false;
    if(mb && mb.info && mb.info.viewModel) {
      this.modified = true;
      switch(mb.info.templateName) {
        case UtilService.APPLICAZIONE:
        case this.ACL:
        case this.DOMINIO:
        case this.ENTRATA:
          // this.dettaglioApplicazione(); ACL
          this.json = mb.info.viewModel;
          this.mapJsonDetail();
        break;
      }
    }
  }

  /**
   * Save Applicazione|Dominio|Entrata (Put to: /applicazioni/{idA2A} )
   * @param {BehaviorSubject<any>} responseService
   * @param {ModalBehavior} mb
   */
  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {
    if(mb && mb.info.viewModel) {
      let _json;
      let _query = null;
      let _method = null;
      let _id = (mb.editMode)?this.json['idA2A']:mb.info.viewModel['idA2A'];
      let _service = UtilService.URL_APPLICAZIONI+'/'+encodeURIComponent(_id);
      switch(mb.info.templateName) {
        case UtilService.APPLICAZIONE:
          _json = mb.info.viewModel;
          _json.domini = _json.domini.map((d) => {
            return d.idDominio;
          });
          _json.entrate = _json.entrate.map((e) => {
            return e.idEntrata;
          });
          delete _json.idA2A;
          break;
        case UtilService.DOMINIO:
          _method = UtilService.METHODS.PATCH;
          _json = [];
          mb.info.viewModel.values.forEach((_item) => {
            _json.push({ op: mb.operation, path: UtilService.URL_DOMINI, value: _item.idDominio });
          });
        break;
        case UtilService.ENTRATA:
          _method = UtilService.METHODS.PATCH;
          _json = [];
          mb.info.viewModel.values.forEach((_item) => {
            _json.push({ op: mb.operation, path: UtilService.URL_ENTRATE, value: _item.idEntrata });
          });
        break;
        case UtilService.ACL:
          _method = UtilService.METHODS.PATCH;
          _json = [];
          _json.push({ op: mb.operation, path: UtilService.URL_ACLS, value: mb.info.viewModel });
          break;
      }
      this.gps.saveData(_service, _json, _query, _method).subscribe(
        (response) => {
          if(mb.editMode) {
            switch(mb.info.templateName) {
              case UtilService.APPLICAZIONE:
                mb.info.viewModel['idA2A'] = this.json['idA2A'];
                mb.info.viewModel['domini'] = this.json.domini;
                mb.info.viewModel['entrate'] = this.json.entrate;
                mb.info.viewModel['acl'] = this.json.acl;
              break;
              case UtilService.DOMINIO:
              case UtilService.ENTRATA:
              case UtilService.ACL:
                mb.info.viewModel = response.body;
              break;
            }
          }
          this.gps.updateSpinner(false);
          responseService.next(true);
        },
        (error) => {
          this.gps.updateSpinner(false);
          this.us.onError(error);
        });
    }
  }

  title(): string {
    return UtilService.defaultDisplay({ value: this.json?this.json.principal:null });
  }

  infoDetail(): any {
    return {};
  }

}
