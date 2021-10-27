package it.govpay.ragioneria.v3.beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Stato della riscossione
 */
public enum StatoRiscossione {
  RISCOSSA("RISCOSSA"),
  
  INCASSATA("INCASSATA");

  private String value;

  StatoRiscossione(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static StatoRiscossione fromValue(String text) {
    for (StatoRiscossione b : StatoRiscossione.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
  
}
