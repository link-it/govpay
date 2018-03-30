import { AfterViewInit, Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { HttpResponse } from '@angular/common/http';

import { LinkService } from "../../services/link.service";
import { GovpayService } from '../../services/govpay.service';
import { UtilService } from '../../services/util.service';

import { Parameters } from '../../classes/parameters';
import { Standard } from '../../classes/view/standard';
import { Dato } from '../../classes/view/dato';

import * as moment from 'moment';
import { ModalBehavior } from '../../classes/modal-behavior';
import { Crono } from '../../classes/view/crono';

@Component({
  selector: 'link-side-list',
  templateUrl: './side-list.component.html',
  styleUrls: ['./side-list.component.scss']
})
export class SideListComponent implements OnInit, AfterViewInit {

  @Input('list-data') listResults = [];
  @Input('enable-over-actions') iconOverActions: boolean = false;
  @Input('enable-fab-actions') fabAction: boolean = false;
  @Input('is-loading-progress') _isLoading: boolean = false;
  @Output() _isLoadingChange: EventEmitter<boolean> = new EventEmitter();

  protected rsc: any;

  private _lastResponse: any;

  constructor(public ls: LinkService, public gps: GovpayService, public us: UtilService) { }

  ngOnInit() {
    this.rsc = this.ls.getRouterStateConfig();
    this.iconOverActions = this.showIconOverActions();
    this.fabAction = this.showFabAction();
    this.getList();
  }

  ngAfterViewInit() {
  }

  /**
   * Get data list
   * @param {string} service
   * @param {string} query
   * @param {boolean} concat
   */
  getList(service?: string, query?: string, concat?: boolean) {
    service = (service || this.rsc.path);
    concat = (concat || false);
    if(!this._isLoading) {
      this._isLoading = true;
      this.gps.getDataService(service, query).subscribe(
        (_response) => {
          this._lastResponse = JSON.parse(JSON.stringify(_response.body));
          this.listResults = concat?this.listResults.concat(this._mapListResults(_response)):this._mapListResults(_response);
          this.gps.updateSpinner(false);
          this._isLoading = false;
          this._isLoadingChange.emit(this._isLoading);
        },
        (error) => {
          //console.log(error);
          this.us.alert(error.message);
          this.gps.updateSpinner(false);
          this._isLoading = false;
          this._isLoadingChange.emit(this._isLoading);
        });
    }
  }

  /**
   * Get last result data
   * @returns {any}
   */
  getLastResult(): any {
    return this._lastResponse;
  }

  /**
   * Blue dialog behavior data mapper
   * @returns {ModalBehavior}
   */
  private blueDialogBehaviorDataMapper(): ModalBehavior {
    let _mb = new ModalBehavior();
    let _component;
    switch(this.rsc.path) {
      case UtilService.URL_REGISTRO_INTERMEDIARI:
        _mb.info = {
          dialogTitle: 'Nuovo intermediario',
          templateName: UtilService.INTERMEDIARIO
        };
        _component = this.ls.componentRefByName(UtilService.REGISTRO_INTERMEDIARI);
        break;
      case UtilService.URL_APPLICAZIONI:
        _mb.info = {
          dialogTitle: 'Nuova applicazione',
          templateName: UtilService.APPLICAZIONE
        };
        _component = this.ls.componentRefByName(UtilService.APPLICAZIONI);
        break;
      case UtilService.URL_OPERATORI:
        _mb.info = {
          dialogTitle: 'Nuovo operatore',
          templateName: UtilService.OPERATORE
        };
        _component = this.ls.componentRefByName(UtilService.OPERATORI);
        break;
      case UtilService.URL_DOMINI:
        _mb.info = {
          dialogTitle: 'Nuovo dominio',
          templateName: UtilService.DOMINIO
        };
        _component = this.ls.componentRefByName(UtilService.DOMINI);
        break;
      case UtilService.URL_ACL:
        _mb.info = {
          dialogTitle: 'Nuovo Acl',
          templateName: UtilService.ACL
        };
        _component = this.ls.componentRefByName(UtilService.ACLS);
        break;
      case UtilService.URL_ENTRATE:
        _mb.info = {
          dialogTitle: 'Nuova entrata',
          templateName: UtilService.ENTRATA
        };
        _component = this.ls.componentRefByName(UtilService.ENTRATE);
        break;
      case UtilService.URL_INCASSI:
        _mb.info = {
          dialogTitle: 'Nuovo incasso',
          templateName: UtilService.INCASSO
        };
        _component = this.ls.componentRefByName(UtilService.INCASSI);
        break;
      default:
        return null;
    }
    _mb.closure = this.refresh.bind(this);
    _mb.async_callback = _component.instance.save.bind(_component.instance);

    return _mb;
  }

  protected _mapListResults(response: HttpResponse<any>): any[] {
    let _mappedData: any;
    let _results = response.body['risultati']?response.body['risultati']:response.body;
    _mappedData = _results.map(function (item) {
      let _mappedElement = new Parameters();
      _mappedElement.model = this.mapNewItem(item);
      _mappedElement.jsonP = item;
      _mappedElement.type = this.classTemplate();
      return _mappedElement;
    }, this);

    return _mappedData;
  }

  protected _risultati(value: number = 0) {
    return (value != 1)?`Trovati ${value} risultati`:`Trovato ${value} risultato`;
  }

  protected _livClick(ref: any) {
    //console.log('click-tap', ref);
    let _url = this.ls.getRouterUrl()+'/dettaglio';
    let _rc = this.ls.getRouterStateConfig(_url);
    if(_rc) {
      let _ivm = ref.getItemViewModel();
      _rc.data.title = _ivm.model.getStandardTitle();
      _rc.data.info = _ivm.jsonP;
      this.ls.setRouterStateConfigData(_rc.data, _url);
      this.ls.navigateTo([_url]);
    }
  }

  protected _iconClick(event: any) {
    let _ivm = event.target.getItemViewModel();
    //console.log('icon-click', _ivm, event);
    let _rc = this.ls.getRouterStateConfig();
    let _service = null;
    let _json = null;
    let _query = null;
    let _method = null;
    switch(event.type) {
      case 'delete': {
        _method = UtilService.METHODS.DELETE;
        switch(_rc.data.type) {
          case UtilService.ACLS:
            _service = UtilService.URL_ACL+'/'+_ivm.jsonP.id;
            break;
        }
        break;
      }
    }
    if(_service) {
      this.doIconAction(event.type, _rc.data.type, _service, _json, _query, _method);
    } else {
      console.warn('Service url unavailable.')
    }
  }

  protected doIconAction(action: string, type: string, service: string, json?: any, query?: any, method?: string) {
    this.gps.saveData(service, json, query, method).subscribe(
      () => {
        this.gps.updateSpinner(false);
        this.getList();
        /*switch(action) {
          case 'delete': {
            switch(type) {
              case UtilService.ACLS:
                break;
            }
            break;
          }
        }*/
      },
      (error) => {
        this.gps.updateSpinner(false);
        this.us.alert(error.message);
      });
  }

  protected showIconOverActions(): boolean {
    let _iconOverActions: boolean = false;
    switch(this.rsc.path) {
      case UtilService.URL_PENDENZE:
      case UtilService.URL_PAGAMENTI:
      case UtilService.URL_ACL:
        _iconOverActions = true;
        break;
      default:
    }

    return _iconOverActions;
  }

  protected _overIcons(): string[] {
    let _icons: string[] = [];
    switch(this.rsc.path) {
      case UtilService.URL_ACL:
        _icons = ['delete'];
        break;
      default:
        _icons = ['file_download'];
    }

    return _icons;
  }

  protected showFabAction(): boolean {
    let _fabAction: boolean = false;
    switch(this.rsc.path) {
      case UtilService.URL_REGISTRO_INTERMEDIARI:
      case UtilService.URL_APPLICAZIONI:
      case UtilService.URL_OPERATORI:
      case UtilService.URL_DOMINI:
      case UtilService.URL_ACL:
      case UtilService.URL_INCASSI:
        _fabAction = true;
        break;
      default:
    }

    return _fabAction;
  }

  protected _fabAction() {
    let _mb: ModalBehavior = this.blueDialogBehaviorDataMapper();
    if(_mb) {
      switch (_mb.info.templateName) {
        case UtilService.ENTRATA:
        case UtilService.INCASSO:
          UtilService.dialogBehavior.next(_mb);
          break;
        default:
          UtilService.blueDialogBehavior.next(_mb);
      }
    } else {
      this.us.alert('Servizio non disponibile');
    }
  }

  protected classTemplate(_service?: string): string {
    _service = (_service || this.rsc.path);
    let _classTemplate = '';
    switch(_service) {
      case UtilService.URL_RPPS:
        _classTemplate = UtilService.CRONO;
      break;
    }

    return _classTemplate;
  }

  protected mapNewItem(item: any): Standard {
    let _std = new Standard();
    let _st, _date;
    switch(this.rsc.path) {
      case UtilService.URL_PENDENZE:
        _std.titolo = new Dato({ label: '',  value: item.nome });
        _std.sottotitolo = new Dato({ label: 'ID pendenza: ',  value: item.idPendenza });
        _std.importo = item.importo;
        _std.stato = UtilService.STATI_PENDENZE[item.stato];
        break;
      case UtilService.URL_PAGAMENTI:
        _date = UtilService.defaultDisplay({ value: moment(item.dataEsecuzionePagamento).format('DD/MM/YYYY') });
        _st = Dato.arraysToDato(
          [ 'C.F. versante', 'Data esecuzione' ],
          [ UtilService.defaultDisplay({ value: (item.soggettoVersante)?item.soggettoVersante.identificativo:null }), _date ],
          ', '
        );
        _std.titolo = new Dato({ label: '',  value: item.id });
        _std.sottotitolo = _st;
        _std.importo = item.importo;
        _std.stato = UtilService.STATI_PAGAMENTO[item.stato];
        break;
      case UtilService.URL_REGISTRO_PSP:
        _std.titolo = new Dato({ label: '',  value: item.ragioneSociale });
        _std.sottotitolo = new Dato({ label: 'Codice: ', value: item.idPsp });
        break;
      case UtilService.URL_REGISTRO_INTERMEDIARI:
        _std.titolo = new Dato({ label: 'Denominazione', value: item.denominazione });
        _std.sottotitolo = new Dato({ label: 'Id intermediario: ', value: item.idIntermediario });
        break;
      case UtilService.URL_APPLICAZIONI:
        _st = Dato.arraysToDato(
          ['Id A2A', 'Abilitato'],
          [ item.idA2A, UtilService.ABILITA[item.abilitato.toString()] ],
          ', '
        );
        _std.titolo = new Dato({ label: '',  value: item.principal });
        _std.sottotitolo = _st;
        break;
      case UtilService.URL_INCASSI:
        _std.titolo = new Dato({ label: 'Incasso', value: item.idIncasso });
        _std.sottotitolo = new Dato({ label: 'Causale', value: item.causale });
        _std.importo = item.importo;
        break;
      case UtilService.URL_GIORNALE_EVENTI:
        _st = Dato.arraysToDato(
          [ 'Codice di pagamento (CCP)', 'IUV', 'Id dominio' ],
          [ item.ccp, item.iuv, item.idDominio ],
          ', '
        );
        _std.titolo = new Dato({ label: '',  value: item.tipoEvento });
        _std.sottotitolo = _st;
        break;
      case UtilService.URL_RISCOSSIONI:
        _st = Dato.arraysToDato(
          ['Pendenza', 'IUV', 'Id dominio'],
          [ item.idVocePendenza, item.iuv, item.idDominio ],
          ', '
        );
        _std.titolo = new Dato({ label: 'Riscossione (IUR): ', value: item.iur });
        _std.sottotitolo = _st;
        _std.importo = item.importo;
        break;
      case UtilService.URL_RENDICONTAZIONI:
        _st = Dato.arraysToDato(
          ['Data incasso', 'Id PSP'],
          [ UtilService.defaultDisplay({ value: moment(item.dataRegolamento).format('DD/MM/YYYY') }) , item.idPsp ],
          ', '
        );
        _std.titolo = new Dato({ label: '',  value: item.idFlusso });
        _std.sottotitolo = _st;
        _std.importo = item.importoTotale;
        break;
      case UtilService.URL_OPERATORI:
        _st = Dato.arraysToDato(
          ['Ragione sociale', 'Abilitato'],
          [ item.ragioneSociale, UtilService.ABILITA[item.abilitato.toString()] ],
          ', '
        );
        _std.titolo = new Dato({ label: '',  value: item.principal });
        _std.sottotitolo = _st;
        break;
      case UtilService.URL_DOMINI:
        _std.titolo = new Dato({ label: '',  value: item.ragioneSociale });
        _std.sottotitolo = new Dato({ label: 'Id: ', value: item.idDominio });
        break;
      case UtilService.URL_ACL:
        let al = ['Servizio', 'Autorizzazioni'];
        let av = [
          item.servizio,
          item.autorizzazioni.join(', ')
        ];
        let _aclTitle = UtilService.defaultDisplay({ value: item.principal, text: '-' });
        if(_aclTitle === '-') {
          _aclTitle = UtilService.defaultDisplay({ value: item.ruolo });
        }
        _st = Dato.arraysToDato(al, av, ', ');
        _std.titolo = new Dato({ label: _aclTitle, value: '' });
        _std.sottotitolo = _st;
        break;
      case UtilService.URL_ENTRATE:
        _st = Dato.arraysToDato(
          ['Id', 'Tipo contabilità'],
          [ item.idEntrata, item.tipoContabilita ],
          ', '
        );
        _std.titolo = new Dato({ label: '',  value: item.descrizione });
        _std.sottotitolo = _st;
        break;
      case UtilService.URL_RPPS:
        _date = UtilService.defaultDisplay({ value:moment(item.dataRichiesta).format('DD/MM/YYYY') });
        let _crn = new Crono();
        _crn.data = _date;
        _crn.titolo = new Dato({ label: '',  value: item.iuv });
        _crn.sottotitolo = new Dato({ label: 'CCP', value: item.ccp });
        _crn.stato = UtilService.STATI_ESITO_PAGAMENTO[item.esito];
        _std = _crn;
        break;
    }
    return _std;
  }

  //TODO: Decidere se aggiungere un item alle liste in base allo switch o ricaricare i dati
  protected refresh(mb: ModalBehavior) {
    if(mb && mb.info.viewModel) {
      let json = mb.info.viewModel;
      let _mappedElement;
      switch(mb.info.templateName) {
        case UtilService.INTERMEDIARIO:
          _mappedElement = new Parameters();
          _mappedElement.model = this.mapNewItem(json);
          _mappedElement.jsonP = json;
          _mappedElement?this.listResults.push(_mappedElement):null;
        break;
        default:
          this.getList();
      }
    }
  }
}
