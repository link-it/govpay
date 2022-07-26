package it.govpay.gde.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets EsitoEvento
 */
public enum EsitoEvento {
  
  OK("OK"),
  
  KO("KO"),
  
  FAIL("FAIL");

  private String value;

  EsitoEvento(String value) {
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
  public static EsitoEvento fromValue(String value) {
    for (EsitoEvento b : EsitoEvento.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

