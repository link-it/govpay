import { ComponentRef, Injectable } from '@angular/core';
import { MatDialog, MatSnackBar } from '@angular/material';
import { FormInput } from '../classes/view/form-input';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { ModalBehavior } from '../classes/modal-behavior';
import { FormService } from './form.service';
import { Subscription } from 'rxjs/Subscription';


@Injectable()
export class UtilService {

  public static NEED_BASIC_AUTHORIZATION: boolean = true;

  public static ROOT_SERVICE: string = UtilService.HTTP_ROOT_SERVICE();
  public static LOGOUT_SERVICE: string = UtilService.HTTP_LOGOUT_SERVICE();
  public static BASE_HREF: string = UtilService.HTTP_BASE_HREF();

  public static TIMEOUT: number = 20000; //20 seconds
  public static PROFILO_UTENTE: any;
  public static AUTHORIZATION: string = 'Z3BhZG1pbjpwYXNzd29yZA==';
  public static METHODS: any = {
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
    hasRendiIncassi: false,
    hasGdE: false,
    hasConfig: false
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
    { label: UtilService._LABEL.SCRITTURA, code: UtilService._CODE.SCRITTURA },
    { label: UtilService._LABEL.ESECUZIONE, code: UtilService._CODE.ESECUZIONE }
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
    SCADUTA: 'Scaduta'
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
    INCASSATA: 'Incassata'
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
    9: 'Pagamento eseguito in assenza di RPT'
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

  //TIPOLOGIE CATEGORIA EVENTO
  public static TIPI_CATEGORIA_EVENTO: any = {
    INTERNO: 'Interno',
    INTERFACCIA: 'Interfaccia'
  };

  //TIPI VERSIONE
  public static TIPI_VERSIONE_API: string[] = [];

  //LISTA SERVIZI
  public static SERVIZI: string[] = [];
  public static CONFIGURAZIONE_E_MANUTENZIONE: string = 'Configurazione e manutenzione';

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
    ENTRATA: 'Entrata',
    SPECIALE: 'Speciale',
    SIOPE: 'Siope',
    ALTRO: 'Altro'
  };

  //VERIFICA
  public static VERIFICHE: any = {
    'true': 'Verificato',
    'false': 'Non verificato'
  };

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
  public static URL_OPERAZIONI: string = '/operazioni';
  public static URL_ACQUISIZIONE_RENDICONTAZIONI: string = '/acquisizioneRendicontazioni';
  public static URL_RECUPERO_RPT_PENDENTI: string = '/recuperoRptPendenti';

  //ROOT URL SHARED SERVICES
  public static URL_SERVIZIACL: string = '/enumerazioni/serviziACL';
  public static URL_TIPI_VERSIONE_API: string = '/enumerazioni/versioneConnettore';

  //LABEL
  public static TXT_DASHBOARD: string = 'Cruscotto';
  public static TXT_PENDENZE: string = 'Pendenze';
  public static TXT_PAGAMENTI: string = 'Pagamenti';
  public static TXT_PROFILO: string = 'Profilo utente';
  public static TXT_REGISTRO_INTERMEDIARI: string = 'Intermediari';
  public static TXT_RPPS: string = 'Richieste di pagamento';
  public static TXT_APPLICAZIONI: string = 'Applicazioni';
  public static TXT_DOMINI: string = 'Domini';
  public static TXT_ENTRATE: string = 'Entrate';
  public static TXT_RUOLI: string = 'Ruoli';
  public static TXT_OPERATORI: string = 'Operatori';
  public static TXT_GIORNALE_EVENTI: string = 'Giornale degli eventi';
  public static TXT_RISCOSSIONI: string = 'Riscossioni';
  public static TXT_RENDICONTAZIONI: string = 'Rendicontazioni';
  public static TXT_INCASSI: string = 'Riconciliazioni';

  public static TXT_MAN_NOTIFICHE: string = 'Spedisci notifiche';
  public static TXT_MAN_RENDICONTAZIONI: string = 'Acquisisci rendicontazioni';
  public static TXT_MAN_PAGAMENTI: string = 'Recupera pagamenti';
  public static TXT_MAN_CACHE: string = 'Resetta la cache';


  //Types
  //Component view ref
  public static DASHBOARD: string = 'dashboard';
  public static PENDENZE: string = 'pendenze';
  public static PAGAMENTI: string = 'pagamenti';
  public static REGISTRO_INTERMEDIARI: string = 'registro_intermediari';
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
  //Item view ref
  public static STANDARD: string = '';
  public static STANDARD_COLLAPSE: string = 'standard_collapse';
  public static RIEPILOGO: string = 'riepilogo';
  public static CRONO: string = 'crono';
  public static CRONO_CODE: string = 'crono_code';
  public static KEY_VALUE: string = 'key_value';
  //Dialog view ref
  public static INTERMEDIARIO: string = 'intermediario';
  public static STAZIONE: string = 'stazione';
  public static APPLICAZIONE: string = 'applicazione';
  public static ACL: string = 'autorizzazione';
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
  public static ENTRATA_DOMINIO: string = 'entrata_dominio';
  public static UNITA_OPERATIVA: string = 'unita_operativa';
  public static IBAN_ACCREDITO: string = 'iban_accredito';
  public static PENDENZA: string = 'pendenza';
  public static NO_TYPE: string = '-';
  //Material standard ref
  public static INPUT: string = 'input';
  public static DATE_PICKER: string = 'date_picker';
  public static SELECT: string = 'select';
  public static SLIDE_TOGGLE: string = 'slide_toggle';
  public static LABEL: string = 'label';

