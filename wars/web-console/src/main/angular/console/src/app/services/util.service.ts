import { ComponentRef, Injectable } from '@angular/core';
import { MatDialog, MatDialogConfig, MatSnackBar, MatSnackBarConfig } from '@angular/material';
import { FormInput } from '../classes/view/form-input';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { ModalBehavior } from '../classes/modal-behavior';
import { FormService } from './form.service';
import { Subscription } from 'rxjs/Subscription';

import * as moment from 'moment';
import { HttpClient } from '@angular/common/http';
import { Voce } from './voce.service';

declare let GovPayConfig: any;

@Injectable()
export class UtilService {

  // Config.govpay: Autenticazione
  public static ACCESS_BASIC: string = 'Basic';
  public static ACCESS_SPID: string = 'Spid';
  public static BASIC: any = GovPayConfig.BASIC;
  public static SPID: any = GovPayConfig.SPID;
  public static TOA: any = { Spid: false, Basic: false };

  // Config.govpay
  public static INFORMATION: any = GovPayConfig.INFO;
  public static BADGE: any = GovPayConfig.BADGE_FILTER;
  public static JS_URL: string = GovPayConfig.EXTERNAL_JS_PROCEDURE_URL;


  public static TEMPORARY_DEPRECATED_CODE: boolean = false; // DEBUG VARS

  public static APPLICATION_VERSION: any;

  public static ROOT_ZIP_FOLDER: string = '-root-'; //Save to zip root folder

  public static TIMEOUT: number = 30000; //30 seconds
  public static PROFILO_UTENTE: any;
  public static METHODS: any = {
    GET: 'get',
    POST: 'post',
    PUT: 'put',
    PATCH: 'patch',
    DELETE: 'delete',
  };

  public static PATCH_METHODS: any = {
    ADD: 'ADD',
    DELETE: 'DELETE',
    REPLACE: 'REPLACE'
  };

  public static ICONS: any = {
    DOWNLOAD: 'file_download',
    EDIT: 'edit',
    DELETE: 'delete',
    ADD: 'add',
  };

  //DIRITTI
  public static USER_ACL: any = {
    hasPagoPA: false,
    hasCreditore: false,
    hasApplicazioni: false,
    hasRuoli: false,
    hasPagamentiePendenze: false,
    hasPagamenti: false,
    hasPendenze: false,
    hasRendiIncassi: false,
    hasGdE: false,
    hasConfig: false,
    hasSetting: false
  };

  public static _LABEL: any = {
    LETTURA: 'Lettura',
    SCRITTURA: 'Scrittura',
    ESECUZIONE: 'Esecuzione'
  };
  public static _CODE: any = {
    LETTURA: 'R',
    SCRITTURA: 'W',
    ESECUZIONE: 'X'
  };

  public static DIRITTI_CODE: any[] = [
    { label: UtilService._LABEL.LETTURA, code: UtilService._CODE.LETTURA },
    { label: UtilService._LABEL.SCRITTURA, code: UtilService._CODE.SCRITTURA }
    // { label: UtilService._LABEL.ESECUZIONE, code: UtilService._CODE.ESECUZIONE } // Non più usata
  ];

  //STATI PAGAMENTO
  public static STATI_PAGAMENTO: any = {
    IN_CORSO: 'Pagamento in corso',
    FALLITO: 'Pagamento rifiutato',
    ESEGUITO: 'Pagamento eseguito',
    ANNULLATO: 'Pagamento annullato',
    NON_ESEGUITO: 'Pagamento non eseguito',
    ESEGUITO_PARZIALE: 'Pagamento parzialmente eseguito'
    //DECORRENZA: 'Decorrenza termini',
    //DECORRENZA_PARZIALE: 'Decorrenza termini parziale'
  };

  //STATI PENDENZE
  public static STATI_PENDENZE: any = {
    ESEGUITA: 'Pagata',
    NON_ESEGUITA: 'Da pagare',
    ESEGUITA_PARZIALE: 'Pagata parzialmente',
    ANNULLATA: 'Annullata',
    SCADUTA: 'Scaduta',
    INCASSATA: 'Riconciliata'
  };

  //STATI RPP PAGAMENTI
  public static STATI_RPP: any = {
    FALLITO: 'Fallito',
    ANOMALO: 'Anomalo',
    IN_CORSO: 'In corso'
  };

  //STATI RISCOSSIONE
  public static STATI_RISCOSSIONE: any = {
    RISCOSSA: 'Riscossa',
    INCASSATA: 'Riconciliata'
  };

  //STATI ESITO PAGAMENTO
  public static STATI_ESITO_PAGAMENTO: any = {
    0: 'Pagamento eseguito',
    1: 'Pagamento non eseguito',
    2: 'Pagamento parzialmente eseguito',
    3: 'Decorrenza termini',
    4: 'Decorrenza termini parziale'
  };

  //STATI ESITO RENDICONTAZIONI
  public static STATI_ESITO_RENDICONTAZIONI: any = {
    0: 'Pagamento eseguito',
    3: 'Pagamento revocato',
    9: 'Pagamento eseguito senza RPT'
  };

  //STATI TRACCIATO
  public static STATI_TRACCIATO: any = {
    IN_ATTESA: 'In attesa',
    IN_ELABORAZIONE: 'In elaborazione',
    ESEGUITO: 'Eseguito',
    ESEGUITO_CON_ERRORI: 'Eseguito con errori',
    SCARTATO: 'Scartato'
  };

  //TIPOLOGIE OPERAZIONI TRACCIATO
  public static TIPO_OPERAZIONI_TRACCIATO: any = {
    ADD: { LABEL: 'Inserimento', KEY: 'ADD' },
    DEL: { LABEL: 'Annullamento', KEY: 'DEL' }
  };

  //TIPOLOGIE VERSAMENTO
  public static TIPI_VERSAMENTO: any = {
    PO: 'Pagamento da PSP',
    BP: 'Pagamento da portale',
    BBT: 'Pagamento da portale',
    CP: 'Pagamento da portale',
    AD: 'Pagamento da portale',
    OBEP: 'Pagamento da portale',
    OTH: 'Pagamento da portale'
  };

  //MODELLI PAGAMENTO
  public static MODELLI_PAGAMENTO: any = {
    0: 'Immediato',
    1: 'Immediato multibeneficiario',
    2: 'Differito',
    4: 'Attivato presso Psp'
  };

  //TIPOLOGIE CATEGORIA EVENTO
  public static TIPI_CATEGORIA_EVENTO: any = {
    INTERNO: 'Interno',
    INTERFACCIA: 'Interfaccia'
  };

