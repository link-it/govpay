package it.govpay.backoffice.v1.beans;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Gets or Sets esitoEvento
 */
public enum EsitoEvento {
  
  
  
  
  OK("OK"),
  
  
  KO("KO"),
  
  
  FAIL("FAIL");
  
  
  

  private String value;

  EsitoEvento(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static EsitoEvento fromValue(String text) {
    for (EsitoEvento b : EsitoEvento.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



