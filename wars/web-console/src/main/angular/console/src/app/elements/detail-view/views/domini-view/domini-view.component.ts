import { AfterViewInit, Component, Input, OnInit, Pipe, PipeTransform } from '@angular/core';
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
import { SimpleListItem } from '../../../simple-list-card/simple-list-card.component';

@Component({
  selector: 'link-domini-view',
  templateUrl: './domini-view.component.html',
  styleUrls: ['./domini-view.component.scss']
})
export class DominiViewComponent implements IModalDialog, OnInit, AfterViewInit {

  @Input() informazioni = [];
  @Input() informazioniPA = [];
  @Input() informazioniExtra = [];
  @Input() iban_cc = [];
  @Input() entrate = [];
  @Input() unita_operative = [];
  @Input() tipiPendenza = [];
  @Input() connettori = [];

  @Input() json: any;
  @Input() modified: boolean = false;
  @Input() parent: any;

  protected NO_LOGO: string = '#';
  protected logo: any = null;
  protected logoError: boolean = false;
  protected _hasAssociazioniPendenza: boolean = false;
  protected _isIntermediato: boolean = false;

  protected _IBAN = UtilService.IBAN_ACCREDITO;
  protected _ENTRATA_DOMINIO = UtilService.ENTRATA_DOMINIO;
  protected _TIPI_PENDENZA_DOMINIO = UtilService.TIPI_PENDENZA_DOMINIO;
  protected _CONNETTORE_MY_PIVOT = UtilService.CONNETTORE_MY_PIVOT;
  protected _CONNETTORE_SECIM = UtilService.CONNETTORE_SECIM;
  protected _CONNETTORE_GOVPAY = UtilService.CONNETTORE_GOVPAY;
  protected _CONNETTORE_HYPERSIC = UtilService.CONNETTORE_HYPERSIC;
  protected _UNITA = UtilService.UNITA_OPERATIVA;
  protected _PLUS_CREDIT = UtilService.USER_ACL.hasCreditore;

  protected _paginatorOptions: any = {
    unita: { page: 0, pages: 1, risultati: [], base: '', url: '', search: '', refresh: false },
    iban: { page: 0, pages: 1, risultati: [], base: '', url: '', search: '', refresh: false },
    entrate: { page: 0, pages: 1, risultati: [], base: '', url: '', search: '', refresh: false },
    pendenze: { page: 0, pages: 1, risultati: [], base: '', url: '', search: '', refresh: false }
  };
  protected _availableConnectors: SimpleListItem[] = [];

  constructor(private _sanitizer: DomSanitizer, public gps: GovpayService, public us: UtilService) { }

  ngOnInit() {
    this._paginatorOptions.iban.base = this.json.contiAccredito;
    this._paginatorOptions.unita.base = this.json.unitaOperative;
    this._paginatorOptions.entrate.base = this.json.entrate;
    this._paginatorOptions.pendenze.base = this.json.tipiPendenza;
    this.dettaglioDominio();
  }

  ngAfterViewInit() {
  }

