package it.govpay.ragioneria.v3.beans;

/**
 * Tipologia della voce riscossa  * ENTRATA: Entrata in tesoreria  * MBT: Marca da bollo telematica  * ALTRO_INTERMEDIARIO: Entrata relativa a un intermediario esterno  * ENTRATA_PA_NON_INTERMEDIATA: Entrata relativa ad un ente creditore non intermediato da GovPay 
 **/
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Tipologia della voce riscossa  * ENTRATA: Entrata in tesoreria  * MBT: Marca da bollo telematica  * ALTRO_INTERMEDIARIO: Entrata relativa a un intermediario esterno  * ENTRATA_PA_NON_INTERMEDIATA: Entrata relativa ad un ente creditore non intermediato da GovPay 
 */
public enum TipoRiscossione {
  
  
  
  
  ENTRATA("ENTRATA"),
  
  
  MBT("MBT"),
  
  
  ALTRO_INTERMEDIARIO("ALTRO_INTERMEDIARIO"),
  
  
  ENTRATA_PA_NON_INTERMEDIATA("ENTRATA_PA_NON_INTERMEDIATA");
  
  
  

  private String value;

  TipoRiscossione(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
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



