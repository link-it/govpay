package it.govpay.pagamento.test;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Stato dell&#x27;avviso
 */
public enum StatoAvviso {
PAGATO("Pagato"),
  NON_PAGATO("Non pagato"),
  SCADUTO("Scaduto"),
  ANNULLATO("Annullato");

  private String value;

  StatoAvviso(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static StatoAvviso fromValue(String text) {
    for (StatoAvviso b : StatoAvviso.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
  
}
