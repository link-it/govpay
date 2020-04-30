package it.govpay.backoffice.v1.beans;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Gets or Sets raggruppamentoStatisticaRendicontazione
 */
public enum RaggruppamentoStatisticaRendicontazione {
  
  
  
  
  FLUSSO_RENDICONTAZIONE("FLUSSO_RENDICONTAZIONE"),
  
  
  DIVISIONE("DIVISIONE"),
  
  
  DIREZIONE("DIREZIONE");
  
  
  

  private String value;

  RaggruppamentoStatisticaRendicontazione(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static RaggruppamentoStatisticaRendicontazione fromValue(String text) {
    for (RaggruppamentoStatisticaRendicontazione b : RaggruppamentoStatisticaRendicontazione.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



