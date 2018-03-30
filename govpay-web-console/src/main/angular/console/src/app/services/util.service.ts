import { ComponentRef, Injectable } from '@angular/core';
import { MatDialog, MatSnackBar } from '@angular/material';
import { FormInput } from '../classes/view/form-input';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { ModalBehavior } from '../classes/modal-behavior';
import { FormService } from './form.service';

@Injectable()
export class UtilService {

  public static ROOT_SERVICE: string = UtilService.HTTP_ROOT_SERVICE();

  public static AUTHORIZATION: string = 'Z3BhZG1pbjpwYXNzd29yZA==';
  public static METHODS: any = {
    POST: 'post',
    PUT: 'put',
    PATCH: 'patch',
    DELETE: 'delete',
  };

  public static ICONS: any = {
    DOWNLOAD: 'file_download',
    EDIT: 'edit',
    DELETE: 'delete',
    ADD: 'add',
  };

  //DIRITTI
  public static DIRITTI: any = {
    LETTURA: 'Lettura',
    SCRITTURA: 'Scrittura',
    ESECUZIONE: 'Esecuzione'
  };

  //LISTA SERVIZI
  public static SERVIZI: any = {
    ANAGRAFICA_PAGOPA: 'Anagrafica PagoPA',
    ANAGRAFICA_CREDITORE: 'Anagrafica Creditore',
    ANAGRAFICA_APPLICAZIONI: 'Anagrafica Applicazioni',
    ANAGRAFICA_RUOLI: 'Anagrafica Ruoli',
    PENDENZE_E_PAGAMENTI: 'Pendenze e Pagamenti',
    PENDENZE_E_PAGAMENTI_PROPRI: 'Pendenze e Pagamenti propri',
    RENDICONTAZIONI_E_INCASSI: 'Rendicontazioni e Incassi',
    GIORNALE_DEGLI_EVENTI: 'Giornale degli Eventi',
    STATISTICHE: 'Statistiche',
    CONFIGURAZIONE_E_MANUTENZIONE: 'Configurazione e Manutenzione'
  };

  //STATI PAGAMENTO
  public static STATI_PAGAMENTO: any = {
    IN_CORSO: 'Pagamento in corso',
    RIFIUTATO: 'Pagamento rifiutato',
    ESEGUITO: 'Pagamento eseguito',
    NON_ESEGUITO: 'Pagamento non eseguito',
    ESEGUITO_PARZIALE: 'Pagamento parzialmente eseguito',
    DECORRENZA: 'Decorrenza termini',
    DECORRENZA_PARZIALE: 'Decorrenza termini parziale'
  };

  //STATI PENDENZE
  public static STATI_PENDENZE: any = {
    ESEGUITO: 'Pagata',
    NON_ESEGUITO: 'Da pagare',
    ESEGUITO_PARZIALE: 'Pagata parzialmente',
    ANNULLATO: 'Annullata',
    SCADUTO: 'Scaduta'
  };

  //STATI ESITO PAGAMENTO
  public static STATI_ESITO_PAGAMENTO: any = {
    IN_CORSO: 'Pagamento in corso',
    RIFIUTATO: 'Pagamento rifiutato',
    ESEGUITO: 'Pagamento eseguito',
    NON_ESEGUITO: 'Pagamento non eseguito',
    ESEGUITO_PARZIALE: 'Pagamento parzialmente eseguito',
    DECORRENZA: 'Decorrenza termini',
    DECORRENZA_PARZIALE: 'Decorrenza termini parziale'
  };

  //TIPOLOGIE VERSAMENTO
  public static TIPI_VERSAMENTO: any = {
    PO: 'Pagamento da PSP',
    BP: 'Bollettino postale',
    BBT: 'Bonifico bancario',
    CP: 'Carta di pagamento',
    AD: 'Addebito diretto',
    OBEP: 'Pagamento Mybank',
    OTH: 'Altro tipo di pagamento'
  };

  //TIPOLOGIE CATEGORIA EVENTO
  public static TIPI_CATEGORIA_EVENTO: any = {
    INTERNO: 'Interno',
    INTERFACCIA: 'Interfaccia'
  };

  //TIPOLOGIE MODELLI DI PAGAMENTO
  public static TIPI_MODELLI_PAGAMENTO: any = {
    '0': 'Immediato mono-beneficiario',
    '1': 'Immediato pluri-beneficiario',
    '2': 'Pagamento differito',
    '4': 'Pagamento da PSP'
  };

  //TIPI VERSIONE
  public static TIPI_VERSIONE_API: any = {
    'REST_1.0': 'REST 1.0',
    'SOAP_2.0': 'SOAP 2.0',
    'SOAP_2.1': 'SOAP 2.1',
    'SOAP_2.3': 'SOAP 2.3',
    'SOAP_2.5': 'SOAP 2.5'
  };