  //TIPI VERSIONE
  public static TIPI_VERSIONE_API: string[] = [];
  //MAPPATURA TIPI EVENTO (GIORNALE)
  static set MAP_TIPI_EVENTO(value: any[]) {
    this._MAP_TIPI_EVENTO = value.map(te => {
      const _key = Object.keys(te)[0];
      return { value: _key, label: te[_key] };
    }).sort((a, b) => {
      if (a.label > b.label) {
        return 1;
      }
      if (a.label < b.label) {
        return -1;
      }
      return 0;
    });
    if(value) {
      UtilService.DIRECT_MAP_TIPI_EVENTO = {};
      this._MAP_TIPI_EVENTO.forEach(e => {
        UtilService.DIRECT_MAP_TIPI_EVENTO[e.value] = e.label;
      });
    }
  }
  private static _MAP_TIPI_EVENTO: any[] = [];
  public static DIRECT_MAP_TIPI_EVENTO: any;
  public static COMPONENTI_EVENTO: any;

  //LISTA OPERAZIONI ENTRATE
  public static AUTODETERMINAZIONE_ENTRATE: any = { label: 'Autodeterminazione delle Entrate', value: 'autodeterminazione'};
  public static TUTTE_ENTRATE: any = { label: 'Tutte', value: '*'};

  //LISTA OPERAZIONI TIPI PENDENZA
  public static AUTODETERMINAZIONE_TIPI_PENDENZA: any = { label: 'Autodeterminazione tipi pendenza', value: 'autodeterminazione'};
  public static TUTTI_TIPI_PENDENZA: any = { label: 'Tutti', value: '*'};
  public static TIPOLOGIA_PENDENZA: any[] = [ { label: 'Spontaneo', value: 'spontaneo'}, { label: 'Dovuto', value: 'dovuto'} ];
  private static _ID_TIPI_PENDENZA: any[] = [];
  static set ID_TIPI_PENDENZA(value: any[]) {
    UtilService._ID_TIPI_PENDENZA = value.map(itp => {
      return { value: itp.idTipoPendenza, label: itp.descrizione };
    }).sort((a, b) => {
      if (a.label > b.label) {
        return 1;
      }
      if (a.label < b.label) {
        return -1;
      }
      return 0;
    });
  }

  //LISTA OPERAZIONI DOMINI
  public static TUTTI_DOMINI: any = { label: 'Tutti', value: '*'};
  public static TUTTE_UNITA_OPERATIVE: any = { label: 'Tutte', value: '*'};
  public static NESSUNA_UNITA_OPERATIVA: any = { label: 'Nessuna', value: null};

  //LISTA SERVIZI
  public static SERVIZI: string[] = [];
  public static CONFIGURAZIONE_E_MANUTENZIONE: string = 'Configurazione e manutenzione';
  public static PAGAMENTI_ACL_LABEL: string = 'Pagamenti';
  public static PENDENZE_ACL_LABEL: string = 'Pendenze';

  //TIPI SOGGETTO
  public static TIPI_SOGGETTO: any = {
    'F': 'Persona fisica',
    'G': 'Persona giuridica'
  };

  //TIPI RISCOSSIONE
  public static TIPI_RISCOSSIONE: any = {
    'ENTRATA': 'Entrata in tesoreria',
    'MBT': 'Marca da bollo telematica'
  };

  //RUOLI GIORNALE DEGLI EVENTI
  public static RUOLI_GDE: any = {
    'SERVER': 'Server',
    'CLIENT': 'Client'
  };

  //ESITI GIORNALE DEGLI EVENTI
  public static ESITI_GDE: any = {
    'OK': 'OK',
    'KO': 'KO',
    'FAIL': 'Fail'
  };

  //ABILITAZIONI
  public static ABILITAZIONI: any = {
    'true': 'Abilitato',
    'false': 'Disabilitato'
  };
  public static ABILITA: any = {
    'true': 'Si',
    'false': 'No'
  };

  public static TIPI_AUTENTICAZIONE: any = {
    basic: 'HTTP Basic',
    ssl: 'SSL'
  };

  public static TIPI_SSL: any = {
    client: 'Client',
    server: 'Server'
  };

  public static TIPI_CONTABILITA: any = {
    CAPITOLO: 'Capitolo',
    SPECIALE: 'Speciale',
    SIOPE: 'Siope',
    ALTRO: 'Altro'
  };

  //VERIFICA
  public static VERIFICHE: any = {
    'true': 'Verificato',
    'false': 'Non verificato'
  };

  public static COOKIE_RIFIUTATI: string = 'GovPay_Dashboard_Rifiutati';
  public static COOKIE_SOSPESI: string = 'GovPay_Dashboard_Sospesi';
  public static COOKIE_SESSION: string = null;
  public static BACK_IN_TIME_DATE: string = '';

  //ROOT URL SERVIZI
  public static URL_DETTAGLIO: string = '/dettaglio';
  public static URL_PROFILO: string = '/profilo';
  public static URL_DASHBOARD: string = '/dashboard';
  public static URL_PENDENZE: string = '/pendenze';
  public static URL_PAGAMENTI: string = '/pagamenti';
  public static URL_REGISTRO_INTERMEDIARI: string = '/intermediari';
  public static URL_RPPS: string = '/rpp';
  public static URL_APPLICAZIONI: string = '/applicazioni';
  public static URL_STAZIONI: string = '/stazioni';
  public static URL_ENTRATE: string = '/entrate';
  public static URL_TIPI_PENDENZA: string = '/tipiPendenza';
  public static URL_DOMINI: string = '/domini';
  public static URL_OPERATORI: string = '/operatori';
  public static URL_ACLS: string = '/acl';
  public static URL_GIORNALE_EVENTI: string = '/eventi';
  public static URL_RISCOSSIONI: string = '/riscossioni';
  public static URL_RENDICONTAZIONI: string = '/flussiRendicontazione';
  public static URL_INCASSI: string = '/incassi';
  public static URL_UNITA_OPERATIVE: string = '/unitaOperative';
  public static URL_IBAN_ACCREDITI: string = '/contiAccredito';
  public static URL_RUOLI: string = '/ruoli';
  //Operazioni
  public static URL_OPERAZIONI: string = '/operazioni';
  public static URL_ACQUISIZIONE_RENDICONTAZIONI: string = '/acquisizioneRendicontazioni';
  public static URL_RECUPERO_RPT_PENDENTI: string = '/recuperoRptPendenti';
  //Reportistiche
  public static URL_REPORTISTICHE: string = '/reportistiche';
  public static URL_PROSPETTO_RISCOSSIONI: string = '/entrate-previste';
  //Sezione Impostazioni (patch configurazioni)
  public static URL_IMPOSTAZIONI: string = '/configurazioni';

