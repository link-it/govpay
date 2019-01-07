package it.govpay.pagamento.test;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Tipologia di codifica del capitolo di bilancio
 */
public enum TipoContabilita {
CAPITOLO("CAPITOLO"),
  SPECIALE("SPECIALE"),
  SIOPE("SIOPE"),
  ALTRO("ALTRO");

  private String value;

  TipoContabilita(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static TipoContabilita fromValue(String text) {
    for (TipoContabilita b : TipoContabilita.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
  
}
