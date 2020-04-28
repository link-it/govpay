package it.govpay.pendenze.v2.beans;


/**
 * Stato dell&#x27;avviso
 **/
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Stato dell'avviso
 */
public enum StatoAvviso {
  
  
  
  
  DUPLICATA("DUPLICATA"),
  
  
  NON_ESEGUITA("NON_ESEGUITA"),
  
  
  ANNULLATA("ANNULLATA"),
  
  
  SCONOSCIUTA("SCONOSCIUTA"),
  
  
  SCADUTA("SCADUTA");
  
  
  

  private String value;

  StatoAvviso(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static StatoAvviso fromValue(String text) {
    for (StatoAvviso b : StatoAvviso.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



