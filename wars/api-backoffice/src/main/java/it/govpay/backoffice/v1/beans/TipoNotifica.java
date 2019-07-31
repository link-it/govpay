package it.govpay.backoffice.v1.beans;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets tipoNotifica
 */
public enum TipoNotifica {
  
  
  
  
  ATTIVAZIONE("ATTIVAZIONE"),
  
  
  RICEVUTA("RICEVUTA"),
  
  
  ANNULLAMENTO("ANNULLAMENTO"),
  
  
  FALLIMENTO("FALLIMENTO");
  
  
  

  private String value;

  TipoNotifica(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static TipoNotifica fromValue(String text) {
    for (TipoNotifica b : TipoNotifica.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



