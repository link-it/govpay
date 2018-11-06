package it.govpay.core.rs.v1.beans.base;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Indica se l'entrata e' pagabile da soggetti terzi
 */
public enum TipoTributoPagaTerzi {
  
  
  
  
  TRUE("true"),
  
  
  FALSE("false");
  
  
  

  private String value;

  TipoTributoPagaTerzi(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static TipoTributoPagaTerzi fromValue(String text) {
    for (TipoTributoPagaTerzi b : TipoTributoPagaTerzi.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