  public static URL_TRACCIATI: string = '/pendenze/tracciati';
  public static URL_AVVISI: string = '/avvisi';
  public static URL_INFO: string = '/info';

  public static URL_LOGIN_SERVICE: string = UtilService.URL_PROFILO;

  //ROOT URL SHARED SERVICES
  public static URL_SERVIZIACL: string = '/enumerazioni/serviziACL';
  public static URL_TIPI_VERSIONE_API: string = '/enumerazioni/versioneConnettore';
  public static URL_LABEL_TIPI_EVENTO: string = '/enumerazioni/labelTipiEvento';
  public static URL_COMPONENTI_EVENTO: string = '/enumerazioni/componentiEvento';

  //LABEL
  public static TXT_DASHBOARD: string = 'Cruscotto';
  public static TXT_PENDENZE: string = 'Pendenze';
  public static TXT_PAGAMENTI: string = 'Pagamenti';
  public static TXT_PROFILO: string = 'Profilo utente';
  public static TXT_REGISTRO_INTERMEDIARI: string = 'Intermediari';
  public static TXT_RPPS: string = 'Richieste di pagamento';
  public static TXT_APPLICAZIONI: string = 'Applicazioni';
  public static TXT_DOMINI: string = Voce.ENTI_CREDITORI;
  public static TXT_TIPI_PENDENZA: string = 'Tipi pendenza';
  public static TXT_ENTRATE: string = 'Entrate';
  public static TXT_RUOLI: string = 'Ruoli';
  public static TXT_OPERATORI: string = 'Operatori';
  public static TXT_GIORNALE_EVENTI: string = 'Giornale degli eventi';
  public static TXT_RISCOSSIONI: string = 'Riscossioni';
  public static TXT_RENDICONTAZIONI: string = 'Rendicontazioni';
  public static TXT_INCASSI: string = 'Riconciliazioni';

  public static TXT_TRACCIATI: string = 'Caricamento pendenze';

  public static TXT_MAN_NOTIFICHE: string = 'Spedisci notifiche';
  public static TXT_MAN_RENDICONTAZIONI: string = 'Acquisisci rendicontazioni';
  public static TXT_MAN_PAGAMENTI: string = 'Recupera pagamenti';
  public static TXT_MAN_CACHE: string = 'Resetta la cache';
  public static TXT_MAN_PROSPETTO_RISCOSSIONI: string = 'Prospetto riscossioni';
  public static TXT_IMPOSTAZIONI: string = 'Impostazioni';


  //Types
  //Component view ref
  public static DASHBOARD: string = 'dashboard';
  public static PENDENZE: string = 'pendenze';
  public static SCHEDA_PENDENZE: string = 'scheda_pendenze';
  public static PAGAMENTI: string = 'pagamenti';
  public static REGISTRO_INTERMEDIARI: string = 'registro_intermediari';
  public static TIPI_PENDENZE: string = 'tipi_pendenze';
  public static RPPS: string = 'richieste_pagamenti';
  public static APPLICAZIONI: string = 'applicazioni';
  public static DOMINI: string = 'domini';
  public static ENTRATE: string = 'entrate';
  public static ACLS: string = 'acl';
  public static RUOLI: string = 'ruoli';
  public static OPERATORI: string = 'operatori';
  public static GIORNALE_EVENTI: string = 'giornale_eventi';
  public static RISCOSSIONI: string = 'riscossioni';
  public static RENDICONTAZIONI: string = 'rendicontazioni';
  public static INCASSI: string = 'incassi';
  public static UNITA_OPERATIVE: string = 'unitaOperative';
  public static IBAN_ACCREDITI: string = 'ibanAccredito';

  public static TRACCIATI: string = 'tracciati';
  public static TRACCIATO: string = 'tracciato';
  public static OPERAZIONI_TRACCIATO: string = 'operazioni';
  public static VERIFICATO: string = 'verificato';

  //Item view ref
  public static STANDARD: string = '';
  public static STANDARD_COLLAPSE: string = 'standard_collapse';
  public static TWO_COLS: string = 'two_cols';
  public static TWO_COLS_COLLAPSE: string = 'two_cols_collapse';
  public static RIEPILOGO: string = 'riepilogo';
  public static CRONO: string = 'crono';
  public static CRONO_CODE: string = 'crono_code';
  public static KEY_VALUE: string = 'key_value';
  //Dialog view ref
  public static AUTORIZZAZIONE_ENTE_UO: string = 'autorizazione_ente_uo';
  public static INTERMEDIARIO: string = 'intermediario';
  public static STAZIONE: string = 'stazione';
  public static APPLICAZIONE: string = 'applicazione';
  public static ACL: string = 'autorizzazione';
  public static NOTA: string = 'nota';
  public static RUOLO: string = 'ruolo';
  public static DOMINIO: string = 'dominio';
  public static OPERATORE: string = 'operatore';
  public static GIORNALE_EVENTO: string = 'giornale_evento';
  public static RISCOSSIONE: string = 'riscossione';
  public static RENDICONTAZIONE: string = 'rendicontazione';
  public static PSP: string = 'psp';
  public static RPP: string = 'richiesta_pagamento';
  public static INCASSO: string = 'incasso';
  public static ENTRATA: string = 'entrata';
  public static TIPI_PENDENZA: string = 'tipi_pendenza';
  public static TIPO_PENDENZA: string = 'tipo_pendenza';
  public static TIPI_PENDENZA_DOMINIO: string = 'tipi_pendenza_dominio';
  public static ENTRATA_DOMINIO: string = 'entrata_dominio';
  public static UNITA_OPERATIVA: string = 'unita_operativa';
  public static IBAN_ACCREDITO: string = 'iban_accredito';
  public static PENDENZA: string = 'pendenza';
  public static SCHEDA_PENDENZA: string = 'scheda_pendenza';
  public static REPORT_PROSPETTO_RISCOSSIONI: string = 'report_prospetto_riscossioni';
  public static NO_TYPE: string = '-';
  //Json schema generators
  public static GENERATORI: any[] = GovPayConfig.GENERATORI;
  public static A2_JSON_SCHEMA_FORM: string = GovPayConfig.MGK.ANGULAR2_JSON_SCHEMA_FORM;
  //Material standard ref
  public static INPUT: string = 'input';
  public static FILTERABLE: string = 'filterable';
  public static DATE_PICKER: string = 'date_picker';
  public static SELECT: string = 'select';
  public static SLIDE_TOGGLE: string = 'slide_toggle';
  public static LABEL: string = 'label';
  //Sidelist item view
  public static ITEM_VIEW: string = 'item_view';

