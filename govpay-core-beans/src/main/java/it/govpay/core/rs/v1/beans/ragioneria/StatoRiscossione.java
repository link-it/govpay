package it.govpay.core.rs.v1.beans.ragioneria;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;


/**
 * Stato della riscossione
 */
public enum StatoRiscossione {
  
  
  
  
  RISCOSSA("RISCOSSA"),
  
  
  INCASSATA("INCASSATA");
  
  
  

  private String value;

  StatoRiscossione(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static StatoRiscossione fromValue(String text) {
    for (StatoRiscossione b : StatoRiscossione.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