  protected dettaglioDominio() {
    const _url = UtilService.URL_DOMINI+'/'+UtilService.EncodeURIComponent(this.json.idDominio);
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

  protected associazioniDisponibili() {
    let _url = UtilService.URL_TIPI_PENDENZA + '?&descrizione=&nonAssociati=' + UtilService.EncodeURIComponent(this.json.idDominio);
    _url += '&'+UtilService.QUERY_METADATI_PAGINAZIONE+'&'+UtilService.QUERY_ESCLUDI_RISULTATI;
    this.gps.getDataService(_url).subscribe(
      function (_response) {
        this._hasAssociazioniPendenza = (_response.body && _response.body.numRisultati > 0);
        this.gps.updateSpinner(false);
      }.bind(this),
      (error) => {
        this._hasAssociazioniPendenza = false;
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

  protected elencoMultiplo(isIntermediato: boolean) {
    this._paginatorOptions.iban.url = this._paginatorOptions.iban.base+'?risultatiPerPagina=5&ordinamento=ibanAccredito';
    this._paginatorOptions.unita.url = this._paginatorOptions.unita.base+'?risultatiPerPagina=5&ordinamento=ragioneSociale';
    this._paginatorOptions.entrate.url = this._paginatorOptions.entrate.base+'?risultatiPerPagina=5&ordinamento=descrizione';
    this._paginatorOptions.pendenze.url = this._paginatorOptions.pendenze.base+'?risultatiPerPagina=5&ordinamento=descrizione';
    const reqs: string[] = [ this._paginatorOptions.iban.url ];
    if (isIntermediato) {
      reqs.push(this._paginatorOptions.unita.url, this._paginatorOptions.entrate.url, this._paginatorOptions.pendenze.url);
    }
    this.gps.updateSpinner(true);
    this.gps.forkService(reqs).subscribe(
    (_responses :any) => {
      this.gps.updateSpinner(false);
      const _options: any = {
        iban: _responses[0].body,
        unita: (isIntermediato)?_responses[1].body:null,
        entrate: (isIntermediato)?_responses[2].body:null,
        pendenze: (isIntermediato)?_responses[3].body:null
      };
      this.updateOptions(_options);
    },
    (error: any) => {
      this.gps.updateSpinner(false);
      this.us.onError(error);
    });
  }

  protected updateOptions(options: any, type: string = 'all') {
    if (type === this._IBAN || type === 'all') {
      this._paginatorOptions.iban.pages = options.iban.numPagine || 1;
      this._paginatorOptions.iban.page = options.iban.pagina || 1;
      this.elencoAccreditiIban(options.iban.risultati);
    }
    if (options.unita && (type === this._UNITA || type === 'all')) {
      this._paginatorOptions.unita.pages = options.unita.numPagine || 1;
      this._paginatorOptions.unita.page = options.unita.pagina || 1;
      this.elencoUnitaOperative(options.unita.risultati);
    }
    if (options.entrate && (type === this._ENTRATA_DOMINIO || type === 'all')) {
      this._paginatorOptions.entrate.pages = options.entrate.numPagine || 1;
      this._paginatorOptions.entrate.page = options.entrate.pagina || 1;
      this.elencoEntrateDominio(options.entrate.risultati);
    }
    if (options.pendenze && (type === this._TIPI_PENDENZA_DOMINIO || type === 'all')) {
      this._paginatorOptions.pendenze.pages = options.pendenze.numPagine || 1;
      this._paginatorOptions.pendenze.page = options.pendenze.pagina || 1;
      this.elencoTipiPendenzaDominio(options.pendenze.risultati);
    }
  }

  protected paginatorLoader(event: any, type: string) {
    this._paginatorOptions.unita.refresh = false;
    this._paginatorOptions.iban.refresh = false;
    this._paginatorOptions.entrate.refresh = false;
    this._paginatorOptions.pendenze.refresh = false;
    let url = '';
    if (type === this._UNITA) {
      url = this._paginatorOptions.unita.url + '&pagina=' + event.pagina;
      url += (event.search !== '')?'&ragioneSociale=' + event.search:'';
    }
    if (type === this._IBAN) {
      url = this._paginatorOptions.iban.url + '&pagina=' + event.pagina;
      url += (event.search !== '')?'&iban=' + event.search:'';
    }
    if (type === this._ENTRATA_DOMINIO) {
      url = this._paginatorOptions.entrate.url + '&pagina=' + event.pagina;
      url += (event.search !== '')?'&descrizione=' + event.search:'';
    }
    if (type === this._TIPI_PENDENZA_DOMINIO) {
      url = this._paginatorOptions.pendenze.url + '&pagina=' + event.pagina;
      url += (event.search !== '')?'&descrizione=' + event.search:'';
    }
    this.gps.getDataService(url).subscribe(
      (response) => {
        this.gps.updateSpinner(false);
        const _options: any = { unita: '', iban: '', entrate: '', pendenze: '' };
        if (type === this._UNITA) {
          _options.unita = response.body;
        }
        if (type === this._IBAN) {
          _options.iban = response.body;
        }
        if (type === this._ENTRATA_DOMINIO) {
          _options.entrate = response.body;
        }
        if (type === this._TIPI_PENDENZA_DOMINIO) {
          _options.pendenze = response.body;
        }
        this.updateOptions(_options, type);
      },
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

  protected mapJsonDetail(json: any) {
    let _dettaglio = { info: [], infoPA: [], infoExtra: [], iban: [], entrate: [], unita: [] };
    if (json.intermediato === undefined || json.intermediato === null) {
      json.intermediato = true;
    }
    if (json.ragioneSociale) {
      _dettaglio.info.push(new Dato({ label: Voce.RAGIONE_SOCIALE, value: json.ragioneSociale }));
    }
    if (json.area) {
      _dettaglio.info.push(new Dato({ label: Voce.AREA, value: json.area }));
    }
    if (json.idDominio) {
      _dettaglio.info.push(new Dato({ label: Voce.ID_DOMINIO, value: json.idDominio }));
    }
    if (json.indirizzo) {
      _dettaglio.info.push(new Dato({ label: Voce.INDIRIZZO, value: json.indirizzo }));
    }
    if (json.civico) {
      _dettaglio.info.push(new Dato({ label: Voce.CIVICO, value: json.civico }));
    }
    if (json.cap) {
      _dettaglio.info.push(new Dato({ label: Voce.CAP, value: json.cap }));
    }
    if (json.localita) {
      _dettaglio.info.push(new Dato({ label: Voce.LUOGO, value: json.localita }));
    }
    if (json.provincia) {
      _dettaglio.info.push(new Dato({ label: Voce.PROVINCIA, value: json.provincia }));
    }
    if (json.nazione) {
      _dettaglio.info.push(new Dato({ label: Voce.NAZIONE, value: json.nazione }));
    }
    if (json.email) {
      _dettaglio.info.push(new Dato({ label: Voce.EMAIL, value: json.email }));
    }
    if (json.pec) {
      _dettaglio.info.push(new Dato({ label: Voce.PEC, value: json.pec }));
    }
    if (json.tel) {
      _dettaglio.info.push(new Dato({ label: Voce.TELEFONO, value: json.tel }));
    }
    if (json.fax) {
      _dettaglio.info.push(new Dato({ label: Voce.FAX, value: json.fax }));
    }
    if (json.web) {
      _dettaglio.info.push(new Dato({ label: Voce.WEB_SITE, value: json.web }));
    }
    _dettaglio.info.push(new Dato({ label: Voce.ABILITATO, value: UtilService.ABILITA[json.abilitato.toString()] }));
    _dettaglio.info.push(new Dato({ label: Voce.INTERMEDIATO, value: UtilService.ABILITA[json.intermediato.toString()] }));

    if (json.intermediato) {
      if (json.stazione) {
        _dettaglio.infoPA.push(new Dato({ label: Voce.ID_STAZIONE, value: json.stazione }));
      }
      if (json.cbill) {
        _dettaglio.infoPA.push(new Dato({ label: Voce.CODICE_INTERBANCARIO, value: json.cbill }));
      }
      if (json.auxDigit) {
        _dettaglio.infoPA.push(new Dato({ label: Voce.AUX_DIGIT, value: json.auxDigit }));
      }
      if (json.segregationCode) {
        _dettaglio.infoPA.push(new Dato({ label: Voce.SECRET_CODE, value: json.segregationCode }));
      }
      if (json.autStampaPosteItaliane) {
        _dettaglio.infoExtra.push(new Dato({ label: Voce.AUT_PT, value: json.autStampaPosteItaliane }));
      }
      if (json.iuvPrefix) {
        _dettaglio.infoExtra.push(new Dato({ label: Voce.IUV_SINTAX, value: json.iuvPrefix }));
      }
    }

    this.logoError = false;
    this.logo = json.logo?this._sanitizer.bypassSecurityTrustUrl(UtilService.RootByTOA()+json.logo):this.NO_LOGO;

    this.informazioni = _dettaglio.info.slice(0);
    this.informazioniPA = _dettaglio.infoPA.slice(0);
    this.informazioniExtra = _dettaglio.infoExtra.slice(0);

    const _connettori: any[] = [];

    // MyPivot
    if (json.servizioMyPivot && json.servizioMyPivot.abilitato) {
      const p = new Parameters();
      p.id = this._CONNETTORE_MY_PIVOT;
      p.jsonP = json.servizioMyPivot;
      p.model = this._mapNewItemByType(json.servizioMyPivot, this._CONNETTORE_MY_PIVOT);
      _connettori.push(p);
    }
    // SECIM
    if (json.servizioSecim && json.servizioSecim.abilitato) {
      const p = new Parameters();
      p.id = this._CONNETTORE_SECIM;
      p.jsonP = json.servizioSecim;
      p.model = this._mapNewItemByType(json.servizioSecim, this._CONNETTORE_SECIM);
      _connettori.push(p);
    }
    // GOVPAY
    if (json.servizioGovPay && json.servizioGovPay.abilitato) {
      const p = new Parameters();
      p.id = this._CONNETTORE_GOVPAY;
      p.jsonP = json.servizioGovPay;
      p.model = this._mapNewItemByType(json.servizioGovPay, this._CONNETTORE_GOVPAY);
      _connettori.push(p);
    }
    // HyperSicAPKappa
    if (json.servizioHyperSicAPKappa && json.servizioHyperSicAPKappa.abilitato) {
      const p = new Parameters();
      p.id = this._CONNETTORE_HYPERSIC;
      p.jsonP = json.servizioHyperSicAPKappa;
      p.model = this._mapNewItemByType(json.servizioHyperSicAPKappa, this._CONNETTORE_HYPERSIC);
      _connettori.push(p);
    }
    this.connettori = [].concat(_connettori);
    this.filtroConnettori();

    this.associazioniDisponibili();

    this.elencoMultiplo(json.intermediato);
    this._isIntermediato = json.intermediato;
  }

  protected elencoUnitaOperative(jsonList: any[]) {
    let p: Parameters;
    let _duo = jsonList.map(function(item) {
      p = new Parameters();
      p.jsonP = item;
      p.model = this._mapNewItemByType(item, this._UNITA);
      return p;
    }, this);
    this.unita_operative = _duo.slice(0);
  }

  protected elencoAccreditiIban(jsonList: any[]) {
    let p: Parameters;
    let _di = jsonList.map(function(item) {
      p = new Parameters();
      p.jsonP = item;
      p.model = this._mapNewItemByType(item, this._IBAN);
      return p;
    }, this);
    this.iban_cc = _di.slice(0);
  }

  protected elencoEntrateDominio(jsonList: any[]) {
    let p: Parameters;
    let _de = jsonList.map(function(item) {
      p = new Parameters();
      p.jsonP = item;
      p.model = this._mapNewItemByType(item, this._ENTRATA_DOMINIO);
      return p;
    }, this);
    this.entrate = _de.slice(0);
  }

  protected elencoTipiPendenzaDominio(jsonList: any[]) {
    let p: Parameters;
    let _de = jsonList.map(function(item) {
      p = new Parameters();
      p.jsonP = item;
      p.model = this._mapNewItemByType(item, this._TIPI_PENDENZA_DOMINIO);
      return p;
    }, this);
    this.tipiPendenza = _de.slice(0);
  }

  protected _onError(event) {
    this.logoError = true;
    console.warn('Image not available.')
  }

  protected _editDominio(event: any) {
    let _mb = new ModalBehavior();
    _mb.editMode = true;
    _mb.info = {
      viewModel: this.json,
      dialogTitle: Voce.MODIFICA_ENTE_CREDITORE,
      templateName: UtilService.DOMINIO
    };
    _mb.async_callback = this.save.bind(this);
    _mb.closure = this.refresh.bind(this);
    UtilService.blueDialogBehavior.next(_mb);
  }

  protected _iconCardClick(type: string, event: any) {
    this._iconClick(type, event.item, event.bubbleEvent);
  }

  protected _iconClick(type: string, ref: any, event: any) {
    let _ivm = ref.getItemViewModel();
    switch(event.type) {
      case 'edit':
        this._addEdit(type, true, _ivm.jsonP);
        break;
      case 'delete':
        if (type === this._CONNETTORE_MY_PIVOT) {
          delete this.json.servizioMyPivot;
        }
        if (type === this._CONNETTORE_SECIM) {
          delete this.json.servizioSecim;
        }
        if (type === this._CONNETTORE_GOVPAY) {
          delete this.json.servizioGovPay;
        }
        if (type === this._CONNETTORE_HYPERSIC) {
          delete this.json.servizioHyperSicAPKappa;
        }
        delete this.json.entrate;
        delete this.json.unitaOperative;
        delete this.json.contiAccredito;
        delete this.json.tipiPendenza;
        this.__updateConnettoriDominio();
       break;
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
      case this._TIPI_PENDENZA_DOMINIO:
        _std.titolo = new Dato({ value: item.descrizione });
        _std.sottotitolo = new Dato({ label: Voce.ID_TIPO_PENDENZA+': ', value: item.idTipoPendenza });
      break;
      case this._UNITA:
        _std.titolo = new Dato({ value: item.ragioneSociale });
        _std.sottotitolo = new Dato({ label: Voce.ID_UNITA+': ', value: item.idUnita });
      break;
      case this._IBAN:
        _std.titolo = new Dato({ value: item.iban });
        _std.sottotitolo = item.descrizione?new Dato({ label: Voce.DESCRIZIONE+': ', value: item.descrizione }):new Dato();
      break;
      case this._CONNETTORE_MY_PIVOT:
        _std.titolo = new Dato({ value: 'MyPivot' });
        _std.sottotitolo = new Dato({ label: Voce.MODALITA_CONNETTORE+': ', value: item.tipoConnettore });
      break;
      case this._CONNETTORE_SECIM:
        _std.titolo = new Dato({ value: 'SECIM' });
        _std.sottotitolo = new Dato({ label: Voce.MODALITA_CONNETTORE+': ', value: item.tipoConnettore });
      break;
      case this._CONNETTORE_GOVPAY:
        _std.titolo = new Dato({ value: 'GovPay' });
        _std.sottotitolo = new Dato({ label: Voce.MODALITA_CONNETTORE+': ', value: item.tipoConnettore });
      break;
      case this._CONNETTORE_HYPERSIC:
        _std.titolo = new Dato({ value: 'Suite HyperSIC - APKappa' });
        _std.sottotitolo = new Dato({ label: Voce.MODALITA_CONNETTORE+': ', value: item.tipoConnettore });
      break;
    }
    return _std;
  }

  protected _listCardAction(event: any) {
    this._addEdit(event.target);
  }

  protected _listCardIconClick(event: any) {
    const el: Parameters = event.item.getItemViewModel();
    this._iconClick(el.id, event.item, event.bubbleEvent);
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
          dialogTitle: (!mode)?'Nuovo tipo entrata':'Modifica tipo entrata',
          templateName: this._ENTRATA_DOMINIO
        };
        UtilService.blueDialogBehavior.next(_mb);
      break;
      case this._TIPI_PENDENZA_DOMINIO:
        _mb.info = {
          viewModel: _viewModel,
          parent: this,
          dialogTitle: (!mode)?'Nuovo tipo pendenza':'Modifica tipo pendenza',
          templateName: this._TIPI_PENDENZA_DOMINIO
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
      case this._CONNETTORE_MY_PIVOT:
      case this._CONNETTORE_SECIM:
      case this._CONNETTORE_GOVPAY:
      case this._CONNETTORE_HYPERSIC:
        _mb.info = {
          viewModel: _viewModel,
          parent: this,
          dialogTitle: (!mode)?'Nuovo connettore MyPivot':'Modifica connettore MyPivot',
          templateName: this._CONNETTORE_MY_PIVOT
        };
        if (type === this._CONNETTORE_SECIM) {
          _mb.info.dialogTitle = (!mode)?'Nuovo connettore SECIM':'Modifica connettore SECIM';
          _mb.info.templateName = this._CONNETTORE_SECIM;
        }
        if (type === this._CONNETTORE_GOVPAY) {
          _mb.info.dialogTitle = (!mode)?'Nuovo connettore GovPay':'Modifica connettore GovPay';
          _mb.info.templateName = this._CONNETTORE_GOVPAY;
        }
        if (type === this._CONNETTORE_HYPERSIC) {
          _mb.info.dialogTitle = (!mode)?'Nuovo connettore Suite HyperSIC - APKappa':'Modifica connettore Suite HyperSIC - APKappa';
          _mb.info.templateName = this._CONNETTORE_HYPERSIC;
        }
        UtilService.blueDialogBehavior.next(_mb);
        break;
    }
  }

  /**
   * Save Dominio|Entrata-Dominio|Unità operativa|Iban|tipoPendenza|Connettori (Put to: /domini/{idDominio} )
   * @param {BehaviorSubject<any>} responseService
   * @param {ModalBehavior} mb
   */
  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {
    let _id = mb.info.viewModel['idDominio'] || this.json.idDominio;
    let _service = UtilService.URL_DOMINI+'/'+UtilService.EncodeURIComponent(_id);
    let _json = null;
    switch(mb.info.templateName) {
      case UtilService.DOMINIO:
        _json = mb.info.viewModel;
        if (mb.editMode) {
          if (this.json.servizioMyPivot) {
            _json.servizioMyPivot = this.json.servizioMyPivot;
          }
          if (this.json.servizioSecim) {
            _json.servizioSecim = this.json.servizioSecim;
          }
          if (this.json.servizioGovPay) {
            _json.servizioGovPay = this.json.servizioGovPay;
          }
          if (this.json.servizioHyperSicAPKappa) {
            _json.servizioHyperSicAPKappa = this.json.servizioHyperSicAPKappa;
          }
        }
        delete _json.idDominio;
      break;
      case this._ENTRATA_DOMINIO:
        _service += UtilService.URL_ENTRATE+'/'+UtilService.EncodeURIComponent(mb.info.viewModel.idEntrata);
        _json = JSON.parse(JSON.stringify(mb.info.viewModel));
        delete _json.idEntrata;
        delete _json['tipoEntrata'];
      break;
      case this._TIPI_PENDENZA_DOMINIO:
        _service += UtilService.URL_TIPI_PENDENZA+'/'+UtilService.EncodeURIComponent(mb.info.viewModel.idTipoPendenza);
        _json = JSON.parse(JSON.stringify(mb.info.viewModel.valori));
      break;
      case this._UNITA:
        _service += UtilService.URL_UNITA_OPERATIVE+'/'+UtilService.EncodeURIComponent(mb.info.viewModel.idUnita);
        _json = JSON.parse(JSON.stringify(mb.info.viewModel));
        delete _json.idUnita;
      break;
      case this._IBAN:
        _service += UtilService.URL_IBAN_ACCREDITI+'/'+mb.info.viewModel.iban;
        _json = JSON.parse(JSON.stringify(mb.info.viewModel));
        delete _json.iban;
      break;
      case this._CONNETTORE_MY_PIVOT:
      case this._CONNETTORE_SECIM:
      case this._CONNETTORE_GOVPAY:
      case this._CONNETTORE_HYPERSIC:
        _json = JSON.parse(JSON.stringify(this.json));
        if (mb.info.templateName === this._CONNETTORE_MY_PIVOT) {
          _json.servizioMyPivot = JSON.parse(JSON.stringify(mb.info.viewModel));
        }
        if (mb.info.templateName === this._CONNETTORE_SECIM) {
          _json.servizioSecim = JSON.parse(JSON.stringify(mb.info.viewModel));
        }
        if (mb.info.templateName === this._CONNETTORE_GOVPAY) {
          _json.servizioGovPay = JSON.parse(JSON.stringify(mb.info.viewModel));
        }
        if (mb.info.templateName === this._CONNETTORE_HYPERSIC) {
          _json.servizioHyperSicAPKappa = JSON.parse(JSON.stringify(mb.info.viewModel));
        }
        delete _json.idDominio;
        delete _json.entrate;
        delete _json.unitaOperative;
        delete _json.contiAccredito;
        delete _json.tipiPendenza;
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
            let j = { e: this.json['entrate'], tp: this.json['tipiPendenza'], ia: this.json['contiAccredito'], uo: this.json['unitaOperative'] };
            let json = mb.info.viewModel;
            json['entrate'] = j.e;
            json['tipiPendenza'] = j.tp;
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
            this._paginatorOptions.entrate.search = p.jsonP.tipoEntrata.descrizione;
            this._paginatorOptions.entrate.refresh = true;
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
        case this._TIPI_PENDENZA_DOMINIO:
          p.jsonP = mb.info.viewModel;
          p.model = this._mapNewItemByType(mb.info.viewModel, this._TIPI_PENDENZA_DOMINIO);
          if(!mb.editMode) {
            this._paginatorOptions.pendenze.search = p.jsonP.descrizione;
            this._paginatorOptions.pendenze.refresh = true;
            this.associazioniDisponibili();
          } else {
            this.tipiPendenza.map((item) => {
              if (item.jsonP.idTipoPendenza == mb.info.viewModel['idTipoPendenza']) {
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
            this._paginatorOptions.unita.search = p.jsonP.ragioneSociale;
            this._paginatorOptions.unita.refresh = true;
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
            this._paginatorOptions.iban.search = p.jsonP.iban;
            this._paginatorOptions.iban.refresh = true;
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
        case this._CONNETTORE_MY_PIVOT:
        case this._CONNETTORE_SECIM:
        case this._CONNETTORE_GOVPAY:
        case this._CONNETTORE_HYPERSIC:
          this.dettaglioDominio();
        break;
      }
    }
  }

  __updateConnettoriDominio() {
    const _service = UtilService.URL_DOMINI+'/'+UtilService.EncodeURIComponent(this.json.idDominio);
    const _json = JSON.parse(JSON.stringify(this.json));
    delete _json.idDominio;
    if(_json && _service) {
      this.gps.saveData(_service, _json).subscribe(
        () => {
          this.gps.updateSpinner(false);
          this.dettaglioDominio();
        },
        (error) => {
          this.gps.updateSpinner(false);
          this.us.onError(error);
        });
    }
  }

  filtroConnettori() {
    this._availableConnectors = UtilService.CONNETTORI.filter((el: SimpleListItem) => {
      let visible: boolean = true;
      this.connettori.forEach(c => {
        if (c.id === el.value) {
          visible = false;
        }
      });
      return visible;
    });
  }

  title(): string {
    return UtilService.defaultDisplay({ value: this.json?this.json.ragioneSociale:null });
  }

  infoDetail(): any {
    return null;
  }
}

