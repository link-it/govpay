package it.govpay.backoffice.v1.beans;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Gets or Sets categoriaEvento
 */
public enum CategoriaEvento {
  
  
  
  
  INTERNO("INTERNO"),
  
  
  INTERFACCIA("INTERFACCIA"),
  
  
  UTENTE("UTENTE");
  
  
  

  private String value;

  CategoriaEvento(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static CategoriaEvento fromValue(String text) {
    for (CategoriaEvento b : CategoriaEvento.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



