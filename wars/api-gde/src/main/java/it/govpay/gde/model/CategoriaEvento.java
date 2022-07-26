package it.govpay.gde.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets CategoriaEvento
 */
public enum CategoriaEvento {
  
  INTERNO("INTERNO"),
  
  INTERFACCIA("INTERFACCIA"),
  
  UTENTE("UTENTE");

  private String value;

  CategoriaEvento(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static CategoriaEvento fromValue(String value) {
    for (CategoriaEvento b : CategoriaEvento.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

