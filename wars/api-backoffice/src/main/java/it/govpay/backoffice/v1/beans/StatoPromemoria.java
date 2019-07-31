package it.govpay.backoffice.v1.beans;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Gets or Sets statoPromemoria
 */
public enum StatoPromemoria {
  
  
  
  
  SPEDITO("SPEDITO"),
  
  
  DA_SPEDIRE("DA_SPEDIRE"),
  
  
  ANNULLATO("ANNULLATO"),
  
  
  FALLITO("FALLITO");
  
  
  

  private String value;

  StatoPromemoria(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static StatoPromemoria fromValue(String text) {
    for (StatoPromemoria b : StatoPromemoria.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



