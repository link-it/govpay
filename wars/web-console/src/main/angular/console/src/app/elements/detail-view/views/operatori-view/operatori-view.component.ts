import { Component, Input, OnInit } from '@angular/core';
import { IModalDialog } from '../../../../classes/interfaces/IModalDialog';
import { GovpayService } from '../../../../services/govpay.service';
import { UtilService } from '../../../../services/util.service';
import { Voce } from '../../../../services/voce.service';
import { Dato } from '../../../../classes/view/dato';
import { Parameters } from '../../../../classes/parameters';
import { Standard } from '../../../../classes/view/standard';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { ModalBehavior } from '../../../../classes/modal-behavior';
import { FormInput } from '../../../../classes/view/form-input';

@Component({
  selector: 'link-operatori-view',
  templateUrl: './operatori-view.component.html',
  styleUrls: ['./operatori-view.component.scss']
})
export class OperatoriViewComponent implements IModalDialog, OnInit {

  @Input() informazioni = [];
  @Input() domini = [];
  @Input() entrate = [];
  @Input() acls = [];

  @Input() json: any;
  @Input() modified: boolean = false;

  protected DOMINIO = UtilService.DOMINIO;
  protected ENTRATA = UtilService.ENTRATA;
  protected ACL = UtilService.ACL;
  protected ADD = UtilService.PATCH_METHODS.ADD;
  protected _OPERAZIONI = Voce.OPERAZIONI;

  constructor(public gps: GovpayService, public us: UtilService) { }

  ngOnInit() {
    this.dettaglioOperatore();
    this.elencoDominiEntrateAcls();
  }

  protected dettaglioOperatore() {
    let _dettaglio = [];
    _dettaglio.push(new Dato({ label: Voce.PRINCIPAL, value: this.json.principal }));
    _dettaglio.push(new Dato({ label: Voce.NOME, value: this.json.ragioneSociale }));
    _dettaglio.push(new Dato({ label: Voce.ABILITATO, value: UtilService.ABILITA[this.json.abilitato.toString()] }));
    this.informazioni = _dettaglio.slice(0);
  }

  protected elencoDominiEntrateAcls() {
    let _service = UtilService.URL_OPERATORI+'/'+encodeURIComponent(this.json.principal);
    this.gps.getDataService(_service).subscribe(function (_response) {
        let _body = _response.body;
        this.mapJsonDetail(_body);
        this.gps.updateSpinner(false);
      }.bind(this),
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

  protected mapJsonDetail(_json: any) {
    let p: Parameters;
    let dea = {
      domini: _json.domini?_json.domini:[],
      entrate: _json.entrate?_json.entrate:[],
      acls: _json.acl?_json.acl:[]
    };
    let _d = dea.domini.map(function(item) {
      let _std = new Standard();
      _std.titolo = new Dato({ label: item.ragioneSociale, value: '' });
      _std.sottotitolo = new Dato({ label: Voce.ID_DOMINIO+': ', value: item.idDominio });
      p = new Parameters();
      p.jsonP = item;
      p.model = _std;
      return p;
    });
    let _e = dea.entrate.map(function(item) {
      let _std = new Standard();
      _std.titolo = new Dato({ label: item.descrizione, value: '' });
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
    this.domini = _d.slice(0);
    this.entrate = _e.slice(0);
    this.acls = _a.slice(0);
  }

  protected _editOperatore(event: any) {
    let _mb = new ModalBehavior();
    _mb.editMode = true;
    _mb.info = {
      viewModel: this.json,
      dialogTitle: 'Modifica operatore',
      templateName: UtilService.OPERATORE
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
            _json = [ { op: UtilService.PATCH_METHODS.DELETE, path: UtilService.URL_ENTRATE, value: _ivm.jsonP.idEntrata } ];
            break;
          case this.ACL:
            _json =  [ { op: UtilService.PATCH_METHODS.DELETE, path: UtilService.URL_ACLS, value: _ivm.jsonP } ];
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
    let _service = UtilService.URL_OPERATORI+'/'+encodeURIComponent(this.json['principal']);
    this.gps.saveData(_service, json, null, UtilService.METHODS.PATCH).subscribe(
      (response) => {
        this.gps.updateSpinner(false);
        switch(type) {
          case this.DOMINIO:
          case this.ENTRATA:
          case this.ACL:
            this.json = response.body;
            this.mapJsonDetail(response.body);
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
    let _fi: FormInput;
    switch(type) {
      case this.DOMINIO:
        _mb.operation = patchOperation;
        _mb.info.dialogTitle = 'Nuovo dominio';
        _fi = new FormInput({ values: this.filterAndMapByList(_viewModel['risultati'], this.domini, 'idDominio', 'ragioneSociale') });
        _fi.values.unshift({ label: UtilService.TUTTI_DOMINI.label, value: UtilService.TUTTI_DOMINI.value });
        _mb.info.viewModel = _fi;
        _mb.info.templateName = this.DOMINIO;
        break;
      case this.ENTRATA:
        _mb.operation = patchOperation;
        _mb.info.dialogTitle = 'Nuova entrata';
        _fi = new FormInput({ values: this.filterAndMapByList(_viewModel['risultati'], this.entrate, 'idEntrata', 'descrizione') });
        _fi.values.unshift({ label: UtilService.TUTTE_ENTRATE.label, value: UtilService.TUTTE_ENTRATE.value });
        _mb.info.viewModel = _fi;
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
        case this.DOMINIO:
        case this.ENTRATA:
        case this.ACL:
          //this.elencoDominiEntrateAcls();
          this.json = mb.info.viewModel;
          this.mapJsonDetail(this.json);
          break;
        case UtilService.OPERATORE:
          this.json = mb.info.viewModel;
          this.dettaglioOperatore();
          break;
      }
    }
  }

  /**
   * Save Operatore|Dominio|Entrata (Put to: /operatori/{principal} )
   * @param {BehaviorSubject<any>} responseService
   * @param {ModalBehavior} mb
   */
  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {
    if(mb && mb.info.viewModel) {
      let _json;
      let _query = null;
      let _method = null;
      let _id = (mb.editMode)?this.json['principal']:mb.info.viewModel['principal'];
      let _service = UtilService.URL_OPERATORI+'/'+encodeURIComponent(_id);
      switch(mb.info.templateName) {
        case UtilService.OPERATORE:
          _json = mb.info.viewModel;
          delete _json.principal;
          _json.domini = _json.domini.map((d) => {
            return d.idDominio;
          });
          _json.entrate = _json.entrate.map((e) => {
            return e.idEntrata;
          });
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
            switch (mb.info.templateName) {
              case UtilService.OPERATORE:
                mb.info.viewModel['principal'] = this.json['principal'];
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
