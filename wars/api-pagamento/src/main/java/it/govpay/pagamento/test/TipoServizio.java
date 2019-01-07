package it.govpay.pagamento.test;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Servizio oggetto dell&#x27;autorizzazione  * Anagrafica PagoPA  * Anagrafica Creditore  * Anagrafica Applicazioni  * Anagrafica Ruoli  * Pendenze e Pagamenti  * Pendenze e Pagamenti propri   * Rendicontazioni e Incassi  * Giornale degli Eventi  * Statistiche  * Configurazione e Manutenzione
 */
public enum TipoServizio {
ANAGRAFICA_PAGOPA("Anagrafica PagoPA"),
  ANAGRAFICA_CREDITORE("Anagrafica Creditore"),
  ANAGRAFICA_APPLICAZIONI("Anagrafica Applicazioni"),
  ANAGRAFICA_RUOLI("Anagrafica Ruoli"),
  PAGAMENTI_E_PENDENZE("Pagamenti e Pendenze"),
  RENDICONTAZIONI_E_INCASSI("Rendicontazioni e Incassi"),
  GIORNALE_DEGLI_EVENTI("Giornale degli Eventi"),
  CONFIGURAZIONE_E_MANUTENZIONE("Configurazione e manutenzione");

  private String value;

  TipoServizio(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static TipoServizio fromValue(String text) {
    for (TipoServizio b : TipoServizio.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
  
}
