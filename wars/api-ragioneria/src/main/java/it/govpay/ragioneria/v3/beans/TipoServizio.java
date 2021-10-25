package it.govpay.ragioneria.v3.beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets TipoServizio
 */
public enum TipoServizio {
  ANAGRAFICA_PAGOPA("Anagrafica PagoPA"),
  
  ANAGRAFICA_CREDITORE("Anagrafica Creditore"),
  
  ANAGRAFICA_APPLICAZIONI("Anagrafica Applicazioni"),
  
  ANAGRAFICA_RUOLI("Anagrafica Ruoli"),
  
  PAGAMENTI("Pagamenti"),
  
  PENDENZE("Pendenze"),
  
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