  //Behaviors
  public static profiloUtenteBehavior: BehaviorSubject<ModalBehavior> = new BehaviorSubject(null);
  public static dialogBehavior: BehaviorSubject<ModalBehavior> = new BehaviorSubject(null);
  public static blueDialogBehavior: BehaviorSubject<ModalBehavior> = new BehaviorSubject(null);
  public static headBehavior: BehaviorSubject<any> = new BehaviorSubject(null);
  public static exportBehavior: BehaviorSubject<string> = new BehaviorSubject(null);
  public static exportSubscription: Subscription;

  //Active detail state
  public static ActiveDetailState: ComponentRef<any>;

  //Header: actions list
  public static HEADER_ACTIONS: any[] = [];//[{ label: 'Esporta', type: UtilService.NO_TYPE } ];
  public static EXPORT_PENDENZA: string = 'esporta_pendenza';
  public static EXPORT_PENDENZE: string = 'esporta_pendenze';
  public static EXPORT_PAGAMENTO: string = 'esporta_pagamento';
  public static EXPORT_PAGAMENTI: string = 'esporta_pagamenti';
  public static EXPORT_GIORNALE_EVENTI: string = 'esporta_giornale_eventi';
  public static EXPORT_RISCOSSIONI: string = 'esporta_riscossioni';
  public static EXPORT_INCASSI: string = 'esporta_incassi';
  public static EXPORT_RENDICONTAZIONI: string = 'esporta_rendicontazioni';
  public static EXPORT_TRACCIATO: string = 'esporta_tracciato';
  public static ESCLUDI_NOTIFICA: string = 'escludi_notifica';
  public static VISTA_COMPLETA_EVENTO_JSON: string = 'vista_completa_evento_json';

  /**
   * Dashboard link params
   * @type { {method: string; params: Array<{ controller: '', key: '', value: '' }>} }
   */
  public static DASHBOARD_LINKS_PARAMS: any = { method: null, params: [] };


  constructor(private message: MatSnackBar, private dialog: MatDialog, private http: HttpClient) { }

  /**
   * ROUTE String
   * @param {string} value
   * @returns {string}
   * @constructor
   */
  public static ROUTE(value: string): string {
    if(value !== null && value !== undefined && value !== '') {
      return value.substring(1);
    }
    return '/';
  }

  /**
   * No value label
   * @param {any} options
   * @returns {any}
   */
  public static defaultDisplay(options: { value?: any, method?: Function, text?: any }): any {
    if((options.value !== null && options.value !== undefined && options.value != 'Invalid date')) {
      return options.value;
    } else {
      if(options.method) {
        let _mr = options.method();
        if((_mr != null && _mr !== undefined && _mr !== '')) {
          return _mr;
        }
      }
    }
    options.text = (options.text || 'n/a');

    return options.text;
  }

  /**
   * Set TOA
   * @param {string} toa: Basic | Spid
   * @param {boolean} value
   */
  public static SetTOA(toa: string, value: boolean = false) {
    UtilService.TOA[toa] = value;
  }

  public static ResetTOA() {
    UtilService.TOA = { Spid: false, Basic: false };
  }

  public static RootByTOA(): string {
    let _root = UtilService.BASIC.HTTP_ROOT_SERVICE;
    if(!UtilService.TOA.Basic && UtilService.TOA.Spid) {
      _root = UtilService.SPID.HTTPS_ROOT_SERVICE;
    }
    return _root;
  }

  public static LogoutByTOA(): string {
    let _root = UtilService.BASIC.HTTP_LOGOUT_SERVICE;
    if(!UtilService.TOA.Basic && UtilService.TOA.Spid) {
      _root = UtilService.SPID.HTTPS_LOGOUT_SERVICE;
    }
    return _root;
  }

  public static cacheUser(profilo: any) {
    UtilService.PROFILO_UTENTE = profilo;
    UtilService.profiloUtenteBehavior.next(profilo);
  }

  public static cleanUser() {
    UtilService.PROFILO_UTENTE = null;
    UtilService.profiloUtenteBehavior.next(null);
  }

  /**
   * On error handler
   * @param error
   * @param {string} customMessage
   */
  onError(error: any, customMessage?: string) {
    let _msg = 'Warning: status ' + error.status;
    try {
      switch(error.status) {
        case 401:
          UtilService.cleanUser();
          if(error.error) {
            _msg = (!error.error.dettaglio)?error.error.descrizione:error.error.descrizione+': '+error.error.dettaglio;
          } else {
            _msg = 'Accesso al servizio non autorizzato. Autenticarsi per avviare la sessione.';
          }
          break;
        case 403:
          if(!error.error) {
            UtilService.cleanUser();
            _msg = 'Accesso non autorizzato. Sessione non valida.';
          } else {
            _msg = (!error.error.dettaglio)?error.error.descrizione:error.error.descrizione+': '+error.error.dettaglio;
          }
          break;
        case 404:
          _msg = 'Servizio non disponibile.';
          break;
        case 500:
          _msg = 'Errore interno del server.';
          break;
        case 504:
          _msg = (error.error)?error.error:'Gateway Timeout.';
          break;
        default:
          if(error.error) {
            _msg = (!error.error.dettaglio)?error.error.descrizione:error.error.descrizione+': '+error.error.dettaglio;
          } else {
            _msg = customMessage?customMessage:error.message;
          }
          if(_msg.length > 200) {
            _msg = _msg.substring(0, 200);
          }
      }
    } catch(e) {
      _msg = 'Si è verificato un problema non previsto.';
    }
    this.alert(_msg);
  }

  /**
   *
   * Alert messages
   * @param {string} _message
   * @param {boolean} _action
   * @param {boolean} _keep
   */
  alert(_message: string, _action: boolean = true, _keep: boolean = false) {
    let _config: MatSnackBarConfig = new MatSnackBarConfig();
    _config.duration = 10000;
    _config.panelClass = 'overflow-hidden';
    let _actions = null;
    if (_keep) {
      _config = null;
    }
    if (_action) {
      _actions = 'Chiudi';
    }
    if(_message) {
      this.message.open(_message, _actions, _config);
    }
  }

