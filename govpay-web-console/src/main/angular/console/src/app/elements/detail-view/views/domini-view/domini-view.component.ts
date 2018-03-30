import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { UtilService } from '../../../../services/util.service';
import { IModalDialog } from '../../../../classes/interfaces/IModalDialog';
import { ModalBehavior } from '../../../../classes/modal-behavior';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { GovpayService } from '../../../../services/govpay.service';
import { Dato } from '../../../../classes/view/dato';
import { Parameters } from '../../../../classes/parameters';
import { Standard } from '../../../../classes/view/standard';

@Component({
  selector: 'link-domini-view',
  templateUrl: './domini-view.component.html',
  styleUrls: ['./domini-view.component.scss']
})
export class DominiViewComponent implements IModalDialog, OnInit, AfterViewInit {

  @Input() informazioni = [];
  @Input() iban_cc = [];
  @Input() entrate = [];
  @Input() unita_operative = [];

  @Input() json: any;
  @Input() modified: boolean = false;
  @Input() parent: any;

  protected logo: string;

  protected _IBAN = UtilService.IBAN_ACCREDITO;
  protected _ENTRATA_DOMINIO = UtilService.ENTRATA_DOMINIO;
  protected _UNITA = UtilService.UNITA_OPERATIVA;


  constructor(public gps: GovpayService, public us: UtilService) { }

  ngOnInit() {
    this.dettaglioDominio();
    this.elencoUnitaOperative();
    this.elencoAccreditiIban();
    this.elencoEntrateDominio();
  }

  ngAfterViewInit() {
  }

  protected dettaglioDominio() {
    let _url = UtilService.URL_DOMINI+'/'+encodeURIComponent(this.json.idDominio);
    this.gps.getDataService(_url).subscribe(
      function (_response) {
        this.json = _response.body;
        //Riepilogo
        this.mapJsonDetail();
        this.gps.updateSpinner(false);
      }.bind(this),
      (error) => {
        //console.log(error);
        this.us.alert(error.message);
        this.gps.updateSpinner(false);
      });

  }

  protected mapJsonDetail() {
    let _dettaglio = { info: [], iban: [], entrate: [], unita: [] };
    _dettaglio.info.push(new Dato({ label: 'Id dominio', value: this.json.idDominio }));
    _dettaglio.info.push(new Dato({ label: 'Ragione sociale', value: this.json.ragioneSociale }));
    _dettaglio.info.push(new Dato({ label: 'Indirizzo', value: this.json.indirizzo }));
    _dettaglio.info.push(new Dato({ label: 'Numero civico', value: this.json.civico }));
    _dettaglio.info.push(new Dato({ label: 'Cap', value: this.json.cap }));
    _dettaglio.info.push(new Dato({ label: 'Località', value: this.json.localita }));
    _dettaglio.info.push(new Dato({ label: 'Provincia', value: this.json.provincia }));
    _dettaglio.info.push(new Dato({ label: 'Nazione', value: this.json.nazione }));
    _dettaglio.info.push(new Dato({ label: 'Email', value: this.json.email }));
    _dettaglio.info.push(new Dato({ label: 'Pec', value: this.json.pec }));
    _dettaglio.info.push(new Dato({ label: 'Telefono', value: this.json.tel }));
    _dettaglio.info.push(new Dato({ label: 'Fax', value: this.json.fax }));
    _dettaglio.info.push(new Dato({ label: 'Sito web', value: this.json.web }));
    _dettaglio.info.push(new Dato({ label: 'Global location number', value: this.json.gln }));
    _dettaglio.info.push(new Dato({ label: 'CBill', value: this.json.cbill }));
    _dettaglio.info.push(new Dato({ label: 'Prefisso IUV', value: this.json.iuvPrefix }));
    _dettaglio.info.push(new Dato({ label: 'Stazione', value: this.json.stazione }));
    _dettaglio.info.push(new Dato({ label: 'Aux', value: this.json.auxDigit }));
    _dettaglio.info.push(new Dato({ label: 'Codice di segregazione', value: this.json.segregationCode }));
    _dettaglio.info.push(new Dato({ label: 'Abilitato', value: UtilService.ABILITA[this.json.abilitato.toString()] }));

    this.logo = this.json.logo;
    this.informazioni = _dettaglio.info.slice(0);
  }

  protected elencoUnitaOperative() {
    this.gps.getDataService(this.json.unitaOperative).subscribe(function (_response) {
        let _body = _response.body;
        let p: Parameters;
        let _duo = _body['risultati'].map(function(item) {
          p = new Parameters();
          p.jsonP = item;
          p.model = this._mapNewItemByType(item, this._UNITA);
          return p;
        }, this);
        this.unita_operative = _duo.slice(0);
        this.gps.updateSpinner(false);
      }.bind(this),
      (error) => {
        this.gps.updateSpinner(false);
        this.us.alert(error.message);
      });
  }

  protected elencoAccreditiIban() {
    this.gps.getDataService(this.json.ibanAccredito).subscribe(function (_response) {
        let _body = _response.body;
        let p: Parameters;
        let _di = _body['risultati'].map(function(item) {
          p = new Parameters();
          p.jsonP = item;
          p.model = this._mapNewItemByType(item, this._IBAN);
          return p;
        }, this);
        this.iban_cc = _di.slice(0);
        this.gps.updateSpinner(false);
      }.bind(this),
      (error) => {
        this.gps.updateSpinner(false);
        this.us.alert(error.message);
      });
  }

