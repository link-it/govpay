package it.govpay.rs.v1.beans.base;

import java.util.Objects;
import org.codehaus.jackson.annotate.JsonValue;

/**
 * Modello di versamento realizzata dal canale
 **/


/**
 * Modello di versamento realizzata dal canale
 */
public enum ModelloPagamento {
  
  
  
  
  _0("0"),
  
  
  _1("1"),
  
  
  _2("2"),
  
  
  _4("4");
  
  
  

  private String value;

  ModelloPagamento(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  public static ModelloPagamento fromValue(String text) {
    for (ModelloPagamento b : ModelloPagamento.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



