package it.govpay.core.rs.v1.beans.base;


/**
 * Indica se l&#x27;entrata spontanea e&#x27; pagabile online
 **/
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Indica se l'entrata spontanea e' pagabile online
 */
public enum TipoTributoOnline {
  
  
  
  
  TRUE("true"),
  
  
  FALSE("false");
  
  
  

  private String value;

  TipoTributoOnline(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static TipoTributoOnline fromValue(String text) {
    for (TipoTributoOnline b : TipoTributoOnline.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



