package it.govpay.ragioneria.v3.beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Stato del flusso di rendicontazione
 */
public enum StatoFlussoRendicontazione {
  ACQUISITO("Acquisito"),
  
  ANOMALO("Anomalo"),
  
  RIFIUTATO("Rifiutato");

  private String value;

  StatoFlussoRendicontazione(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static StatoFlussoRendicontazione fromValue(String text) {
    for (StatoFlussoRendicontazione b : StatoFlussoRendicontazione.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
  
}
