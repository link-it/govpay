package it.govpay.core.rs.v1.beans.base;

import org.codehaus.jackson.annotate.JsonValue;

/**
 * Esito della richiesta di pagamento:  * IN_CORSO: Pagamento in corso  * RIFIUTATO: Pagamento rifiutato  * ESEGUITO: Pagamento eseguito  * NON_ESEGUITO: Pagamento non eseguito  * ESEGUITO_PARZIALE: Pagamento parzialmente eseguito  * DECORRENZA: Decorrenza termini  * DECORRENZA_PARZIALE: Decorrenza termini parziale 
 **/


/**
 * Esito della richiesta di pagamento:  * IN_CORSO: Pagamento in corso  * RIFIUTATO: Pagamento rifiutato  * ESEGUITO: Pagamento eseguito  * NON_ESEGUITO: Pagamento non eseguito  * ESEGUITO_PARZIALE: Pagamento parzialmente eseguito  * DECORRENZA: Decorrenza termini  * DECORRENZA_PARZIALE: Decorrenza termini parziale 
 */
public enum EsitoRpt {
  
  
  
  
  IN_CORSO("IN_CORSO"),
  
  
  RIFIUTATO("RIFIUTATO"),
  
  
  ESEGUITO("ESEGUITO"),
  
  
  NON_ESEGUITO("NON_ESEGUITO"),
  
  
  ESEGUITO_PARZIALE("ESEGUITO_PARZIALE"),
  
  
  DECORRENZA("DECORRENZA"),
  
  
  DECORENNZA_PARZIALE("DECORENNZA_PARZIALE");
  
  
  

  private String value;

  EsitoRpt(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  public static EsitoRpt fromValue(String text) {
    for (EsitoRpt b : EsitoRpt.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



