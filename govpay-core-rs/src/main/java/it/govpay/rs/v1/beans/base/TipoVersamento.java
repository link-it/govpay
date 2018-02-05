package it.govpay.rs.v1.beans.base;

import java.util.Objects;
import org.codehaus.jackson.annotate.JsonValue;

/**
 * Tipologia di versamento
 **/


/**
 * Tipologia di versamento
 */
public enum TipoVersamento {
  
  
  
  
  PO("PO"),
  
  
  BP("BP"),
  
  
  BBT("BBT"),
  
  
  CP("CP"),
  
  
  AD("AD"),
  
  
  OBEP("OBEP"),
  
  
  OTH("OTH");
  
  
  

  private String value;

  TipoVersamento(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  public static TipoVersamento fromValue(String text) {
    for (TipoVersamento b : TipoVersamento.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



