package it.govpay.pagamento.test;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * tipologia di soggetto, se persona fisica (F) o giuridica (G)
 */
public enum TipoSoggetto {
G("G"),
  F("F");

  private String value;

  TipoSoggetto(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static TipoSoggetto fromValue(String text) {
    for (TipoSoggetto b : TipoSoggetto.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
  
}
