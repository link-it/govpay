import { Component, Input, OnInit } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { ModalBehavior } from '../../../../classes/modal-behavior';
import { IModalDialog } from '../../../../classes/interfaces/IModalDialog';
import { UtilService } from '../../../../services/util.service';
import { GovpayService } from '../../../../services/govpay.service';
import { Voce } from '../../../../services/voce.service';
import { Parameters } from '../../../../classes/parameters';
import { Standard } from '../../../../classes/view/standard';
import { Dato } from '../../../../classes/view/dato';

@Component({
  selector: 'link-ruoli-view',
  templateUrl: './ruoli-view.component.html',
  styleUrls: ['./ruoli-view.component.scss']
})
export class RuoliViewComponent implements OnInit, IModalDialog {

  @Input() json: any;
  @Input() autorizzazioni = [];

  protected AUTH = UtilService.ACL;
  protected _OPERAZIONI = Voce.OPERAZIONI;

  constructor(public gps: GovpayService, public us: UtilService) { }

  ngOnInit() {
    this.dettaglioRuoli();
  }

  protected dettaglioRuoli() {
    let _service = UtilService.URL_RUOLI+'/'+this.json.id;
    this.gps.getDataService(_service).subscribe(function (_response) {
        this.json.acl = _response.body['acl'];
        this.mapAutorizzazioni();
        this.gps.updateSpinner(false);
      }.bind(this),
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

  protected mapAutorizzazioni() {
    this.autorizzazioni = this.json.acl.map((item) => {
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
      let p = new Parameters();
      p.jsonP = item;
      p.model = _std;
      return p;
    });
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
          case this.AUTH:
            _json = [ { op: UtilService.PATCH_METHODS.DELETE, path: UtilService.URL_RUOLI, value: _ivm.jsonP } ];
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
    let _service = UtilService.URL_RUOLI+'/'+encodeURIComponent(this.json['id']);
    this.gps.saveData(_service, json, null, UtilService.METHODS.PATCH).subscribe(
      (response) => {
        this.gps.updateSpinner(false);
        switch(type) {
          case this.AUTH:
            //this.json = response.body;
            this.dettaglioRuoli();
            this.us.alert('Operazione completata.');
            break;
        }
      },
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

  protected _addEdit(type: string, mode: boolean = false) {
    let _mb: ModalBehavior = new ModalBehavior();
    _mb.editMode = mode;
    _mb.async_callback = this.save.bind(this);
    _mb.closure = this.refresh.bind(this);
    switch(type) {
      case this.AUTH:
        _mb.info.dialogTitle = 'Nuova operazione';
        _mb.info.viewModel = this.json;
        _mb.info.templateName = this.AUTH;
        _mb.operation = UtilService.PATCH_METHODS.ADD;
        break;
    }
    UtilService.dialogBehavior.next(_mb);
  }

  refresh(mb: ModalBehavior) {
    if(mb && mb.info.viewModel) {
      this.dettaglioRuoli();
    }
  }

  /**
   * Save Ruolo|Autorizzazione
   * @param {BehaviorSubject<any>} responseService
   * @param {ModalBehavior} mb
   */
  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {
    if(mb && mb.info.viewModel) {
      //Non è previsto l'edit di un ruolo
      let _json;
      let _query = null;
      let _method = null;
      let _service = UtilService.URL_RUOLI+'/';
      switch(mb.info.templateName) {
        case UtilService.ACL:
          _service += encodeURIComponent(this.json.id);
          //this.json.acl.push(JSON.parse(JSON.stringify(mb.info.viewModel)));
          _method = UtilService.METHODS.PATCH;
          _json = [];
          _json.push({ op: mb.operation, path: UtilService.URL_RUOLI, value: mb.info.viewModel });
          break;
        case UtilService.RUOLO:
          _service += encodeURIComponent(mb.info.viewModel.id);
          let _tmpAuth = JSON.parse(JSON.stringify(mb.info.viewModel));
          delete _tmpAuth.id;
          _json = { acl: [] };
          _json.acl.push(_tmpAuth);
          break;
      }
      this.gps.saveData(_service, _json, _query, _method).subscribe(
        () => {
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
    let _t = this.json?((this.json.principal)?this.json.principal:this.json.ruolo):null;
    return UtilService.defaultDisplay({ value: _t });
  }

  infoDetail(): any {
    return null;
  }

}
