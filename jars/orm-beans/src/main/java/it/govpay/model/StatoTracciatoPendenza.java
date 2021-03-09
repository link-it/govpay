package it.govpay.model;

/**
 * Gets or Sets statoTracciatoPendenza
 */
public enum StatoTracciatoPendenza {
  
  IN_ATTESA("IN_ATTESA"),
  
  
  IN_ELABORAZIONE("IN_ELABORAZIONE"),
  
  
  ESEGUITO("ESEGUITO"),
  
  
  ESEGUITO_CON_ERRORI("ESEGUITO_CON_ERRORI"),
  
  
  SCARTATO("SCARTATO"),
  
  
  ELABORAZIONE_STAMPA("ELABORAZIONE_STAMPA");
  
  
  

  private String value;

  StatoTracciatoPendenza(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return String.valueOf(this.value);
  }

  public static StatoTracciatoPendenza fromValue(String text) {
    for (StatoTracciatoPendenza b : StatoTracciatoPendenza.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



