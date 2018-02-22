package it.govpay.rs.v1.beans.base;

import org.codehaus.jackson.annotate.JsonValue;

/**
 * Stato della pendenza:   * ESEGUITO: Pagata  * NON_ESEGUITO: Da pagare  * ESEGUITO_PARZIALE: Pagata parzialmente  * ANNULLATO: Annullata  * SCADUTO: Scaduta
 **/


/**
 * Stato della pendenza:   * ESEGUITO: Pagata  * NON_ESEGUITO: Da pagare  * ESEGUITO_PARZIALE: Pagata parzialmente  * ANNULLATO: Annullata  * SCADUTO: Scaduta
 */
public enum StatoPendenza {
  
  
  
  
  ESEGUITO("ESEGUITO"),
  
  
  NON_ESEGUITO("NON_ESEGUITO"),
  
  
  ESEGUITO_PARZIALE("ESEGUITO_PARZIALE"),
  
  
  ANNULLATO("ANNULLATO"),
  
  
  SCADUTO("SCADUTO");
  
  
  

  private String value;

  StatoPendenza(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  public static StatoPendenza fromValue(String text) {
    for (StatoPendenza b : StatoPendenza.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



