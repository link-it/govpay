package it.govpay.ragioneria.v3.beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Stato della rendicontazione
 */
public enum StatoRendicontazione {
  OK("OK"),
  
  ANOMALA("ANOMALA"),
  
  ALTRO_INTERMEDIARIO("ALTRO_INTERMEDIARIO");

  private String value;

  StatoRendicontazione(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static StatoRendicontazione fromValue(String text) {
    for (StatoRendicontazione b : StatoRendicontazione.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
  
}
