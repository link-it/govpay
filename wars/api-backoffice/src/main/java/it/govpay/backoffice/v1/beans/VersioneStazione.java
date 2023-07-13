package it.govpay.backoffice.v1.beans;


/**
 * Versione della stazione
 **/
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Versione della stazione
 */
public enum VersioneStazione {
  
  
  
  
  V1("V1"),
  
  
  V2("V2");
  
  
  

  private String value;

  VersioneStazione(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static VersioneStazione fromValue(String text) {
    for (VersioneStazione b : VersioneStazione.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



