package it.govpay.pendenze.v2.beans;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Gets or Sets modalitaAvvisaturaDigitale
 */
public enum ModalitaAvvisaturaDigitale {
  
  
  
  
  ASINCRONA("ASINCRONA"),
  
  
  SINCRONA("SINCRONA");
  
  
  

  private String value;

  ModalitaAvvisaturaDigitale(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static ModalitaAvvisaturaDigitale fromValue(String text) {
    for (ModalitaAvvisaturaDigitale b : ModalitaAvvisaturaDigitale.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



