package it.govpay.backoffice.v1.beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Gets or Sets tipoOperazionePendenza
 */
public enum TipoOperazionePendenza {




  ADD("ADD"),


  DEL("DEL"),


  NON_VALIDA("NON_VALIDA");




  private String value;

  TipoOperazionePendenza(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(this.value);
  }

  @JsonCreator
  public static TipoOperazionePendenza fromValue(String text) {
    for (TipoOperazionePendenza b : TipoOperazionePendenza.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



