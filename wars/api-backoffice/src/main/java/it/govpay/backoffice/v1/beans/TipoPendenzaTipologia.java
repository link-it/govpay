package it.govpay.backoffice.v1.beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Gets or Sets tipoPendenzaTipologia
 */
public enum TipoPendenzaTipologia {
  
  
  
  
  SPONTANEA("spontanea"),
  
  
  DOVUTA("dovuta");
  
  
  

  private String value;

  TipoPendenzaTipologia(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static TipoPendenzaTipologia fromValue(String text) {
    for (TipoPendenzaTipologia b : TipoPendenzaTipologia.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



