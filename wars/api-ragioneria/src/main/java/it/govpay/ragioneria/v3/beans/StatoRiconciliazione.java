package it.govpay.ragioneria.v3.beans;


/**
 * Stato della riconciliazione  * IN_ELABORAZIONE: In elaborazione  * ACQUISITA: Acquisizione completata con successo  * ERRORE: Acquisizione completata con errore 
 **/
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Stato della riconciliazione  * IN_ELABORAZIONE: In elaborazione  * ACQUISITA: Acquisizione completata con successo  * ERRORE: Acquisizione completata con errore 
 */
public enum StatoRiconciliazione {
  
  
  
  
  IN_ELABORAZIONE("IN_ELABORAZIONE"),
  
  
  ACQUISITA("ACQUISITA"),
  
  
  ERRORE("ERRORE");
  
  
  

  private String value;

  StatoRiconciliazione(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static StatoRiconciliazione fromValue(String text) {
    for (StatoRiconciliazione b : StatoRiconciliazione.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



