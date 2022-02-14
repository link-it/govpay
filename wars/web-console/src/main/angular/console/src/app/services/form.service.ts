import { Injectable } from '@angular/core';

@Injectable()
export class FormService {

  //Pattern validatori
  public static VAL_CODICE_FISCALE: string = '\\w{6}\\d{2}\\w{1}\\d{2}\\w{1}\\d{3}\\w{1}';
  public static VAL_PARTITA_IVA: string = '\\d{11}';
  public static VAL_CF_PI: string = '(\\w{6}\\d{2}\\w{1}\\d{2}\\w{1}\\d{3}\\w{1}|\\d{11})';

  public static FORM_PH_SELECT: string = 'Seleziona';

  //FORM_LABEL
  //Pendenze
  public static FORM_DOMINIO: string = 'Id dominio';
  public static FORM_PH_DOMINIO: string = '';
  public static FORM_A2A: string = 'Identificativo A2A';
  public static FORM_PH_A2A: string = '';
  public static FORM_DEBITORE: string = 'Debitore';
  public static FORM_PH_DEBITORE: string = 'Cod. Fisc. o P. IVA';
  public static FORM_STATO: string = 'Stato';
  public static FORM_ENTE_CREDITORE: string = 'Ente creditore';

  //Pagamenti
  public static FORM_VERSANTE: string = 'Versante';
  public static FORM_PH_VERSANTE: string = 'Cod. Fisc. o P. IVA';
  public static FORM_SESSIONE: string = 'Identificativo carrello';
  public static FORM_PH_SESSIONE: string = '';
  public static FORM_DATA_INIZIO: string = 'Data da';
  public static FORM_DATA_FINE: string = 'Data a';
  public static FORM_VERIFICATO: string = 'Stato verifica';
  public static FORM_LIVELLO_SEVERITA: string = 'Livello di errore';

  //PSP
  // public static FORM_BOLLO: string = 'Bollo';
  // public static FORM_STORNO: string = 'Storno';
  public static FORM_ABILITATO: string = 'Abilitato';

  //Domini
  public static FORM_RAGIONE_SOCIALE: string = 'Ragione sociale';
  public static FORM_STAZIONE: string = 'Identificativo stazione';
  public static FORM_INTERMEDIATO: string = 'Intermediato';
  public static FORM_PH_STAZIONE: string = '';
  public static FORM_PH_ABILITAZIONE: string = 'Stato abilitazione';

  //RPPS
  public static FORM_IUV: string = 'Codice IUV';
  public static FORM_PH_IUV: string = 'IUV/Numero avviso';
  public static FORM_CCP: string = 'Codice CCP';
  public static FORM_PH_CCP: string = 'Codice di pagamento';
  public static FORM_PAGAMENTO: string = 'Identificativo pagamento';
  public static FORM_PH_PAGAMENTO: string = '';
  public static FORM_PENDENZA: string = 'Identificativo pendenza';
  public static FORM_PH_PENDENZA: string = '';
  public static FORM_ESITO: string = 'Esito pagamento';
  public static FORM_IUR: string = 'Identificativo riscossione';
  public static FORM_PH_IUR: string = '';

  //Ruoli
  public static FORM_RUOLO: string = 'Ruolo';
  public static FORM_PH_RUOLO: string = '';
  public static FORM_PRINCIPAL: string = 'Principal';
  public static FORM_PH_PRINCIPAL: string = '';
  public static FORM_SERVIZIO: string = 'Servizio';

  //Riscossioni
  public static FORM_TIPO_RISCOSSIONE: string = 'Tipo';

  //Rendicontazioni
  public static FORM_IDENTIFICATIVO_FLUSSO: string = 'Identificativo flusso';
  public static FORM_DATA_REG_INIZIO: string = 'Data regolamento';
  public static FORM_DATA_REG_FINE: string = 'Data regolamento';
  public static FORM_DATA_RISC_INIZIO: string = 'Data riscossione';
  public static FORM_DATA_RISC_FINE: string = 'Data riscossione';
  public static FORM_PH_DATA_REG_INIZIO: string = '(Inzio)';
  public static FORM_PH_DATA_REG_FINE: string = '(Fine)';
  public static FORM_PH_DATA_RISC_INIZIO: string = '(Inzio)';
  public static FORM_PH_DATA_RISC_FINE: string = '(Fine)';

  //Giornale eventi
  public static FORM_ESITO_GDE: string = 'Esito';
  public static FORM_TIPI_EVENTO: string = 'Tipo evento';
  public static FORM_COMPONENTE: string = 'Componente';

  //Tipi pendenze
  public static FORM_TIPOLOGIA: string = 'Tipologia';
  public static FORM_DESCRIZIONE: string = 'Descrizione';
  public static FORM_ID_TIPO_PENDENZA: string = 'Id tipo pendenza';
  public static FORM_TIPO_PENDENZA: string = 'Tipo pendenza';

  //Incassi
  public static FORM_SCT: string = 'SCT';
}
