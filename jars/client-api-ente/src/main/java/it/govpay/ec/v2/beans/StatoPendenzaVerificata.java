package it.govpay.ec.v2.beans;



/**
 * Gets or Sets StatoPendenzaVerificata
 */
public enum StatoPendenzaVerificata {
  NON_ESEGUITA("NON_ESEGUITA"),
  
  ANNULLATA("ANNULLATA"),
  
  SCADUTA("SCADUTA"),
  
  SCONOSCIUTA("SCONOSCIUTA");

  private String value;

  StatoPendenzaVerificata(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
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
