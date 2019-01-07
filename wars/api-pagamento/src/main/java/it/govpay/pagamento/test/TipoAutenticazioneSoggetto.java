package it.govpay.pagamento.test;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * modalita&#x27; di autenticazione del soggetto versante
 */
public enum TipoAutenticazioneSoggetto {
CNS("CNS"),
  USR("USR"),
  OTH("OTH"),
  N_A("N/A");

  private String value;

  TipoAutenticazioneSoggetto(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static TipoAutenticazioneSoggetto fromValue(String text) {
    for (TipoAutenticazioneSoggetto b : TipoAutenticazioneSoggetto.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
  
}