  protected elencoEntrateDominio() {
    this.gps.getDataService(this.json.entrate).subscribe(function (_response) {
        let _body = _response.body;
        let p: Parameters;
        let _de = _body['risultati'].map(function(item) {
          p = new Parameters();
          p.jsonP = item;
          p.model = this._mapNewItemByType(item, this._ENTRATA_DOMINIO);
          return p;
        }, this);
        this.entrate = _de.slice(0);
        this.gps.updateSpinner(false);
      }.bind(this),
      (error) => {
        this.gps.updateSpinner(false);
        this.us.alert(error.message);
      });
  }

  protected _onError() {
    console.warn('Png image: Cannot load base64 data.')
  }

  protected _editDominio(event: any) {
    let _mb = new ModalBehavior();
    _mb.editMode = true;
    _mb.info = {
      viewModel: this.json,
      dialogTitle: 'Modifica dominio',
      templateName: UtilService.DOMINIO
    };
    _mb.async_callback = this.save.bind(this);
    _mb.closure = this.refresh.bind(this);
    UtilService.blueDialogBehavior.next(_mb);
  }

  protected _iconClick(type: string, ref: any, event: any) {

    //TODO: Servizi non attivi

    let _ivm = ref.getItemViewModel();
    switch(event.type) {
      case 'edit':
        console.log('edit');
        break;
      case 'delete':
        console.log('delete');
        break;
    }
  }

  protected _mapNewItemByType(item: any, type: string): Standard {
    let _std = new Standard();
    switch(type) {
      case this._ENTRATA_DOMINIO:
        _std.titolo = new Dato({ value: item.tipoEntrata.descrizione });
        _std.sottotitolo = new Dato({ label: 'Id: ', value: item.idEntrata });
      break;
      case this._UNITA:
        _std.titolo = new Dato({ value: item.ragioneSociale });
        _std.sottotitolo = new Dato({ label: 'Id: ', value: item.idUnita });
      break;
      case this._IBAN:
        _std.titolo = new Dato({ value: item.ibanAccredito });
      break;
    }
    return _std;
  }

  protected _addEdit(type: string, mode: boolean = false, _viewModel?: any) {
    let _mb: ModalBehavior = new ModalBehavior();
    _mb.editMode = mode;
    _mb.async_callback = this.save.bind(this);
    _mb.closure = this.refresh.bind(this);
    switch(type) {
      case this._UNITA:
        _mb.info = {
          viewModel: _viewModel,
          dialogTitle: 'Nuova unità operativa',
          templateName: this._UNITA
        };
      UtilService.blueDialogBehavior.next(_mb);
      break;
      case this._ENTRATA_DOMINIO:
        _mb.info = {
          viewModel: _viewModel,
          parent: this,
          dialogTitle: 'Nuova entrata',
          templateName: this._ENTRATA_DOMINIO
        };
      UtilService.blueDialogBehavior.next(_mb);
      break;
      case this._IBAN:
        _mb.info = {
          viewModel: _viewModel,
          parent: this,
          dialogTitle: 'Nuovo IBAN',
          templateName: this._IBAN
        };
      UtilService.dialogBehavior.next(_mb);
      break;
    }
  }

  /**
   * Save Dominio|Entrata-Dominio|Unità operativa|Iban (Put to: /domini/{idDominio} )
   * @param {BehaviorSubject<any>} responseService
   * @param {ModalBehavior} mb
   */
  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {
    let _service = null;
    let _json = null;
    switch(mb.info.templateName) {
      case UtilService.DOMINIO:
        _service = UtilService.URL_DOMINI+'/'+mb.info.viewModel['idDominio'];
        _json = mb.info.viewModel;
      break;
      case this._ENTRATA_DOMINIO:
        _service = this.json['entrate']+'/'+mb.info.viewModel['tipoEntrata'].idEntrata;
        _json = mb.info.viewModel;
      break;
      case this._UNITA:
        _service = this.json['unitaOperative']+'/'+mb.info.viewModel.idUnita;
        _json = mb.info.viewModel;
      break;
      case this._IBAN:
        _service = this.json['ibanAccredito']+'/'+mb.info.viewModel.ibanAccredito;
        _json = mb.info.viewModel;
      break;
    }
    if(_json && _service) {
    this.gps.saveData(_service, _json).subscribe(
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

  refresh(mb: ModalBehavior) {
    this.modified = false;
    if(mb && mb.info && mb.info.viewModel) {
      this.modified = true;
      let p = new Parameters();
      switch(mb.info.templateName) {
        case UtilService.DOMINIO:
          if(mb.editMode) {
            let j = { e: this.json['entrate'], ia: this.json['ibanAccredito'], uo: this.json['unitaOperative'] };
            this.json = mb.info.viewModel;
            this.json['entrate'] = j.e;
            this.json['ibanAccredito'] = j.ia;
            this.json['unitaOperative'] = j.uo;
            this.mapJsonDetail();
          }
        break;
        case this._ENTRATA_DOMINIO:
          p.jsonP = mb.info.viewModel;
          p.model = this._mapNewItemByType(mb.info.viewModel, this._ENTRATA_DOMINIO);
          this.entrate.push(p);
        break;
        case this._UNITA:
          p.jsonP = mb.info.viewModel;
          p.model = this._mapNewItemByType(mb.info.viewModel, this._UNITA);
          this.unita_operative.push(p);
        break;
        case this._IBAN:
          p.jsonP = mb.info.viewModel;
          p.model = this._mapNewItemByType(mb.info.viewModel, this._IBAN);
          this.iban_cc.push(p);
        break;
      }
    }
  }

  title(): string {
    return UtilService.defaultDisplay({ value: this.json?this.json.ragioneSociale:null });
  }

  infoDetail(): any {
    return null;
  }
}
