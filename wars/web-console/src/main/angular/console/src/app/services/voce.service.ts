import { Injectable } from '@angular/core';

@Injectable()
export class Voce {

  public static ABILITATO: string = 'Abilitato';
  public static ALLEGA_PDF_AVVISO: string = 'Allega PDF avviso';
  public static ALLEGA_PDF_RICEVUTA: string = 'Allega PDF ricevuta';
  public static API_PAGAMENTI: string = 'API Pagamenti';
  public static API_PENDENZE: string = 'API Pendenze';
  public static API_RAGIONERIA: string = 'API Ragioneria';
  public static APPLICAZIONE: string = 'Applicazione';
  public static AREA: string = 'Area';
  public static AUT_PT: string = 'Autorizzazione stampa PT';
  public static AUTORIZZAZIONI: string = 'Autorizzazioni';
  public static AUTORIZZAZIONI_API: string = 'Autorizzazioni API';
  public static AUTORIZZAZIONI_BACKOFFICE: string = 'Autorizzazioni Backoffice';
  public static AUX: string = 'Aux';
  public static AVVISO: string = 'N. avviso';

  public static BASIC: string = 'HTTP Basic';
  public static BIC_RIVERSAMENTO: string = 'Codice Bic riversamento';

  public static CAP: string = 'Cap';
  public static CATEGORIA_EVENTO: string = 'Categoria evento';
  public static CAUSALE: string = 'Causale';
  public static CBILL: string = 'CBill';
  public static CCP: string = 'CCP';
  public static CIVICO: string = 'Numero civico';
  public static CODICE: string = 'Codice';
  public static CODICE_CONTABILITA: string = 'Codice contabilità';
  public static COMMISSIONI: string = 'Commissioni';
  public static CONTABILITA: string = 'Contabilità';
  public static CONTENUTO_ALLEGATO: string = 'Contenuto allegato';
  public static CONTO_ACCREDITO: string = 'Conto accredito';

  public static DATA: string = 'Data';
  public static DATA_CARICAMENTO: string = 'Data caricamento';
  public static DATA_CONTABILE: string = 'Data contabile';
  public static DATA_ESITO: string = 'Data esito';
  public static DATA_ESECUZIONE_RISCOSSIONE: string = 'Data esecuzione riscossione';
  public static DATA_REGOLAMENTO: string = 'Data regolamento';
  public static DATA_RICEVUTA: string = 'Data ricevuta';
  public static DATA_RICHIESTA: string = 'Data richiesta';
  public static DATA_RICHIESTA_PAGAMENTO: string = 'Data richiesta';
  public static DATA_RISPOSTA: string = 'Data risposta';
  public static DATA_ULTIMO_AGGIORNAMENTO: string = 'Ultimo aggiornamento';
  public static DATA_VALUTA_INCASSO: string = 'Data valuta incasso';
  public static DATA_VALIDITA: string = 'Data validità';
  public static DATI_PAGO_PA: string = 'Dati pagoPA';
  public static DEBITORE: string = 'Debitore';
  public static DEFAULT: string = 'Predefinito';
  public static DEFINIZIONE: string = 'Definizione';
  public static DENOMINAZIONE: string = 'Denominazione';
  public static DESCRIZIONE: string = 'Descrizione';
  public static DETTAGLIO_ESITO: string = 'Dettaglio esito';
  public static DOMINIO: string = 'Dominio';
  public static DOMINI: string = 'Domini';
  public static DURATA: string = 'Durata';

  public static ELABORAZIONE: string = 'Elaborazione';
  public static EMAIL: string = 'Email';
  public static ENTE_CREDITORE: string = 'Ente creditore';
  public static ENTE_CREDITORE_SIGLA: string = 'EC';
  public static ENTI_CREDITORI: string = 'Enti creditori';
  public static EROGATORE: string = 'Erogatore';
  public static ESITO: string = 'Esito';

  public static FAX: string = 'Fax';
  public static FORM: string = 'Form';
  public static FRUITORE: string = 'Fruitore';

  public static GLN: string = 'Global location number';

  public static HOSTNAME: string = 'Hostname';

