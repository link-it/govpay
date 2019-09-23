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
  public static FORM_DOMINIO: string = 'Identificativo Dominio';
  public static FORM_PH_DOMINIO: string = '';
  public static FORM_A2A: string = 'Identificativo A2A';
  public static FORM_PH_A2A: string = '';
  public static FORM_DEBITORE: string = 'Debitore';
  public static FORM_PH_DEBITORE: string = 'Codice fiscale';
  public static FORM_STATO: string = 'Stato';
  public static FORM_ENTE_CREDITORE: string = 'Ente creditore';

  //Pagamenti
  public static FORM_VERSANTE: string = 'Versante';
  public static FORM_PH_VERSANTE: string = 'Codice fiscale';
  public static FORM_SESSIONE: string = 'Identificativo sessione';
  public static FORM_PH_SESSIONE: string = '';
  public static FORM_DATA_INIZIO: string = 'Data da';
  public static FORM_DATA_FINE: string = 'Data a';
  public static FORM_VERIFICATO: string = 'Stato verifica';

  //PSP
  // public static FORM_BOLLO: string = 'Bollo';
  // public static FORM_STORNO: string = 'Storno';
  public static FORM_ABILITATO: string = 'Abilitato';

  //Domini
  public static FORM_STAZIONE: string = 'Identificativo stazione';
  public static FORM_PH_STAZIONE: string = '';

  //RPPS
  public static FORM_IUV: string = 'Codice IUV';
  public static FORM_PH_IUV: string = '';
  public static FORM_CCP: string = 'Codice CCP';
  public static FORM_PH_CCP: string = 'Codice di pagamento';
  public static FORM_PAGAMENTO: string = 'Identificativo pagamento';
  public static FORM_PH_PAGAMENTO: string = '';
  public static FORM_PENDENZA: string = 'Identificativo pendenza';
  public static FORM_PH_PENDENZA: string = '';
  public static FORM_ESITO: string = 'Esito pagamento';

  //Ruoli
  public static FORM_RUOLO: string = 'Ruolo';
  public static FORM_PH_RUOLO: string = '';
  public static FORM_PRINCIPAL: string = 'Principal';
  public static FORM_PH_PRINCIPAL: string = '';
  public static FORM_SERVIZIO: string = 'Servizio';

  //Riscossioni
  public static FORM_TIPO_RISCOSSIONE: string = 'Tipo';

  //Rendicontazioni
  public static FORM_DATA_RISC_INIZIO: string = 'Data riscossione';
  public static FORM_DATA_RISC_FINE: string = 'Data riscossione';
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

}