  openDialog(component: any, _mb: ModalBehavior): void {
    const mdc: MatDialogConfig = new MatDialogConfig();
    mdc.minWidth = '50%';
    mdc.data =  _mb;
    let dialogRef = this.dialog.open(component, mdc);

    dialogRef.afterClosed().subscribe((value) => {
      if(_mb.closure) {
        _mb.closure(value);
      }
    });
  }

  /**
   * Numero in formato valuta €
   * @param {number} value
   * @returns {string}
   */
  currencyFormat(value: number): string {
    if(value && !isNaN(value)) {
      let currency;
      try {
        currency = value.toLocaleString("it-IT", { minimumFractionDigits: 2 });
      } catch (e) {
        currency = 'n/a';
      }
      return '€ '+ currency;
    }
    return '€ 0,00';
  }

  mappaturaTipoEvento(value: string): string {
    if(UtilService.DIRECT_MAP_TIPI_EVENTO[value]) {
      return UtilService.DIRECT_MAP_TIPI_EVENTO[value];
    }
    return value;
  }

  mapRiferimentoGiornale(_item : any): string {
    let s = '';
    if(_item.idDominio && _item.iuv && _item.ccp) {
      s = [_item.idDominio, _item.iuv, _item.ccp].join('/');
    } else {
      if(_item.idA2A && _item.idPendenza) {
        s = [_item.idA2A, _item.idPendenza].join('/');
      } else {
        if(_item.idPagamento) {
          s = _item.idPagamento;
        }
      }
    }
    return s;
  }

  // importoClass(_stato: string, detail: boolean = false): any {
  //   let _status = { e: false, w: false, n: false, got: false };
  //   switch(_stato) {
  //     case UtilService.STATI_PENDENZE.SCADUTO:
  //       _status.e = true;
  //       _status.got = true;
  //       break;
  //     case UtilService.STATI_PENDENZE.NON_ESEGUITO:
  //       _status.w = true;
  //       _status.got = true;
  //       break;
  //   }
  //   if(!_status.got) {
  //     switch(_stato) {
  //       case UtilService.STATI_PAGAMENTO.DECORRENZA:
  //         _status.e = true;
  //         break;
  //       case UtilService.STATI_PAGAMENTO.NON_ESEGUITO:
  //         _status.w = true;
  //         break;
  //       default:
  //         _status.n = true;
  //         break;
  //     }
  //   }
  //   return {
  //     'medium-24': detail,
  //     'medium-16': !detail,
  //     'text-truncate': true,
  //     'stato-error': _status.e,
  //     'stato-warning': _status.w,
  //     'stato-normal': _status.n,
  //   };
  // }

  /**
   * Format milliseconds to HH[h] mm[m] ss[s].SSS
   * @param duration
   * @returns {string}
   */
  formatMs(duration: number) {
    return moment.utc(duration).format("HH[h] mm[m] ss[s].SSS");
  }

  /**
   * Var is valid
   * @param value
   * @returns {boolean}
   */
  hasValue(value: any): boolean {
    return (value != null && value !== undefined && value !== '');
  }

  /**
   * Sentence capitalize
   * @param value
   * @returns {string}
   */
  sentenceCapitalize(value: string): string {
    if(value) {
      value = value.charAt(0).toUpperCase() + value.substring(1);
    }
    return value;
  }

  /**
   * Object key by value
   * @param object
   * @param value
   * @returns {string}
   */
  getKeyByValue(object, value) {
    return Object.keys(object).filter(function(key) {
      return object[key] === value
    })[0];
  }

  /**
   * Get label by value
   * @param items (any[{label: '', value: ''}])
   * @param value
   * @returns {string}
   */
  getLabelByValue(items: any[], value: string) {
    const item = items.filter(function(el) {
      return el.value === value;
    });

    return (item && item[0].label) || '';
  }

  /**
   * Get property value by path
   * @param path {string}
   * @param stack {string}
   * @returns any
   */
  searchPropertyByPathString(path: string, stack: any): any {
    try {
      path.split('.').forEach((property) => {
        stack = stack[property];
      });
    } catch (e) {
      console.warn('Stack error', stack, path);
      return 'Percorso non valido.';
    }
    return stack || 'Valore non presente.';
  }

  desaturateColor(_color: string): string {
    let col = this.hexToRgb(_color);
    let sat = 0;
    let gray = col.r * 0.3086 + col.g * 0.6094 + col.b * 0.0820;

    col.r = Math.round(col.r * sat + gray * (1-sat));
    col.g = Math.round(col.g * sat + gray * (1-sat));
    col.b = Math.round(col.b * sat + gray * (1-sat));

    return this.rgbToHex(col.r,col.g,col.b);
  }

  private componentToHex(c: any) {
    let hex = c.toString(16);
    return hex.length == 1 ? "0" + hex : hex;
  }

  private rgbToHex(r, g, b) {
    return "#" + this.componentToHex(r) + this.componentToHex(g) + this.componentToHex(b);
  }

  private hexToRgb(hex: string) {
    let result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
    return result ? {
      r: parseInt(result[1], 16),
      g: parseInt(result[2], 16),
      b: parseInt(result[3], 16)
    } : null;
  }

  /**
   * Lettura cookie
   * @constructor
   */
  static ReadCookie(name: string): string {
    let _name = name + '=';
    let decodedCookie = decodeURIComponent(document.cookie);
    let ca = decodedCookie.split(';');
    for(let i = 0; i <ca.length; i++) {
      let c = ca[i];
      while (c.charAt(0) == ' ') {
        c = c.substring(1);
      }
      if (c.indexOf(_name) == 0) {
        return c.substring(_name.length, c.length);
      }
    }
    return null;
  }

  /**
   * Scrittura cookie
   * @constructor
   */
  static SaveCookie(name: string) {
    let d = new Date();
    let value = moment().format('YYYY-MM-DDTHH:mm:ss');
    d.setTime(d.getTime() + (365*24*60*60*1000));
    let expirationDate = "expires="+ d.toUTCString();
    document.cookie = name + '=' + value + ';' + expirationDate + ';path=/';
  }

