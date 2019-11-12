package it.govpay.backoffice.v1.beans;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Gets or Sets raggruppamentoStatistica
 */
public enum RaggruppamentoStatistica {
  
  
  
  
  DOMINIO("DOMINIO"),
  
  
  UNITA_OPERATIVA("UNITA_OPERATIVA"),
  
  
  TIPO_PENDENZA("TIPO_PENDENZA"),
  
  
  DIVISIONE("DIVISIONE"),
  
  
  DIREZIONE("DIREZIONE"),
  
  
  APPLICAZIONE("APPLICAZIONE");
  
  
  

  private String value;

  RaggruppamentoStatistica(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static RaggruppamentoStatistica fromValue(String text) {
    for (RaggruppamentoStatistica b : RaggruppamentoStatistica.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



