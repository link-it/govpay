package it.govpay.backoffice.v1.beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Gets or Sets statoTracciatoPendenza
 */
public enum StatoTracciatoPendenza {
  
  
  
  
  IN_ATTESA("IN_ATTESA"),
  
  
  IN_ELABORAZIONE("IN_ELABORAZIONE"),
  
  
  ESEGUITO("ESEGUITO"),
  
  
  ESEGUITO_CON_ERRORI("ESEGUITO_CON_ERRORI"),
  
  
  SCARTATO("SCARTATO"),
  
  
  ELABORAZIONE_STAMPA("ELABORAZIONE_STAMPA");
  
  
  

  private String value;

  StatoTracciatoPendenza(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static StatoTracciatoPendenza fromValue(String text) {
    for (StatoTracciatoPendenza b : StatoTracciatoPendenza.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



