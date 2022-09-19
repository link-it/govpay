package it.govpay.netpay.v1.beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Esito del pagamento
 */
public enum PaymentType {
  BBT("BBT"),
  
  BP("BP"),
  
  AD("AD"),
  
  CP("CP"),
  
  PO("PO"),
  
  OBEP("OBEP");

  private String value;

  PaymentType(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static PaymentType fromValue(String text) {
    for (PaymentType b : PaymentType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
  
}
