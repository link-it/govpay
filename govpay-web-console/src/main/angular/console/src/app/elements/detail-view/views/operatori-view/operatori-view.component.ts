import { Component, Input, OnInit } from '@angular/core';
import { IModalDialog } from '../../../../classes/interfaces/IModalDialog';
import { GovpayService } from '../../../../services/govpay.service';
import { UtilService } from '../../../../services/util.service';
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

  @Input() json: any;
  @Input() modified: boolean = false;

  protected DOMINIO = UtilService.DOMINIO;
  protected ENTRATA = UtilService.ENTRATA;

  constructor(public gps: GovpayService, public us: UtilService) { }

  ngOnInit() {
    this.dettaglioOperatore();
    this.elencoDominiEntrate();
  }

  protected dettaglioOperatore() {
    let _dettaglio = [];
    _dettaglio.push(new Dato({ label: 'Principal', value: this.json.principal }));
    _dettaglio.push(new Dato({ label: 'Ragione sociale', value: this.json.ragioneSociale }));
    _dettaglio.push(new Dato({ label: 'Abilitato', value: UtilService.ABILITA[this.json.abilitato.toString()] }));
    this.informazioni = _dettaglio.slice(0);
  }

  protected elencoDominiEntrate() {
    let _service = UtilService.URL_OPERATORI+'/'+this.json.principal;
    this.gps.getDataService(_service).subscribe(function (_response) {
        let _body = _response.body;
        let p: Parameters;
        let de = {
          domini: _body.domini?_body.domini:[],
          entrate: _body.entrate?_body.entrate:[]
        };
        let _d = de.domini.map(function(item) {
          let _std = new Standard();
          _std.titolo = new Dato({ label: item.ragioneSociale, value: '' });
          _std.sottotitolo = new Dato({ label: 'Id: ', value: item.idDominio });
          p = new Parameters();
          p.jsonP = item;
          p.model = _std;
          return p;
        });
        let _e = de.entrate.map(function(item) {
          let _std = new Standard();
          _std.titolo = new Dato({ label: item.descrizione, value: '' });
          _std.sottotitolo = new Dato({ label: 'Id: ', value: item.idEntrata });
          p = new Parameters();
          p.jsonP = item;
          p.model = _std;
          return p;
        });
        this.domini = _d.slice(0);
        this.entrate = _e.slice(0);
        this.gps.updateSpinner(false);
      }.bind(this),
      (error) => {
        this.gps.updateSpinner(false);
        this.us.alert(error.message);
      });
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

    if(type) {
      //TODO: Servizio 'delete' non attivo
      return;
    }

    let _ivm = ref.getItemViewModel();
    switch(event.type) {
      case 'edit':
        console.log('edit');
        break;
      case 'delete':
        let json = JSON.parse(JSON.stringify(this.json));
        let modelArray = [];
        switch(type) {
          case this.DOMINIO:
            modelArray = this.domini.filter((item) => {
              return !(item.jsonP.idDominio === _ivm.jsonP.idDominio);
            });
            json.domini = modelArray.map((el) => {
              return el.jsonP;
            });
            break;
          case this.ENTRATA:
            modelArray = this.entrate.filter((item) => {
              return !(item.jsonP.idEntrata === _ivm.jsonP.idEntrata);
            });
            json.entrate = modelArray.map((el) => {
              return el.jsonP;
            });
            break;
        }
        this.updateElements(type, modelArray, json);
        break;
    }
  }

  /**
   * Update json list elements
   * @param {string} type
   * @param {Parameters[]} elements
   * @param json
   */
  protected updateElements(type: string, elements: Parameters[], json: any) {
    let _service = UtilService.URL_OPERATORI+'/'+json['principal'];
    this.gps.saveData(_service, json).subscribe(
      () => {
        this.gps.updateSpinner(false);
        switch(type) {
          case this.DOMINIO:
            this.domini = elements.slice(0);
            break;
          case this.ENTRATA:
            this.entrate = elements.slice(0);
            break;
        }
      },
      (error) => {
        this.gps.updateSpinner(false);
        this.us.alert(error.message);
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
        this._addEdit(type, true, response.body); //TODO: edit mode: false !?
      },
      (error) => {
        this.gps.updateSpinner(false);
        this.us.alert(error.message);
      });
  }

  protected _addEdit(type: string, mode: boolean = false, _viewModel?: any) {
    let _mb: ModalBehavior = new ModalBehavior();
    _mb.editMode = mode;
    _mb.async_callback = this.save.bind(this);
    _mb.closure = this.refresh.bind(this);
    switch(type) {
      case this.DOMINIO:
        _mb.info.dialogTitle = 'Nuovo dominio';
        _mb.info.viewModel = new FormInput({ values: this.filterAndMapByList(_viewModel['risultati'], this.domini, 'idDominio', 'ragioneSociale') });
        _mb.info.templateName = this.DOMINIO;
        break;
      case this.ENTRATA:
        _mb.info.dialogTitle = 'Nuova entrata';
        _mb.info.viewModel = new FormInput({ values: this.filterAndMapByList(_viewModel['risultati'], this.entrate, 'idEntrata', 'descrizione') });
        _mb.info.templateName = this.ENTRATA;
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
      let _std = new Standard();
      let p = new Parameters();
      switch(mb.info.templateName) {
        case this.DOMINIO:
          mb.info.viewModel.values.forEach((el) => {
            _std.titolo = new Dato({ label: el.ragioneSociale, value: '' });
            _std.sottotitolo = new Dato({ label: 'Id: ', value: el.idDominio });
            p.jsonP = el;
            p.model = _std;
            this.domini.push(p);
          });
          break;
        case this.ENTRATA:
          mb.info.viewModel.values.forEach((el) => {
            _std.titolo = new Dato({ label: el.descrizione, value: '' });
            _std.sottotitolo = new Dato({ label: 'Id: ', value: el.idEntrata });
            p.jsonP = el;
            p.model = _std;
            this.entrate.push(p);
          });
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
      let _id = (mb.editMode)?this.json['principal']:mb.info.viewModel['principal'];
      let _service = UtilService.URL_OPERATORI+'/'+_id;
      switch(mb.info.templateName) {
        case UtilService.OPERATORE:
          this.json = mb.info.viewModel;
          break;
        case UtilService.DOMINIO:
          this.json.domini = this.json.domini.concat(mb.info.viewModel.values);
          break;
        case UtilService.ENTRATA:
          this.json.entrate = this.json.entrate.concat(mb.info.viewModel.values);
          break;
      }
      this.gps.saveData(_service, this.json).subscribe(
        () => {
          this.gps.updateSpinner(false);
          responseService.next(true);
        },
        (error) => {
          this.gps.updateSpinner(false);
          this.us.alert(error.message);
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
