import { Injectable } from '@angular/core';

@Injectable()
export class Voce {

  public static ABILITATO: string = 'Abilitato';
  public static AUTORIZZAZIONI: string = 'Autorizzazioni';
  public static AUX: string = 'Aux';
  public static AVVISO: string = 'Avviso';

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
  public static CONTENUTO_ALLEGATO: string = 'Contenuto allegato';

  public static DATA: string = 'Data';
  public static DATA_CONTABILE: string = 'Data contabile';
  public static DATA_ESITO: string = 'Data esito';
  public static DATA_ESECUZIONE_RISCOSSIONE: string = 'Data esecuzione riscossione';
  public static DATA_REGOLAMENTO: string = 'Data regolamento';
  public static DATA_RICEVUTA: string = 'Data ricevuta';
  public static DATA_RICHIESTA: string = 'Data richiesta';
  public static DATA_RICHIESTA_PAGAMENTO: string = 'Data richiesta pagamento';
  public static DATA_RISPOSTA: string = 'Data risposta';
  public static DATA_VALUTA_INCASSO: string = 'Data valuta incasso';
  public static DEBITORE: string = 'Debitore';
  public static DENOMINAZIONE: string = 'Denominazione';
  public static DESCRIZIONE: string = 'Descrizione';
  public static DOMINIO: string = 'Dominio';

  public static EMAIL: string = 'Email';
  public static ENTE_CREDITORE: string = 'Ente creditore';
  public static EROGATORE: string = 'Erogatore';
  public static ESITO: string = 'Esito';

  public static FAX: string = 'Fax';
  public static FRUITORE: string = 'Fruitore';

  public static GLN: string = 'Global location number';

  public static ID_A2A: string = 'Id A2A';
  public static ID_CANALE: string = 'Id canale';
  public static ID_DOMINIO: string = 'Id dominio';
  public static ID_ENTRATA: string = 'Id entrata';
  public static ID_FLUSSO: string = 'Id rendicontazione';
  public static ID_INCASSO: string = 'Id incasso';
  public static ID_INTERMEDIARIO: string = 'Id intermediario';
  public static ID_PSP: string = 'Id PSP';
  public static ID_PENDENZA: string = 'Id pendenza';
  public static ID_STAZIONE: string = 'Id stazione';
  public static ID_UNITA: string = 'Id unità';
  public static IDENTIFICATIVO: string = 'Identificativo';
  public static IMPORTO: string = 'Importo';
  public static INCASSO: string = 'Incasso';
  public static INDICE_PENDENZA: string = 'Indice pendenza';
  public static INDIRIZZO: string = 'Indirizzo';
  public static ISTITUTO: string = 'Istituto';
  public static IUR: string = 'Riscossione (IUR)';
  public static IUV: string = 'IUV';
  public static IUV_CODEC: string = 'Codifica IUV';
  public static IUV_GENERATION: string = 'Generazione IUV interna';
  public static IUV_PREFIX: string = 'Prefisso IUV';
  public static IUV_REGEX: string = 'RegEx IUV';

  public static KEY_STORE_LOC: string = 'KeyStore Location';
  public static KEY_STORE_PWD: string = 'KeyStore Password';

  public static LUOGO: string = 'Località';

  public static MODULO: string = 'Modulo interno che ha emesso l\'evento';

  public static NAZIONE: string = 'Nazione';
  public static NO_PSP: string = 'PSP non pervenuto';
  public static NUMERO_PAY: string = 'Numero di pagamenti';
  public static NOME: string = 'Nome';
  public static NESSUNA: string = 'Nessuna';

  public static OPERAZIONI: string = 'Operazioni';

  public static PAGO_PA: string = 'Principal pagoPa';
  public static PARAMETRI: string = 'Parametri';
  public static PASSWORD: string = 'Password';
  public static PEC: string = 'Pec';
  public static PENDENZA: string = 'Pendenza';
  public static PRINCIPAL: string = 'Principal';
  public static PROVINCIA: string = 'Provincia';
  public static PSP: string = 'Psp';

  public static RAGIONE_SOCIALE: string = 'Ragione sociale';
  public static RISORSA: string = 'Risorsa';

  public static SCADENZA: string = 'Scadenza';
  public static SECRET_CODE: string = 'Codice di segregazione';
  public static STATO: string = 'Stato';
  public static STAZIONE: string = 'Stazione';

  public static TASSONOMIA: string = 'Tipo (ente)';
  public static TASSONOMIA_AVVISO: string = 'Tipo (AGID)';
  public static TELEFONO: string = 'Telefono';
  public static TIPO: string = 'Tipo';
  public static TIPO_ALLEGATO: string = 'Tipo allegato';
  public static TIPO_AUTH: string = 'Tipo autenticazione';
  public static TIPO_CONTABILITA: string = 'Tipo contabilità';
  public static TIPO_EVENTO: string = 'Tipo evento';
  public static TIPO_RISCOSSIONE: string = 'Tipo riscossione';
  public static TIPO_VERSAMENTO: string = 'Tipo versamento';
  public static TRN: string = 'Id operazione di riversamento (TRN)';
  public static TRUST_STORE_LOC: string = 'TrustStore Location';
  public static TRUST_STORE_PWD: string = 'TrustStore Password';

  public static UNDEFINED: string = 'Non specificato';
  public static URL: string = 'URL';
  public static USERNAME: string = 'Username';

  public static VALIDITA: string = 'Validità';
  public static VALORE_PREDEFINITO: string = 'valore predefinito';
  public static VERSIONE_API: string = 'Versione API';

  public static WEB: string = 'Sito web';

}
