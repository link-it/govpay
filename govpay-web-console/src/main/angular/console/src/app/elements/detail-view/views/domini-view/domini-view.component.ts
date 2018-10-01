import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { UtilService } from '../../../../services/util.service';
import { IModalDialog } from '../../../../classes/interfaces/IModalDialog';
import { ModalBehavior } from '../../../../classes/modal-behavior';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { GovpayService } from '../../../../services/govpay.service';
import { Voce } from '../../../../services/voce.service';
import { Dato } from '../../../../classes/view/dato';
import { Parameters } from '../../../../classes/parameters';
import { Standard } from '../../../../classes/view/standard';
import { DomSanitizer } from '@angular/platform-browser';

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

  protected NO_LOGO: string = 'no-logo';
  protected logo: any = null;
  protected logoError: boolean = false;

  protected _IBAN = UtilService.IBAN_ACCREDITO;
  protected _ENTRATA_DOMINIO = UtilService.ENTRATA_DOMINIO;
  protected _UNITA = UtilService.UNITA_OPERATIVA;
  protected _PLUS_CREDIT = UtilService.USER_ACL.hasCreditore;


  constructor(private _sanitizer: DomSanitizer, public gps: GovpayService, public us: UtilService) { }

  ngOnInit() {
    this.dettaglioDominio();
  }

  ngAfterViewInit() {
  }

  protected dettaglioDominio() {
    let _url = UtilService.URL_DOMINI+'/'+encodeURIComponent(this.json.idDominio);
    this.gps.getDataService(_url).subscribe(
      function (_response) {
        this.json = _response.body;
        //Riepilogo
        this.mapJsonDetail(_response.body);
        this.gps.updateSpinner(false);
      }.bind(this),
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });

  }

  protected mapJsonDetail(json: any) {
    let _dettaglio = { info: [], iban: [], entrate: [], unita: [] };
    _dettaglio.info.push(new Dato({ label: Voce.ID_DOMINIO, value: json.idDominio }));
    _dettaglio.info.push(new Dato({ label: Voce.RAGIONE_SOCIALE, value: json.ragioneSociale }));
    _dettaglio.info.push(new Dato({ label: Voce.INDIRIZZO, value: json.indirizzo }));
    _dettaglio.info.push(new Dato({ label: Voce.CIVICO, value: json.civico }));
    _dettaglio.info.push(new Dato({ label: Voce.CAP, value: json.cap }));
    _dettaglio.info.push(new Dato({ label: Voce.LUOGO, value: json.localita }));
    _dettaglio.info.push(new Dato({ label: Voce.PROVINCIA, value: json.provincia }));
    _dettaglio.info.push(new Dato({ label: Voce.NAZIONE, value: json.nazione }));
    _dettaglio.info.push(new Dato({ label: Voce.EMAIL, value: json.email }));
    _dettaglio.info.push(new Dato({ label: Voce.PEC, value: json.pec }));
    _dettaglio.info.push(new Dato({ label: Voce.TELEFONO, value: json.tel }));
    _dettaglio.info.push(new Dato({ label: Voce.FAX, value: json.fax }));
    _dettaglio.info.push(new Dato({ label: Voce.WEB, value: json.web }));
    _dettaglio.info.push(new Dato({ label: Voce.GLN, value: json.gln }));
    _dettaglio.info.push(new Dato({ label: Voce.CBILL, value: json.cbill }));
    _dettaglio.info.push(new Dato({ label: Voce.IUV_PREFIX, value: json.iuvPrefix }));
    _dettaglio.info.push(new Dato({ label: Voce.STAZIONE, value: json.stazione }));
    _dettaglio.info.push(new Dato({ label: Voce.AUX, value: json.auxDigit }));
    _dettaglio.info.push(new Dato({ label: Voce.SECRET_CODE, value: UtilService.defaultDisplay({ value: json.segregationCode }) }));
    _dettaglio.info.push(new Dato({ label: Voce.ABILITATO, value: UtilService.ABILITA[json.abilitato.toString()] }));

    this.logoError = false;
    this.logo = json.logo?this._sanitizer.bypassSecurityTrustUrl(json.logo):'no-logo';

    this.informazioni = _dettaglio.info.slice(0);

    this.elencoUnitaOperative(json);
    this.elencoAccreditiIban(json);
    this.elencoEntrateDominio(json);
  }

  protected elencoUnitaOperative(json: any) {
    let p: Parameters;
    let _duo = json.unitaOperative.map(function(item) {
      p = new Parameters();
      p.jsonP = item;
      p.model = this._mapNewItemByType(item, this._UNITA);
      return p;
    }, this);
    this.unita_operative = _duo.slice(0);
  }

  protected elencoAccreditiIban(json: any) {
    let p: Parameters;
    let _di = json.contiAccredito.map(function(item) {
      p = new Parameters();
      p.jsonP = item;
      p.model = this._mapNewItemByType(item, this._IBAN);
      return p;
    }, this);
    this.iban_cc = _di.slice(0);
  }

  protected elencoEntrateDominio(json: any) {
    let p: Parameters;
    let _de = json.entrate.map(function(item) {
      p = new Parameters();
      p.jsonP = item;
      p.model = this._mapNewItemByType(item, this._ENTRATA_DOMINIO);
      return p;
    }, this);
    this.entrate = _de.slice(0);
  }

  protected _onError() {
    this.logoError = true;
    console.warn('Image not available.')
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
    //TODO: Delete non sono impostati
    let _ivm = ref.getItemViewModel();
    switch(event.type) {
      case 'edit':
        this._addEdit(type, true, _ivm.jsonP);
        break;
        // let _json;
        // case 'delete':
        //   switch(type) {
        //     case this._UNITA:
        //       _json = [ { op: UtilService.PATCH_METHODS.DELETE, path: UtilService.URL_UNITA_OPERATIVE, value: _ivm.jsonP.idUnita } ];
        //       break;
        //     case this._ENTRATA_DOMINIO:
        //       _json = [ { op: UtilService.PATCH_METHODS.DELETE, path: UtilService.URL_ENTRATE, value: _ivm.jsonP.idEntrata }];
        //       break;
        //     case this._IBAN:
        //       _json = [ { op: UtilService.PATCH_METHODS.DELETE, path: UtilService.URL_IBAN_ACCREDITI, value: _ivm.jsonP.iban }];
        //       break;
        //   }
        //   if(_json) {
        //     this.updateElements(type, _json);
        //   }
        //  break;
    }
  }

  /**
   * Update json list elements
   * @param {string} type
   * @param json
   */
  // protected updateElements(type: string, json: any) {
  //   let _service = '...';
  //   this.gps.saveData(_service, json, null, UtilService.METHODS.PATCH).subscribe(
  //     (response) => {
  //       this.gps.updateSpinner(false);
  //       this.json = response.body;
  //       switch(type) {
  //         case this._UNITA:
  //           this.elencoUnitaOperative(this.json);
  //           break;
  //         case this._ENTRATA_DOMINIO:
  //           this.elencoEntrateDominio(this.json);
  //           break;
  //         case this._IBAN:
  //           this.elencoAccreditiIban(this.json);
  //           break;
  //       }
  //       this.us.alert('Operazione completata.');
  //     },
  //     (error) => {
  //        this.gps.updateSpinner(false);
  //        this.us.onError(error);
  //     });
  // }

  /**
   * _mapNewItemByType
   * @param item
   * @param {string} type
   * @returns {Standard}
   * @private
   */
  protected _mapNewItemByType(item: any, type: string): Standard {
    let _std = new Standard();
    switch(type) {
      case this._ENTRATA_DOMINIO:
        _std.titolo = new Dato({ value: item.tipoEntrata.descrizione });
        _std.sottotitolo = new Dato({ label: Voce.ID_ENTRATA+': ', value: item.idEntrata });
      break;
      case this._UNITA:
        _std.titolo = new Dato({ value: item.ragioneSociale });
        _std.sottotitolo = new Dato({ label: Voce.ID_UNITA+': ', value: item.idUnita });
      break;
      case this._IBAN:
        _std.titolo = new Dato({ value: item.iban });
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
          dialogTitle: (!mode)?'Nuova unità operativa':'Modifica unità operativa',
          templateName: this._UNITA
        };
        UtilService.blueDialogBehavior.next(_mb);
      break;
      case this._ENTRATA_DOMINIO:
        _mb.info = {
          viewModel: _viewModel,
          parent: this,
          dialogTitle: (!mode)?'Nuova entrata':'Modifica entrata',
          templateName: this._ENTRATA_DOMINIO
        };
        UtilService.blueDialogBehavior.next(_mb);
      break;
      case this._IBAN:
        _mb.info = {
          viewModel: _viewModel,
          parent: this,
          dialogTitle: (!mode)?'Nuovo IBAN':'Modifica IBAN',
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
    let _id = mb.info.viewModel['idDominio'] || this.json.idDominio;
    let _service = UtilService.URL_DOMINI+'/'+_id;
    let _json = null;
    switch(mb.info.templateName) {
      case UtilService.DOMINIO:
        _json = mb.info.viewModel;
        delete _json.idDominio;
      break;
      case this._ENTRATA_DOMINIO:
        _service += UtilService.URL_ENTRATE+'/'+mb.info.viewModel.idEntrata;
        _json = JSON.parse(JSON.stringify(mb.info.viewModel));
        delete _json.idEntrata;
        delete _json['tipoEntrata'];
      break;
      case this._UNITA:
        _service += UtilService.URL_UNITA_OPERATIVE+'/'+mb.info.viewModel.idUnita;
        _json = JSON.parse(JSON.stringify(mb.info.viewModel));
        delete _json.idUnita;
      break;
      case this._IBAN:
        _service += UtilService.URL_IBAN_ACCREDITI+'/'+mb.info.viewModel.iban;
        _json = JSON.parse(JSON.stringify(mb.info.viewModel));
        delete _json.iban;
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
          this.us.onError(error);
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
            let j = { e: this.json['entrate'], ia: this.json['contiAccredito'], uo: this.json['unitaOperative'] };
            let json = mb.info.viewModel;
            json['entrate'] = j.e;
            json['contiAccredito'] = j.ia;
            json['unitaOperative'] = j.uo;
            // this.mapJsonDetail(json);
            this.dettaglioDominio();
          }
        break;
        case this._ENTRATA_DOMINIO:
          p.jsonP = mb.info.viewModel;
          p.model = this._mapNewItemByType(mb.info.viewModel, this._ENTRATA_DOMINIO);
          if(!mb.editMode) {
            this.entrate.push(p);
          } else {
            this.entrate.map((item) => {
              if (item.jsonP.idEntrata == mb.info.viewModel['idEntrata']) {
                Object.keys(p.jsonP).forEach((key) => {
                  item.jsonP[key]= p.jsonP[key];
                });
                item.model = p.model;
              }
              return item;
            });
          }
        break;
        case this._UNITA:
          p.jsonP = mb.info.viewModel;
          p.model = this._mapNewItemByType(mb.info.viewModel, this._UNITA);
          if(!mb.editMode) {
            this.unita_operative.push(p);
          } else {
            this.unita_operative.map((item) => {
              if (item.jsonP.idUnita == mb.info.viewModel['idUnita']) {
                Object.keys(p.jsonP).forEach((key) => {
                  item.jsonP[key]= p.jsonP[key];
                });
                item.model = p.model;
              }
              return item;
            });
          }
        break;
        case this._IBAN:
          p.jsonP = mb.info.viewModel;
          p.model = this._mapNewItemByType(mb.info.viewModel, this._IBAN);
          if(!mb.editMode) {
            this.iban_cc.push(p);
          } else {
            this.iban_cc.map((item) => {
              if (item.jsonP.iban == mb.info.viewModel['iban']) {
                Object.keys(p.jsonP).forEach((key) => {
                  item.jsonP[key]= p.jsonP[key];
                });
                item.model = p.model;
              }
              return item;
            });
          }
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
