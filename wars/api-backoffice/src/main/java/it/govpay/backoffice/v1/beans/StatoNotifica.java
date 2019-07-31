package it.govpay.backoffice.v1.beans;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Gets or Sets statoNotifica
 */
public enum StatoNotifica {
  
  
  
  
  SPEDITA("SPEDITA"),
  
  
  DA_SPEDIRE("DA_SPEDIRE"),
  
  
  ANNULLATA("ANNULLATA");
  
  
  

  private String value;

  StatoNotifica(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static StatoNotifica fromValue(String text) {
    for (StatoNotifica b : StatoNotifica.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