  //TIPI SOGGETTO
  public static TIPI_SOGGETTO: any = {
    'F': 'Persona fisica',
    'G': 'Persona giuridica'
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

  //ROOT URL SERVIZI
  public static URL_PROFILO: string = '/profilo';
  public static URL_PENDENZE: string = '/pendenze';
  public static URL_PAGAMENTI: string = '/pagamenti';
  public static URL_REGISTRO_PSP: string = '/psp';
  public static URL_REGISTRO_INTERMEDIARI: string = '/intermediari';
  public static URL_RPPS: string = '/rpp';
  public static URL_APPLICAZIONI: string = '/applicazioni';
  public static URL_STAZIONI: string = '/stazioni';
  public static URL_ENTRATE: string = '/entrate';
  public static URL_DOMINI: string = '/domini';
  public static URL_OPERATORI: string = '/operatori';
  public static URL_GIORNALE_EVENTI: string = '/eventi';
  public static URL_RISCOSSIONI: string = '/riscossioni';
  public static URL_RENDICONTAZIONI: string = '/flussiRendicontazione';
  public static URL_INCASSI: string = '/incassi';
  public static URL_UNITA_OPERATIVE: string = '/unitaOperative';
  public static URL_IBAN_ACCREDITI: string = '/ibanAccredito';
  public static URL_ACL: string = '/acl';

  //LABEL
  public static TXT_PENDENZE: string = 'Pendenze';
  public static TXT_PAGAMENTI: string = 'Pagamenti';
  public static TXT_PROFILO: string = 'Profilo utente';
  public static TXT_REGISTRO_PSP: string = 'Registro PSP';
  public static TXT_REGISTRO_INTERMEDIARI: string = 'Registro intermediari';
  public static TXT_RPPS: string = 'Richieste di pagamento';
  public static TXT_APPLICAZIONI: string = 'Applicazioni';
  public static TXT_DOMINI: string = 'Domini';
  public static TXT_ENTRATE: string = 'Entrate';
  public static TXT_ACL: string = 'ACL';
  public static TXT_OPERATORI: string = 'Operatori';
  public static TXT_GIORNALE_EVENTI: string = 'Giornale degli eventi';
  public static TXT_RISCOSSIONI: string = 'Riscossioni';
  public static TXT_RENDICONTAZIONI: string = 'Flussi rendicontazione';
  public static TXT_INCASSI: string = 'Incassi';

  //Types
  //Component view ref
  public static PENDENZE: string = 'pendenze';
  public static PAGAMENTI: string = 'pagamenti';
  public static REGISTRO_PSP: string = 'registro_psp';
  public static REGISTRO_INTERMEDIARI: string = 'registro_intermediari';
  public static RPPS: string = 'richieste_pagamenti';
  public static APPLICAZIONI: string = 'applicazioni';
  public static DOMINI: string = 'domini';
  public static ENTRATE: string = 'entrate';
  public static ACLS: string = 'acls';
  public static OPERATORI: string = 'operatori';
  public static GIORNALE_EVENTI: string = 'giornale_eventi';
  public static RISCOSSIONI: string = 'riscossioni';
  public static RENDICONTAZIONI: string = 'rendicontazioni';
  public static INCASSI: string = 'incassi';
  public static UNITA_OPERATIVE: string = 'unitaOperative';
  public static IBAN_ACCREDITI: string = 'ibanAccredito';
  //Item view ref
  public static STANDARD: string = '';
  public static RIEPILOGO: string = 'riepilogo';
  public static CRONO: string = 'crono';
  public static KEY_VALUE: string = 'key_value';
  //Dialog view ref
  public static INTERMEDIARIO: string = 'intermediario';
  public static STAZIONE: string = 'stazione';
  public static APPLICAZIONE: string = 'applicazione';
  public static ACL: string = 'acl';
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
  public static SELECT: string = 'select';
  public static SLIDE_TOGGLE: string = 'slide_toggle';
  public static LABEL: string = 'label';

  //Behaviors
  public static dialogBehavior: BehaviorSubject<ModalBehavior> = new BehaviorSubject(null);
  public static blueDialogBehavior: BehaviorSubject<ModalBehavior> = new BehaviorSubject(null);
  public static headBehavior: BehaviorSubject<any> = new BehaviorSubject(null);

  //Active detail state
  public static ActiveDetailState: ComponentRef<any>;

  //Header: actions list
  public static HEADER_ACTIONS: any[] = [{ label: 'Esporta', type: UtilService.NO_TYPE } ];

  constructor(private message: MatSnackBar, private dialog: MatDialog) { }


  public static HTTP_ROOT_SERVICE(): string {
    return window['rootService']();
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

  /**
   *
   * Alert messages
   * @param {string} _message
   * @param {boolean} _keep
   */
  alert(_message: string, _keep: boolean = false) {
    let _config = { duration: 3000 };
    let _action = null;
    if (_keep){
      _config = null;
      _action = 'Chiudi';
    }
    if(_message) {
      this.message.open(_message, _action, _config);
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
      return '€ '+ (value.toFixed(2));
    }
    return '€ 0.00';
  }

  importoClass(_stato: string): any {
    let _status = { e: false, w: false, n: false, got: false };
    switch(_stato) {
      case UtilService.STATI_PENDENZE.SCADUTO:
        _status.e = true;
        _status.got = true;
        break;
      case UtilService.STATI_PENDENZE.NON_ESEGUITO:
        _status.w = true;
        _status.got = true;
        break;
    }
    if(!_status.got) {
      switch(_stato) {
        case UtilService.STATI_PAGAMENTO.DECORRENZA:
          _status.e = true;
          break;
        case UtilService.STATI_PAGAMENTO.NON_ESEGUITO:
          _status.w = true;
          break;
        default:
          _status.n = true;
          break;
      }
    }
    return {
      'font-weight-500': true,
      'text-truncate': true,
      'stato-error': _status.e,
      'stato-warning': _status.w,
      'stato-normal': _status.n,
    };
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
          // new FormInput({ id: 'stato2', label: FormService.FORM_STATO, placeholder: FormService.FORM_PH_SELECT, type: UtilService.SELECT, values: this.statiPendenza(),
          //   dependency: 'stato', target: this.getKeyByValue(UtilService.STATI_PENDENZE, UtilService.STATI_PENDENZE.ESEGUITO), required: true })
        ];
        break;
      case UtilService.PAGAMENTI:
        _list = [
          new FormInput({ id: 'versante', label: FormService.FORM_VERSANTE, placeholder: FormService.FORM_PH_VERSANTE, type: UtilService.INPUT, pattern: FormService.VAL_CODICE_FISCALE }),
          new FormInput({ id: 'stato', label: FormService.FORM_STATO, placeholder: FormService.FORM_PH_SELECT, type: UtilService.SELECT, values: this.statiPagamento() }),
          new FormInput({ id: 'idSessionePortale', label: FormService.FORM_SESSIONE, placeholder: FormService.FORM_PH_SESSIONE, type: UtilService.INPUT })
        ];
        break;
      case UtilService.REGISTRO_INTERMEDIARI:
      case UtilService.APPLICAZIONI:
      case UtilService.OPERATORI:
        _list = [
          new FormInput({ id: 'abilitato', label: FormService.FORM_ABILITATO, type: UtilService.SLIDE_TOGGLE })
        ];
        break;
      case UtilService.REGISTRO_PSP:
        _list = [
          new FormInput({ id: 'bollo', label: FormService.FORM_BOLLO, type: UtilService.SLIDE_TOGGLE }),
          new FormInput({ id: 'storno', label: FormService.FORM_STORNO, type: UtilService.SLIDE_TOGGLE }),
          new FormInput({ id: 'abilitato', label: FormService.FORM_ABILITATO, type: UtilService.SLIDE_TOGGLE })
        ];
        break;
      case UtilService.DOMINI:
        _list = [
          new FormInput({ id: 'idStazione', label: FormService.FORM_STAZIONE, placeholder: FormService.FORM_PH_STAZIONE, type: UtilService.INPUT }),
          new FormInput({ id: 'abilitato', label: FormService.FORM_ABILITATO, type: UtilService.SLIDE_TOGGLE })
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
      case UtilService.ACLS:
        _list = [
          new FormInput({ id: 'ruolo', label: FormService.FORM_RUOLO, placeholder: FormService.FORM_PH_RUOLO, type: UtilService.INPUT }),
          new FormInput({ id: 'principal', label: FormService.FORM_PRINCIPAL, placeholder: FormService.FORM_PH_PRINCIPAL, type: UtilService.INPUT }),
          new FormInput({ id: 'servizio', label: FormService.FORM_SERVIZIO, placeholder: FormService.FORM_PH_SELECT, type: UtilService.SELECT, values: this.elencoServizi() })
        ];
      break;
      case UtilService.GIORNALE_EVENTI:
        _list = [
          new FormInput({ id: 'idDominio', label: FormService.FORM_DOMINIO, placeholder: FormService.FORM_PH_DOMINIO, type: UtilService.INPUT }),
          new FormInput({ id: 'iuv', label: FormService.FORM_IUV, placeholder: FormService.FORM_PH_IUV, type: UtilService.INPUT })
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

  tipiAutenticazione(): any[] {
    return Object.keys(UtilService.TIPI_AUTENTICAZIONE).map((key) => {
      return { label: UtilService.TIPI_AUTENTICAZIONE[key], value: key };
    });
  }

  tipiVersione(): any[] {
    return Object.keys(UtilService.TIPI_VERSIONE_API).map((key) => {
      return { label: UtilService.TIPI_VERSIONE_API[key], value: key };
    });
  }

  tipiContabilita(): any[] {
    return Object.keys(UtilService.TIPI_CONTABILITA).map((key) => {
      return { label: UtilService.TIPI_CONTABILITA[key], value: key };
    });
  }

  dirittiUtente(): any[] {
    return Object.keys(UtilService.DIRITTI).map((key) => {
      return { label: UtilService.DIRITTI[key], value: key };
    });
  }

  elencoServizi(): any[] {
    return Object.keys(UtilService.SERVIZI).map((key) => {
      return { label: UtilService.SERVIZI[key], value: key };
    });
  }

}