  public static IBAN_POSTALE: string = 'Iban postale';
  public static ID_A2A: string = 'Id A2A';
  public static ID_BOLLO: string = 'Id bollo';
  public static ID_CANALE: string = 'Id canale';
  public static ID_CONTABILE: string = 'Identificativo contabile';
  public static ID_DOMINIO: string = 'Id dominio';
  public static ID_ENTRATA: string = 'Id entrata';
  public static ID_FLUSSO: string = 'Id rendicontazione';
  public static ID_INCASSO: string = 'Id incasso';
  public static ID_INTERMEDIARIO: string = 'Id intermediario';
  public static ID_INTERMEDIARIO_PSP: string = 'Id intermediario Psp';
  public static ID_TIPO_PENDENZA: string = 'Id tipo pendenza';
  public static ID_PAGAMENTO: string = 'Id pagamento';
  public static ID_PENDENZA: string = 'Id pendenza';
  public static ID_PSP: string = 'Id PSP';
  public static ID_SESSIONE: string = 'Id carrello';
  public static ID_STAZIONE: string = 'Id stazione';
  public static ID_UNITA: string = 'Id unità';
  public static IDENTIFICATIVO: string = 'Identificativo';
  public static IDF_IUV: string = 'Flusso/IUV';
  public static IMPORTO: string = 'Importo';
  public static INCASSO: string = 'Incasso';
  public static INOLTRO: string = 'Inoltro';
  public static INDICE: string = 'Indice';
  public static INDICE_PENDENZA: string = 'Indice pendenza';
  public static INDIRIZZO: string = 'Indirizzo';
  public static ISTITUTO: string = 'Istituto';
  public static IUR: string = 'Riscossione (IUR)';
  public static IUR_SIGLA: string = 'IUR';
  public static IUV: string = 'IUV';
  public static IUV_CODEC: string = 'Codifica IUV';
  public static IUV_GENERATION: string = 'Generazione IUV interna';
  public static IUV_PREFIX: string = 'Prefisso IUV';
  public static IUV_REGEX: string = 'RegEx IUV';

  public static KEY_STORE_LOC: string = 'KeyStore Location';
  public static KEY_STORE_PWD: string = 'KeyStore Password';

  public static LUOGO: string = 'Località';
  public static LAYOUT_FORM_DATI: string = 'Layout form dati';

  public static MODULO: string = 'Modulo interno che ha emesso l\'evento';
  public static MODELLO_PAGAMENTO: string = 'Modello pagamento';
  public static MODIFICA_DOMINIO: string = 'Modifica dominio';
  public static MODIFICA_ENTE_CREDITORE: string = 'Modifica Ente creditore';
  public static MY_BANK: string = 'My Bank';

  public static NAZIONE: string = 'Nazione';
  public static NO_PSP: string = 'PSP non pervenuto';
  public static NUMERO_PAY: string = 'Numero di pagamenti';
  public static NOME: string = 'Nome';
  public static NON_PRESENTE: string = 'Non presente';
  public static NESSUNA: string = 'Nessuna';
  public static NESSUNO: string = 'Nessuno';
  public static NON_CONFIGURATO: string = 'Non configurato';
  public static NUOVO_DOMINIO: string = 'Nuovo dominio';
  public static NUOVO_ENTE_CREDITORE: string = 'Nuovo Ente creditore';

  public static OPERATORE_MITTENTE: string = 'Operatore mittente';
  public static OPERAZIONI: string = 'Operazioni';
  public static OPERAZIONI_FALLITE: string = 'Operazioni fallite';
  public static OPERAZIONI_ESEGUITE: string = 'Operazioni eseguite';
  public static OPERAZIONI_TOTALI: string = 'Operazioni totali';

  public static PAGO_PA: string = 'Principal pagoPa';
  public static PARAMETRI: string = 'Parametri';
  public static PASSWORD: string = 'Password';
  public static PEC: string = 'Pec';
  public static PENDENZA: string = 'Pendenza';
  public static PENDENZE: string = 'Pendenze';
  public static PORTA: string = 'Porta';
  public static PRINCIPAL: string = 'Principal';
  public static PROMEMORIA_AVVISO_PAGAMENTO: string = 'Promemoria avviso pagamento';
  public static PROMEMORIA_RICEVUTA_TELEMATICA: string = 'Promemoria ricevuta telematica';
  public static PROVINCIA: string = 'Provincia';
  public static PSP: string = 'Psp';

  public static RAGIONE_SOCIALE: string = 'Ragione sociale';
  public static RIEPILOGO: string = 'Riepilogo informazioni';
  public static RISORSA: string = 'Risorsa';
  public static RUOLO: string = 'Ruolo';
  public static RUOLI: string = 'Ruoli';

  public static SCADENZA: string = 'Scadenza';
  public static SCT: string = 'SCT';
  public static SECRET_CODE: string = 'Codice di segregazione';
  public static SERVIZIO_AVVISATURA: string = 'Servizio avvisatura';
  public static SERVIZIO_RPT: string = 'Servizio RPT';
  public static SOGGETTO_VERSANTE: string = 'Versante';
  public static SOTTOTIPO_ESITO: string = 'Sottotipo esito';
  public static STATO: string = 'Stato';
  public static STAZIONE: string = 'Stazione';