  //Behaviors
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

  /**
   * Dashboard link params
   * @type { {method: string; params: Array<{ controller: '', key: '', value: '' }>} }
   */
  public static DASHBOARD_LINKS_PARAMS: any = { method: null, params: [] };


  constructor(private message: MatSnackBar, private dialog: MatDialog) { }


  public static HTTP_ROOT_SERVICE(): string {
    return window['rootService']();
  }

  public static HTTP_LOGOUT_SERVICE(): string {
    return window['httpLogoutService']();
  }

  public static HTTP_BASE_HREF(): string {
    return window['httpBase']();
  }

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

  onError(error: any) {
    let _msg = '';
    try {
      _msg = (!error.instance.error.dettaglio)?error.instance.error.descrizione:error.instance.error.descrizione+': '+error.instance.error.dettaglio;
      if(_msg.length > 200) {
        _msg = _msg.substring(0, 200);
      }
    } catch(e) {
      _msg = error.message;
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
    let _config = { duration: 10000, panelClass: 'overflow-hidden' };
    let _actions = null;
    if (_keep){
      _config = null;
    }
    if (_action){
      _actions = 'Chiudi';
    }
    if(_message) {
      this.message.open(_message, _actions, _config);
    }
  }

  openDialog(component: any, _mb: ModalBehavior): void {
    let dialogRef = this.dialog.open(component, {
      width: '50%',
      //height: '50%',
      data: _mb
    });

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
   * Var is valid
   * @param value
   * @returns {boolean}
   */
  hasValue(value: any): boolean {
    return (value != null && value !== undefined && value !== '');
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
   * Search fields list by service
   * @param {string} service
   * @returns {any[]}
   */
  fieldsForService(service: string): any[] {
    let _list = [];
    switch(service) {
      case UtilService.PENDENZE:
        _list = [
          new FormInput({ id: 'idDominio', label: FormService.FORM_DOMINIO, placeholder: FormService.FORM_PH_DOMINIO, type: UtilService.INPUT }),
          new FormInput({ id: 'idA2A', label: FormService.FORM_A2A, placeholder: FormService.FORM_PH_A2A, type: UtilService.INPUT }),
          new FormInput({ id: 'idDebitore', label: FormService.FORM_DEBITORE, placeholder: FormService.FORM_PH_DEBITORE, type: UtilService.INPUT, pattern: FormService.VAL_CODICE_FISCALE }),
          new FormInput({ id: 'stato', label: FormService.FORM_STATO, placeholder: FormService.FORM_PH_SELECT, type: UtilService.SELECT, values: this.statiPendenza() }),
          new FormInput({ id: 'idPagamento', label: FormService.FORM_PAGAMENTO, placeholder: FormService.FORM_PH_PAGAMENTO, type: UtilService.INPUT })
          // new FormInput({ id: 'stato2', label: FormService.FORM_STATO, placeholder: FormService.FORM_PH_SELECT, type: UtilService.SELECT, values: this.statiPendenza(),
          //   dependency: 'stato', target: this.getKeyByValue(UtilService.STATI_PENDENZE, UtilService.STATI_PENDENZE.ESEGUITO), required: true })
        ];
        break;
      case UtilService.PAGAMENTI:
        _list = [
          new FormInput({ id: 'versante', label: FormService.FORM_VERSANTE, placeholder: FormService.FORM_PH_VERSANTE, type: UtilService.INPUT, pattern: FormService.VAL_CODICE_FISCALE }),
          new FormInput({ id: 'stato', label: FormService.FORM_STATO, placeholder: FormService.FORM_PH_SELECT, type: UtilService.SELECT, values: this.statiPagamento() }),
          new FormInput({ id: 'idSessionePortale', label: FormService.FORM_SESSIONE, placeholder: FormService.FORM_PH_SESSIONE, type: UtilService.INPUT }),
          new FormInput({ id: 'dataRichiestaPagamentoInizio', label: FormService.FORM_DATA_INIZIO, type: UtilService.DATE_PICKER, }),
          new FormInput({ id: 'dataRichiestaPagamentoFine', label: FormService.FORM_DATA_FINE, type: UtilService.DATE_PICKER }),
          new FormInput({ id: 'verificato', label: FormService.FORM_PH_SELECT, type: UtilService.SELECT, values: this.statiVerifica() })
        ];
        break;
      case UtilService.REGISTRO_INTERMEDIARI:
      case UtilService.APPLICAZIONI:
      case UtilService.OPERATORI:
        _list = [
          new FormInput({ id: 'abilitato', label: FormService.FORM_PH_SELECT, type: UtilService.SELECT, values: this.statiAbilitazione() })
        ];
        break;
      case UtilService.DOMINI:
        _list = [
          new FormInput({ id: 'idStazione', label: FormService.FORM_STAZIONE, placeholder: FormService.FORM_PH_STAZIONE, type: UtilService.INPUT }),
          new FormInput({ id: 'abilitato', label: FormService.FORM_PH_SELECT, type: UtilService.SELECT, values: this.statiAbilitazione() })
        ];
        break;
      case UtilService.RPPS:
        _list = [
          new FormInput({ id: 'idDominio', label: FormService.FORM_DOMINIO, placeholder: FormService.FORM_PH_DOMINIO, type: UtilService.INPUT }),
          new FormInput({ id: 'iuv', label: FormService.FORM_IUV, placeholder: FormService.FORM_PH_IUV, type: UtilService.INPUT }),
          new FormInput({ id: 'ccp', label: FormService.FORM_CCP, placeholder: FormService.FORM_PH_CCP, type: UtilService.INPUT }),
          new FormInput({ id: 'idA2A', label: FormService.FORM_A2A, placeholder: FormService.FORM_PH_A2A, type: UtilService.INPUT }),
          new FormInput({ id: 'idPendenza', label: FormService.FORM_PENDENZA, placeholder: FormService.FORM_PH_PENDENZA, type: UtilService.INPUT }),
          new FormInput({ id: 'esito', label: FormService.FORM_ESITO, placeholder: FormService.FORM_PH_SELECT, type: UtilService.SELECT, values: this.statiPagamento() }),
          new FormInput({ id: 'idPagamento', label: FormService.FORM_PAGAMENTO, placeholder: FormService.FORM_PH_PAGAMENTO, type: UtilService.INPUT })
        ];
      break;
      case UtilService.RUOLI:
      break;
      case UtilService.RENDICONTAZIONI:
        _list = [
          new FormInput({ id: 'idDominio', label: FormService.FORM_DOMINIO, placeholder: FormService.FORM_PH_DOMINIO, type: UtilService.INPUT }),
          new FormInput({ id: 'dataDa', label: FormService.FORM_DATA_RISC_INIZIO+' '+FormService.FORM_PH_DATA_RISC_INIZIO, type: UtilService.DATE_PICKER, }),
          new FormInput({ id: 'dataA', label: FormService.FORM_DATA_RISC_FINE+' '+FormService.FORM_PH_DATA_RISC_FINE, type: UtilService.DATE_PICKER })
        ];
      break;
      case UtilService.GIORNALE_EVENTI:
        _list = [
          new FormInput({ id: 'idDominio', label: FormService.FORM_DOMINIO, placeholder: FormService.FORM_PH_DOMINIO, type: UtilService.INPUT }),
          new FormInput({ id: 'iuv', label: FormService.FORM_IUV, placeholder: FormService.FORM_PH_IUV, type: UtilService.INPUT }),
          new FormInput({ id: 'idA2A', label: FormService.FORM_A2A, placeholder: FormService.FORM_PH_A2A, type: UtilService.INPUT }),
          new FormInput({ id: 'idPendenza', label: FormService.FORM_PENDENZA, placeholder: FormService.FORM_PH_PENDENZA, type: UtilService.INPUT })
        ];
      break;
      case UtilService.RISCOSSIONI:
        _list = [
          new FormInput({ id: 'idDominio', label: FormService.FORM_DOMINIO, placeholder: FormService.FORM_PH_DOMINIO, type: UtilService.INPUT }),
          new FormInput({ id: 'idA2A', label: FormService.FORM_A2A, placeholder: FormService.FORM_PH_A2A, type: UtilService.INPUT }),
          new FormInput({ id: 'idPendenza', label: FormService.FORM_PENDENZA, placeholder: FormService.FORM_PH_PENDENZA, type: UtilService.INPUT }),
          new FormInput({ id: 'stato', label: FormService.FORM_STATO, placeholder: FormService.FORM_PH_SELECT, type: UtilService.SELECT, values: this.statiRiscossione() }),
          new FormInput({ id: 'dataDa', label: FormService.FORM_DATA_RISC_INIZIO+' '+FormService.FORM_PH_DATA_RISC_INIZIO, type: UtilService.DATE_PICKER, }),
          new FormInput({ id: 'dataA', label: FormService.FORM_DATA_RISC_FINE+' '+FormService.FORM_PH_DATA_RISC_FINE, type: UtilService.DATE_PICKER }),
          new FormInput({ id: 'tipo', label: FormService.FORM_TIPO_RISCOSSIONE, placeholder: FormService.FORM_PH_SELECT, type: UtilService.SELECT, values: this.elencoTipiRiscossione() })
        ];
      break;
    }

    return _list;
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

  resetDashboardLinksParams() {
    UtilService.DASHBOARD_LINKS_PARAMS = { method: null, params: [] };
  }

}
