package it.govpay.model;

public enum StatoPendenza {
  
  ESEGUITA("ESEGUITA"),
  
  NON_ESEGUITA("NON_ESEGUITA"),
  
  ESEGUITA_PARZIALE("ESEGUITA_PARZIALE"),
  
  ANNULLATA("ANNULLATA"),
  
  SCADUTA("SCADUTA"),
  
  INCASSATA("INCASSATA"),
  
  ANOMALA("ANOMALA");
  
  
  

  private String value;

  StatoPendenza(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return String.valueOf(this.value);
  }

  public static StatoPendenza fromValue(String text) {
    for (StatoPendenza b : StatoPendenza.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