  public static TASSONOMIA: string = 'Tipo (ente)';
  public static TASSONOMIA_AVVISO: string = 'Tassonomia avviso';
  public static TELEFONO: string = 'Telefono';
  public static TEMPLATE_OGGETTO: string = 'Template oggetto';
  public static TEMPLATE_MESSAGGIO: string = 'Template messaggio';
  public static TERZI: string = 'Pagabile da terzi';
  public static TIPI_PENDENZA: string = 'Tipi pendenza';
  public static TIPO: string = 'Tipo';
  public static TIPO_ALLEGATO: string = 'Tipo allegato';
  public static TIPO_AUTH: string = 'Tipo autenticazione';
  public static TIPO_CONTABILITA: string = 'Tipo contabilità';
  public static TIPO_EVENTO: string = 'Tipo evento';
  public static TIPO_LAYOUT: string = 'Tipo layout';
  public static TIPO_OPERAZIONE: string = 'Tipo operazione';
  public static TIPO_RISCOSSIONE: string = 'Tipo riscossione';
  public static TIPO_TEMPLATE: string = 'Tipo template';
  public static TIPO_VERSAMENTO: string = 'Tipo versamento';
  public static TIPOLOGIA: string = 'Tipologia';
  public static TRASFORMAZIONE: string = 'Trasformazione';
  public static TRN: string = 'Id operazione di riversamento (TRN)';
  public static TRUST_STORE_LOC: string = 'TrustStore Location';
  public static TRUST_STORE_PWD: string = 'TrustStore Password';
  public static TUTTE: string = 'Tutte';
  public static TUTTI: string = 'Tutti';

  public static UNDEFINED: string = 'Non specificato';
  public static UNITA_OPERATIVA: string = 'Unità operativa';
  public static UNITA_OPERATIVE: string = 'Unità operative';
  public static URL: string = 'URL';
  public static USERNAME: string = 'Username';

  public static VALIDAZIONE: string = 'Validazione';
  public static VALIDITA: string = 'Validità';
  public static VALORE_PREDEFINITO: string = 'valore predefinito';
  public static VERSIONE_API: string = 'Versione API';
  public static VISUALIZZAZIONE: string = 'Visualizzazione';
  public static VISTA_COMPLETA: string = 'Vista completa';

  public static WEB: string = 'Sito web';


  //Validation Error Messages
  public static VALIDATION_IT_MESSAGES: any = {
    // Italian error messages
    required: 'Campo obbligatorio.',
    minLength: 'Numero minimo di caratteri richiesti: {{minimumLength}} (valore corrente: {{currentLength}})',
    maxLength: 'Numero massimo di caratteri: {{maximumLength}} (valore corrente: {{currentLength}})',
    pattern: 'Schema non corrispondente: {{requiredPattern}}',
    format: function (error) {
      switch (error.requiredFormat) {
        case 'date':
          return 'Formato data non corrispondente';
        case 'time':
          return 'Formato orario non corrispondente';
        case 'date-time':
          return 'Formato data/ora non corrispondente';
        case 'email':
          return 'Indirizzo email nonvalido, es. "name@example.com"';
        case 'hostname':
          return 'Hostname non valido, es. "example.com"';
        case 'ipv4':
          return 'Indirizzo IPv4 non valido, es. "127.0.0.1"';
        case 'ipv6':
          return 'Indirizzo IPv6 non valido, es. "1234:5678:9ABC:DEF0:1234:5678:9ABC:DEF0"';
        // Add examples for 'uri', 'uri-reference', and 'uri-template'
        // case 'uri': case 'uri-reference': case 'uri-template':
        case 'url':
          return 'Url non valido, es. "http://www.example.com/page.html"';
        case 'uuid':
          return 'UUID non valido, es. "12345678-9ABC-DEF0-1234-56789ABCDEF0"';
        case 'color':
          return 'Colore no valido, es. "#FFFFFF" or "rgb(255, 255, 255)"';
        case 'json-pointer':
          return 'Puntatore JSON non valido, es. "/pointer/to/something"';
        case 'relative-json-pointer':
          return 'Puntatore JSON relativo non valido, es. "2/pointer/to/something"';
        case 'regex':
          return 'Espressione regolare no valida, es. "(1-)?\\d{3}-\\d{3}-\\d{4}"';
        default:
          return 'Valore non correttamente formattato ' + error.requiredFormat;
      }
    },
    minimum: 'Valore minimo {{minimumValue}} o maggiore',
    exclusiveMinimum: 'Valore minimo richiesto almeno {{exclusiveMinimumValue}}',
    maximum: 'Valore massimo {{maximumValue}} o inferiore',
    exclusiveMaximum: 'Valore massimo richiesto al più {{exclusiveMaximumValue}}',
    multipleOf: function (error) {
      if ((1 / error.multipleOfValue) % 10 === 0) {
        const decimals = Math.log10(1 / error.multipleOfValue);
        return `Numero decimali richiesti ${decimals} o meno.`;
      } else {
        return `Deve essere un multiplo di ${error.multipleOfValue}.`;
      }
    },
    minProperties: 'Proprietà richieste: {{minimumProperties}} o più (proprietà correnti: {{currentProperties}})',
    maxProperties: 'Proprietà richieste: {{maximumProperties}} o meno (proprietà correnti: {{currentProperties}})',
    minItems: 'Elementi richiesti: {{minimumItems}} o più (elementi correnti: {{currentItems}})',
    maxItems: 'Elementi richiesti: {{maximumItems}} or meno (elementi correnti: {{currentItems}})',
    uniqueItems: 'Ogni elemento deve essere unico',
    // Note: No default error messages for 'type', 'const', 'enum', or 'dependencies'
  };


}
