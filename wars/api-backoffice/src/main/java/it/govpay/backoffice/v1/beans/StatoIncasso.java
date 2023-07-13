package it.govpay.backoffice.v1.beans;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Gets or Sets StatoIncasso
 */
public enum StatoIncasso {
  
  
  
  
  IN_ELABORAZIONE("IN_ELABORAZIONE"),
  
  
  ACQUISITO("ACQUISITO"),
  
  
  ERRORE("ERRORE");
  
  
  

  private String value;

  StatoIncasso(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static StatoIncasso fromValue(String text) {
    for (StatoIncasso b : StatoIncasso.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



