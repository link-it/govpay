package it.govpay.core.rs.v1.beans.base;

/**
 * Tipologia della Nota  * NOTA_UTENTE: Nota utente 
 **/
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Tipologia della Nota  * NOTA_UTENTE: Nota utente 
 */
public enum TipoNota {
  
  
  
  
  UTENTE("NOTA_UTENTE");
  
  
  

  private String value;

  TipoNota(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static TipoNota fromValue(String text) {
    for (TipoNota b : TipoNota.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



