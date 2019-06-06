package it.govpay.ragioneria.v1.beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Tipologia della voce riscossa  * ENTRATA: Entrata in tesoreria  * MBT: Marca da bollo telematica 
 */
public enum TipoRiscossione {
  
  
  
  
  ENTRATA("ENTRATA"),
  
  
  MBT("MBT");
  
  
  

  private String value;

  TipoRiscossione(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(this.value);
  }

  @JsonCreator
  public static TipoRiscossione fromValue(String text) {
    for (TipoRiscossione b : TipoRiscossione.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



