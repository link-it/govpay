package it.govpay.ec.v2.beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets LinguaSecondaria
 */
public enum LinguaSecondaria {
  FALSE("false"),
  
  DE("de"),
  
  EN("en"),
  
  FR("fr"),
  
  SL("sl");

  private String value;

  LinguaSecondaria(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static LinguaSecondaria fromValue(String text) {
    for (LinguaSecondaria b : LinguaSecondaria.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
  
}