  /**
   * Search fields list by service
   * @param {string} service
   * @returns {any[]}
   */
  fieldsForService(service: string): any[] {
    let _list = [];
    switch(service) {
      case UtilService.PENDENZE:
        _list = [
          new FormInput({ id: 'idDominio', label: FormService.FORM_ENTE_CREDITORE, type: UtilService.FILTERABLE,
            promise: { async: true, url: UtilService.RootByTOA() + UtilService.URL_DOMINI, mapFct: this.asyncElencoDominiPendenza.bind(this),
                   eventType: 'idDominio-async-load', preventSelection: true } }, this.http),
          new FormInput({ id: 'idA2A', label: FormService.FORM_A2A, noOptionLabel: 'Tutti', placeholder: FormService.FORM_PH_SELECT, type: UtilService.SELECT,
            promise: { async: true, url: UtilService.RootByTOA() + UtilService.URL_APPLICAZIONI, mapFct: this.asyncElencoApplicazioniPendenza.bind(this),
                   eventType: 'idA2A-async-load', preventSelection: true } }, this.http),
          new FormInput({ id: 'idDebitore', label: FormService.FORM_DEBITORE, placeholder: FormService.FORM_PH_DEBITORE,
                        type: UtilService.INPUT, pattern: FormService.VAL_CODICE_FISCALE }),
          new FormInput({ id: 'stato', label: FormService.FORM_STATO, noOptionLabel: 'Tutti', placeholder: FormService.FORM_PH_SELECT, type: UtilService.SELECT,
                      values: this.statiPendenza() }),
          new FormInput({ id: 'idPendenza', label: FormService.FORM_PENDENZA, placeholder: FormService.FORM_PH_PENDENZA, type: UtilService.INPUT }),
          new FormInput({ id: 'idPagamento', label: FormService.FORM_PAGAMENTO, placeholder: FormService.FORM_PH_PAGAMENTO, type: UtilService.INPUT }),
          // new FormInput({ id: 'tipo', label: FormService.FORM_TIPOLOGIA, noOptionLabel: 'Tutti', placeholder: FormService.FORM_PH_SELECT, type: UtilService.SELECT, values: UtilService.TIPOLOGIA_PENDENZA }),
          new FormInput({ id: 'idTipoPendenza', label: FormService.FORM_TIPO_PENDENZA, type: UtilService.FILTERABLE, values: UtilService._ID_TIPI_PENDENZA,
            optionControlValue: true, showTooltip: true }),
          // new FormInput({ id: 'stato2', label: FormService.FORM_STATO, noOptionLabel: 'Tutti', placeholder: FormService.FORM_PH_SELECT, type: UtilService.SELECT, values: this.statiPendenza(),
          //   dependency: 'stato', target: this.getKeyByValue(UtilService.STATI_PENDENZE, UtilService.STATI_PENDENZE.ESEGUITO), required: true })
        ];
        break;
      case UtilService.PAGAMENTI:
        _list = [
          new FormInput({ id: 'versante', label: FormService.FORM_VERSANTE, placeholder: FormService.FORM_PH_VERSANTE, type: UtilService.INPUT,
                     pattern: FormService.VAL_CODICE_FISCALE }),
          new FormInput({ id: 'stato', label: FormService.FORM_STATO, noOptionLabel: 'Tutti', placeholder: FormService.FORM_PH_SELECT, type: UtilService.SELECT, values: this.statiPagamento() }),
          new FormInput({ id: 'idSessionePortale', label: FormService.FORM_SESSIONE, placeholder: FormService.FORM_PH_SESSIONE, type: UtilService.INPUT }),
          new FormInput({ id: 'dataDa', label: FormService.FORM_DATA_INIZIO, type: UtilService.DATE_PICKER, }),
          new FormInput({ id: 'dataA', label: FormService.FORM_DATA_FINE, type: UtilService.DATE_PICKER, defaultTime: '23:59' }),
          new FormInput({ id: 'verificato', label: FormService.FORM_VERIFICATO, noOptionLabel: 'Tutti', type: UtilService.SELECT, values: this.statiVerifica() })
        ];
        break;
      case UtilService.REGISTRO_INTERMEDIARI:
      case UtilService.APPLICAZIONI:
      case UtilService.OPERATORI:
        _list = [
          new FormInput({ id: 'abilitato', label: FormService.FORM_PH_SELECT, noOptionLabel: 'Tutti', type: UtilService.SELECT, values: this.statiAbilitazione() })
        ];
        break;
      case UtilService.DOMINI:
        _list = [
          new FormInput({ id: 'idStazione', label: FormService.FORM_STAZIONE, placeholder: FormService.FORM_PH_STAZIONE, type: UtilService.INPUT }),
          new FormInput({ id: 'abilitato', label: FormService.FORM_PH_SELECT, noOptionLabel: 'Tutti', type: UtilService.SELECT, values: this.statiAbilitazione() })
        ];
        break;
      case UtilService.RPPS:
        _list = [
          new FormInput({ id: 'idDominio', label: FormService.FORM_DOMINIO, placeholder: FormService.FORM_PH_DOMINIO, type: UtilService.INPUT }),
          new FormInput({ id: 'iuv', label: FormService.FORM_IUV, placeholder: FormService.FORM_PH_IUV, type: UtilService.INPUT }),
          new FormInput({ id: 'ccp', label: FormService.FORM_CCP, placeholder: FormService.FORM_PH_CCP, type: UtilService.INPUT }),
          new FormInput({ id: 'idA2A', label: FormService.FORM_A2A, placeholder: FormService.FORM_PH_A2A, type: UtilService.INPUT }),
          new FormInput({ id: 'idPendenza', label: FormService.FORM_PENDENZA, placeholder: FormService.FORM_PH_PENDENZA, type: UtilService.INPUT }),
          new FormInput({ id: 'esito', label: FormService.FORM_ESITO, noOptionLabel: 'Tutti', placeholder: FormService.FORM_PH_SELECT, type: UtilService.SELECT, values: this.statiPagamento() }),
          new FormInput({ id: 'idPagamento', label: FormService.FORM_PAGAMENTO, placeholder: FormService.FORM_PH_PAGAMENTO, type: UtilService.INPUT })
        ];
      break;
      case UtilService.RUOLI:
      break;
      case UtilService.RENDICONTAZIONI:
        _list = [
          // new FormInput({ id: 'idDominio', label: FormService.FORM_DOMINIO, placeholder: FormService.FORM_PH_DOMINIO, type: UtilService.INPUT }),
          // new FormInput({ id: 'idDominio', label: FormService.FORM_DOMINIO, noOptionLabel: 'Tutti', placeholder: FormService.FORM_PH_SELECT, type: UtilService.SELECT,
          //   promise: { async: true, url: UtilService.RootByTOA() + UtilService.URL_DOMINI, mapFct: this.asyncElencoDominiPendenza.bind(this),
          //     eventType: 'idDominio-async-load', preventSelection: true } }, this.http),
          new FormInput({ id: 'idDominio', label: FormService.FORM_ENTE_CREDITORE, type: UtilService.FILTERABLE,
            promise: { async: true, url: UtilService.RootByTOA() + UtilService.URL_DOMINI, mapFct: this.asyncElencoDominiPendenza.bind(this),
              eventType: 'idDominio-async-load', preventSelection: true } }, this.http),
          new FormInput({ id: 'dataDa', label: FormService.FORM_DATA_RISC_INIZIO+' '+FormService.FORM_PH_DATA_RISC_INIZIO, type: UtilService.DATE_PICKER, }),
          new FormInput({ id: 'dataA', label: FormService.FORM_DATA_RISC_FINE+' '+FormService.FORM_PH_DATA_RISC_FINE, type: UtilService.DATE_PICKER, defaultTime: '23:59' })
        ];
      break;
      case UtilService.GIORNALE_EVENTI:
        _list = [
          // new FormInput({ id: 'idDominio', label: FormService.FORM_DOMINIO, placeholder: FormService.FORM_PH_DOMINIO, type: UtilService.INPUT }),
          // new FormInput({ id: 'idDominio', label: FormService.FORM_DOMINIO, noOptionLabel: 'Tutti', placeholder: FormService.FORM_PH_SELECT, type: UtilService.SELECT,
          //   promise: { async: true, url: UtilService.RootByTOA() + UtilService.URL_DOMINI, mapFct: this.asyncElencoDominiPendenza.bind(this),
          //     eventType: 'idDominio-async-load', preventSelection: true } }, this.http),
          new FormInput({ id: 'tipoEvento', label: FormService.FORM_TIPI_EVENTO, type: UtilService.FILTERABLE, values: UtilService._MAP_TIPI_EVENTO,
            optionControlValue: true, showTooltip: false }),
          new FormInput({ id: 'idDominio', label: FormService.FORM_ENTE_CREDITORE, type: UtilService.FILTERABLE,
            promise: { async: true, url: UtilService.RootByTOA() + UtilService.URL_DOMINI, mapFct: this.asyncElencoDominiPendenza.bind(this),
              eventType: 'idDominio-async-load', preventSelection: true } }, this.http),
          new FormInput({ id: 'iuv', label: FormService.FORM_IUV, placeholder: FormService.FORM_PH_IUV, type: UtilService.INPUT }),
          // new FormInput({ id: 'idA2A', label: FormService.FORM_A2A, placeholder: FormService.FORM_PH_A2A, type: UtilService.INPUT }),
          new FormInput({ id: 'idA2A', label: FormService.FORM_A2A, noOptionLabel: 'Tutti', placeholder: FormService.FORM_PH_SELECT, type: UtilService.SELECT,
            promise: { async: true, url: UtilService.RootByTOA() + UtilService.URL_APPLICAZIONI, mapFct: this.asyncElencoApplicazioniPendenza.bind(this),
              eventType: 'idA2A-async-load', preventSelection: true } }, this.http),
          new FormInput({ id: 'idPendenza', label: FormService.FORM_PENDENZA, placeholder: FormService.FORM_PH_PENDENZA, type: UtilService.INPUT }),
          new FormInput({ id: 'ruolo', label: FormService.FORM_RUOLO, noOptionLabel: 'Tutti', placeholder: FormService.FORM_PH_SELECT, type: UtilService.SELECT,
            showTooltip: false, values: this.ruoliGdE() }),
          new FormInput({ id: 'esito', label: FormService.FORM_ESITO_GDE, noOptionLabel: 'Tutti', placeholder: FormService.FORM_PH_SELECT, type: UtilService.SELECT,
            showTooltip: false, values: this.esitiGdE() }),
          new FormInput({ id: 'componente', label: FormService.FORM_COMPONENTE, noOptionLabel: 'Tutti', placeholder: FormService.FORM_PH_SELECT, type: UtilService.SELECT,
            promise: { async: true, url: UtilService.RootByTOA() + UtilService.URL_COMPONENTI_EVENTO, mapFct: this.asyncComponentiGdE.bind(this),
              eventType: 'componente-async-load', preventSelection: true }, showTooltip: false }, this.http),
        ];
      break;
      case UtilService.RISCOSSIONI:
        _list = [
          // new FormInput({ id: 'idDominio', label: FormService.FORM_DOMINIO, placeholder: FormService.FORM_PH_DOMINIO, type: UtilService.INPUT }),
          // new FormInput({ id: 'idDominio', label: FormService.FORM_DOMINIO, noOptionLabel: 'Tutti', placeholder: FormService.FORM_PH_SELECT, type: UtilService.SELECT,
          //   promise: { async: true, url: UtilService.RootByTOA() + UtilService.URL_DOMINI, mapFct: this.asyncElencoDominiPendenza.bind(this),
          //     eventType: 'idDominio-async-load', preventSelection: true } }, this.http),
          new FormInput({ id: 'idDominio', label: FormService.FORM_ENTE_CREDITORE, type: UtilService.FILTERABLE,
            promise: { async: true, url: UtilService.RootByTOA() + UtilService.URL_DOMINI, mapFct: this.asyncElencoDominiPendenza.bind(this),
              eventType: 'idDominio-async-load', preventSelection: true } }, this.http),
          // new FormInput({ id: 'idA2A', label: FormService.FORM_A2A, placeholder: FormService.FORM_PH_A2A, type: UtilService.INPUT }),
          new FormInput({ id: 'idA2A', label: FormService.FORM_A2A, noOptionLabel: 'Tutti', placeholder: FormService.FORM_PH_SELECT, type: UtilService.SELECT,
            promise: { async: true, url: UtilService.RootByTOA() + UtilService.URL_APPLICAZIONI, mapFct: this.asyncElencoApplicazioniPendenza.bind(this),
              eventType: 'idA2A-async-load', preventSelection: true } }, this.http),
          new FormInput({ id: 'idPendenza', label: FormService.FORM_PENDENZA, placeholder: FormService.FORM_PH_PENDENZA, type: UtilService.INPUT }),
          new FormInput({ id: 'stato', label: FormService.FORM_STATO, noOptionLabel: 'Tutti', placeholder: FormService.FORM_PH_SELECT, type: UtilService.SELECT, values: this.statiRiscossione() }),
          new FormInput({ id: 'dataDa', label: FormService.FORM_DATA_RISC_INIZIO+' '+FormService.FORM_PH_DATA_RISC_INIZIO, type: UtilService.DATE_PICKER, }),
          new FormInput({ id: 'dataA', label: FormService.FORM_DATA_RISC_FINE+' '+FormService.FORM_PH_DATA_RISC_FINE, type: UtilService.DATE_PICKER, defaultTime: '23:59' }),
          new FormInput({ id: 'tipo', label: FormService.FORM_TIPO_RISCOSSIONE, noOptionLabel: 'Tutti', placeholder: FormService.FORM_PH_SELECT, type: UtilService.SELECT, values: this.elencoTipiRiscossione() })
        ];
      break;
      case UtilService.TRACCIATI:
        _list = [
          new FormInput({ id: 'statoTracciatoPendenza', label: FormService.FORM_STATO, noOptionLabel: 'Tutti', placeholder: FormService.FORM_PH_SELECT, type: UtilService.SELECT, values: this.statiTracciatoPendenza() })
        ];
      break;
      case UtilService.INCASSI:
        _list = [
          new FormInput({ id: 'idDominio', label: FormService.FORM_ENTE_CREDITORE, type: UtilService.FILTERABLE,
            promise: { async: true, url: UtilService.RootByTOA() + UtilService.URL_DOMINI, mapFct: this.asyncElencoDominiPendenza.bind(this),
              eventType: 'idDominio-async-load', preventSelection: true } }, this.http)
        ];
      break;
      case UtilService.TIPI_PENDENZE:
        _list = [
          new FormInput({ id: 'tipo', label: FormService.FORM_TIPOLOGIA, noOptionLabel: 'Tutti', placeholder: FormService.FORM_PH_SELECT, type: UtilService.SELECT, values: UtilService.TIPOLOGIA_PENDENZA }),
          new FormInput({ id: 'idTipoPendenza', label: FormService.FORM_ID_TIPO_PENDENZA, type: UtilService.INPUT }),
          new FormInput({ id: 'descrizione', label: FormService.FORM_DESCRIZIONE, type: UtilService.INPUT })
        ];
      break;
    }

    return _list;
  }

