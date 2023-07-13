package it.govpay.pagamento.v3.beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets StatoPendenza
 */
public enum StatoPendenza {
  ESEGUITA("ESEGUITA"),
  
  NON_ESEGUITA("NON_ESEGUITA"),
  
  ESEGUITA_PARZIALE("ESEGUITA_PARZIALE"),
  
  ANNULLATA("ANNULLATA"),
  
  SCADUTA("SCADUTA"),
  
  ANOMALA("ANOMALA");

  private String value;

  StatoPendenza(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static StatoPendenza fromValue(String text) {
    for (StatoPendenza b : StatoPendenza.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
  
}
