package it.govpay.pendenze.v1.beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Tipologia di codifica del capitolo di bilancio
 */
public enum TipoContabilita {
  
  
  
  
  ENTRATA("ENTRATA"),
  
  
  SPECIALE("SPECIALE"),
  
  
  SIOPE("SIOPE"),
  
  
  ALTRO("ALTRO");
  
  
  

  private String value;

  TipoContabilita(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(this.value);
  }

  @JsonCreator
  public static TipoContabilita fromValue(String text) {
    for (TipoContabilita b : TipoContabilita.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



