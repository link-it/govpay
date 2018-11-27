package it.govpay.core.beans.tracciati;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Gets or Sets statoOperazionePendenza
 */
public enum StatoOperazionePendenza {
  
  
  
  
  ESEGUITO("ESEGUITO"),
  
  
  SCARTATO("SCARTATO"),
  
  
  NON_VALIDO("NON_VALIDO");
  
  
  

  private String value;

  StatoOperazionePendenza(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(this.value);
  }

  @JsonCreator
  public static StatoOperazionePendenza fromValue(String text) {
    for (StatoOperazionePendenza b : StatoOperazionePendenza.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



