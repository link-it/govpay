package it.govpay.rs.v1.beans.base;

import java.util.Objects;
import org.codehaus.jackson.annotate.JsonValue;

/**
 * Esito della richiesta di pagamento
 **/


/**
 * Esito della richiesta di pagamento
 */
public enum EsitoRpt {
  
  
  
  
  PAGAMENTO_ESEGUITO("Pagamento eseguito"),
  
  
  PAGAMENTO_NON_ESEGUITO("Pagamento non eseguito"),
  
  
  PAGAMENTO_PARZIALMENTE_ESEGUITO("Pagamento parzialmente eseguito"),
  
  
  DECORRENZA_TERMINI("Decorrenza termini"),
  
  
  DECORRENZA_TERMINI_PARZIALE("Decorrenza termini parziale");
  
  
  

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



