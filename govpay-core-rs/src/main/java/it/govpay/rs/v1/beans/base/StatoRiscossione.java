package it.govpay.rs.v1.beans.base;

import org.codehaus.jackson.annotate.JsonValue;

/**
 * Stato della riscossione
 **/


/**
 * Stato della riscossione
 */
public enum StatoRiscossione {
  
  
  
  
  RISCOSSA("Riscossa"),
  
  
  INCASSATA("Incassata");
  
  
  

  private String value;

  StatoRiscossione(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  public static StatoRiscossione fromValue(String text) {
    for (StatoRiscossione b : StatoRiscossione.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



