package it.govpay.pagamento.v2.beans;


/**
 * Stato della voce di pagamento
 **/
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Stato della voce di pagamento
 */
public enum StatoVocePendenza {
  
  
  
  
  ESEGUITO("Eseguito"),
  
  
  NON_ESEGUITO("Non eseguito"),
  
  
  ANOMALO("Anomalo");
  
  
  

  private String value;

  StatoVocePendenza(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static StatoVocePendenza fromValue(String text) {
    for (StatoVocePendenza b : StatoVocePendenza.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



