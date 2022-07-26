package it.govpay.gde.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets RuoloEvento
 */
public enum RuoloEvento {
  
  CLIENT("CLIENT"),
  
  SERVER("SERVER");

  private String value;

  RuoloEvento(String value) {
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
  public static RuoloEvento fromValue(String value) {
    for (RuoloEvento b : RuoloEvento.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