  mapACL(acl: string): string {
    return UtilService.MAP_ACL(acl);
  }

 public static MAP_ACL(acl: string): string {
    let map = acl;
    switch(acl) {
      case 'Anagrafica Creditore':
        map = 'Anagrafica Enti';
        break;
      case 'Anagrafica Ruoli':
        map = 'Anagrafica Operatori';
        break;
      case 'Pagamenti':
        map = 'Backoffice Pagamenti';
        break;
      case 'Pendenze':
        map = 'Backoffice Pendenze';
        break;
      case 'Rendicontazioni e Incassi':
        map = 'Backoffice Ragioneria';
        break;
      case 'Configurazione e manutenzione':
        map = 'Gestione Batch';
        break;
    }
    return map;
  }

  /**
   *
   * Ordinamento voci menu di navigazione
   *
   * @param item1
   * @param item2
   * @returns {boolean}
   */
  public static SortMenu(item1, item2): number {
    if(item1.sort > item2.sort) {
      return 1;
    }
    if(item1.sort < item2.sort) {
      return -1;
    }

    return 0;
  }

  /**
   * Caricamenti asincroni
   */
  asyncElencoDominiPendenza(response: any): any[] {
    let _elenco = [];
    if(UtilService.PROFILO_UTENTE && UtilService.PROFILO_UTENTE.domini) {
      if (UtilService.PROFILO_UTENTE.domini.length != 0) {
        response.risultati = UtilService.PROFILO_UTENTE.domini;
      }
      if(response && response.risultati) {
        _elenco = response.risultati.map((item) => {
          return { label: item.ragioneSociale, value: item.idDominio };
        });
      }
    }
    return _elenco.sort((item1, item2) => {
      return (item1.label>item2.label)?1:(item1.label<item2.label)?-1:0;
    });
  }

