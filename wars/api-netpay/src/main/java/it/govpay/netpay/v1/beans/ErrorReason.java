package it.govpay.netpay.v1.beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enumeration con i possibili casi di errore.
 */
public enum ErrorReason {
  INVALID_REQUEST("INVALID_REQUEST"),
  
  DOMAIN_ID_NOT_VALID("DOMAIN_ID_NOT_VALID"),
  
  PDP_RPT_RT_NON_TROVATA("PDP_RPT_RT_NON_TROVATA"),
  
  WS_DOMINIO_UB_MANCANTE("WS_DOMINIO_UB_MANCANTE"),
  
  WS_PARAMETRI_MANCANTI("WS_PARAMETRI_MANCANTI"),
  
  WS_ERRORE_INTERNO("WS_ERRORE_INTERNO"),
  
  WS_GENERIC_ERROR("WS_GENERIC_ERROR"),
  
  IBAN_NON_CENSITO("IBAN_NON_CENSITO");

  private String value;

  ErrorReason(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static ErrorReason fromValue(String text) {
    for (ErrorReason b : ErrorReason.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
  
}
