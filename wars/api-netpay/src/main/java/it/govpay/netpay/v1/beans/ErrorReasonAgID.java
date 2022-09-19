package it.govpay.netpay.v1.beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enumeration con i possibili casi di errore secondo la codifica standard AgID
 */
public enum ErrorReasonAgID {
  SYSTEM_ERROR("PPT_SYSTEM_ERROR"),
  
  RT_SCONOSCIUTA("PPT_RT_SCONOSCIUTA"),
  
  DOMINIO_SCONOSCIUTO("PPT_DOMINIO_SCONOSCIUTO"),
  
  IBAN_NON_CENSITO("PPT_IBAN_NON_CENSITO");

  private String value;

  ErrorReasonAgID(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static ErrorReasonAgID fromValue(String text) {
    for (ErrorReasonAgID b : ErrorReasonAgID.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
  
}