  asyncElencoApplicazioniPendenza(response: any): any[] {
    let _elenco = [];
    if(response && response.risultati) {
      _elenco = response.risultati.map((item) => {
        return { label: item.idA2A, value: item.idA2A };
      });
    }
    return _elenco;
  }

  asyncComponentiGdE(response: any): any[] {
    let _elenco = [];
    if(response) {
      _elenco = response.map((item) => {
        return { label: item, value: item };
      });
    }
    return _elenco;
  }

  statiPendenza(): any[] {
    return Object.keys(UtilService.STATI_PENDENZE).map((key) => {
      return { label: UtilService.STATI_PENDENZE[key], value: key };
    });
  }

  statiPagamento(): any[] {
    return Object.keys(UtilService.STATI_PAGAMENTO).map((key) => {
      return { label: UtilService.STATI_PAGAMENTO[key], value: key };
    });
  }

  statiRiscossione(): any[] {
    return Object.keys(UtilService.STATI_RISCOSSIONE).map((key) => {
      return { label: UtilService.STATI_RISCOSSIONE[key], value: key };
    });
  }

  statiAbilitazione(): any[] {
    return Object.keys(UtilService.ABILITAZIONI).map((key) => {
      return { label: UtilService.ABILITAZIONI[key], value: key == 'true' };
    });
  }

  statiTracciatoPendenza(): any[] {
    return Object.keys(UtilService.STATI_TRACCIATO).map((key) => {
      return { label: UtilService.STATI_TRACCIATO[key], value: key };
    });
  }

  statiVerifica(): any[] {
    return Object.keys(UtilService.VERIFICHE).map((key) => {
      return { label: UtilService.VERIFICHE[key], value: key == 'true' };
    });
  }

  tipiAutenticazione(): any[] {
    return Object.keys(UtilService.TIPI_AUTENTICAZIONE).map((key) => {
      return { label: UtilService.TIPI_AUTENTICAZIONE[key], value: key };
    });
  }

  tipiContabilita(): any[] {
    return Object.keys(UtilService.TIPI_CONTABILITA).map((key) => {
      return { label: UtilService.TIPI_CONTABILITA[key], value: key };
    });
  }

  dirittiCodeUtente(): any[] {
    return UtilService.DIRITTI_CODE;
  }

  elencoTipiRiscossione(): any[] {
    return Object.keys(UtilService.TIPI_RISCOSSIONE).map((key) => {
      return { label: UtilService.TIPI_RISCOSSIONE[key], value: key };
    });
  }

  ruoliGdE(): any[] {
    return Object.keys(UtilService.RUOLI_GDE).map((key) => {
      return { label: UtilService.RUOLI_GDE[key], value: key };
    });
  }

  esitiGdE(): any[] {
    return Object.keys(UtilService.ESITI_GDE).map((key) => {
      return { label: UtilService.ESITI_GDE[key], value: key };
    });
  }

  resetDashboardLinksParams() {
    UtilService.DASHBOARD_LINKS_PARAMS = { method: null, params: [] };
  }

}
