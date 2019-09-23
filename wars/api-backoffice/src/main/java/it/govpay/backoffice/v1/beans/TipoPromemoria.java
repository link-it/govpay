package it.govpay.backoffice.v1.beans;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets tipoPromemoria
 */
public enum TipoPromemoria {
  
  
  
  
  AVVISO_PAGAMENTO("AVVISO_PAGAMENTO"),
  
  
  RICEVUTA_TELEMATICA("RICEVUTA_TELEMATICA");
  
  
  

  private String value;

  TipoPromemoria(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static TipoPromemoria fromValue(String text) {
    for (TipoPromemoria b : TipoPromemoria.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



