package it.govpay.rs.v1.beans.base;

import java.util.Objects;
import org.codehaus.jackson.annotate.JsonValue;

/**
 * Stato della pendenza
 **/


/**
 * Stato della pendenza
 */
public enum StatoPendenza {
  
  
  
  
  PAGATA("Pagata"),
  
  
  NON_PAGATA("Non pagata"),
  
  
  PAGATA_PARZIALMENTE("Pagata parzialmente"),
  
  
  SCADUTA("Scaduta"),
  
  
  ANNULLATA("Annullata"),
  
  
  ANOMALIA("Anomalia");
  
  
  

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



