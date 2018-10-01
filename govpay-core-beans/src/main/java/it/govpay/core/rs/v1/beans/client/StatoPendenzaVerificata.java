package it.govpay.core.rs.v1.beans.client;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Stato della pendenza:   * NON_ESEGUITA: Pendenza da pagare  * ANNULLATA: Pendenza annullata  * SCADUTA: Termini per il pagamento decorsi  * DUPLICATA: Pendenza gia&#x27; pagata  * SCONOSCIUTA: Pendenza non trovata
 **/


/**
 * Stato della pendenza:   * NON_ESEGUITA: Pendenza da pagare  * ANNULLATA: Pendenza annullata  * SCADUTA: Termini per il pagamento decorsi  * DUPLICATA: Pendenza gia' pagata  * SCONOSCIUTA: Pendenza non trovata
 */
public enum StatoPendenzaVerificata {
  
  
  
  
  NON_ESEGUITA("NON_ESEGUITA"),
  
  
  ANNULLATA("ANNULLATA"),
  
  
  SCADUTA("SCADUTA"),
  
  
  DUPLICATA("DUPLICATA"),
  
  
  SCONOSCIUTA("SCONOSCIUTA");
  
  
  

  private String value;

  StatoPendenzaVerificata(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(this.value);
  }

  public static StatoPendenzaVerificata fromValue(String text) {
    for (StatoPendenzaVerificata b : StatoPendenzaVerificata.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



