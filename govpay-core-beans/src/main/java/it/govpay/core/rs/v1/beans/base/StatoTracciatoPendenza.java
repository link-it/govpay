package it.govpay.core.rs.v1.beans.base;

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
  
  
  SCARTATO("SCARTATO");
  
  
  

  private String value;

  StatoTracciatoPendenza(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(this.value);
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



