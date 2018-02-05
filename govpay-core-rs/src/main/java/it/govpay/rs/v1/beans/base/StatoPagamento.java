package it.govpay.rs.v1.beans.base;

import java.util.Objects;
import org.codehaus.jackson.annotate.JsonValue;

/**
 * Stato del pagamento
 **/


/**
 * Stato del pagamento
 */
public enum StatoPagamento {
  
  
  
  
  DA_REDIRIGERE_AL_WISP("Da redirigere al WISP"),
  
  
  SELEZIONE_WISP_IN_CORSO("Selezione WISP in corso"),
  
  
  SELEZIONE_WISP_FALLITA("Selezione WISP fallita"),
  
  
  SELEZIONE_WISP_TIMEOUT("Selezione WISP timeout"),
  
  
  SELEZIONE_WISP_ANNULLATA("Selezione WISP annullata"),
  
  
  PAGAMENTO_IN_CORSO_AL_PSP("Pagamento in corso al PSP"),
  
  
  PAGAMENTO_IN_ATTESA_DI_ESITO("Pagamento in attesa di esito"),
  
  
  PAGAMENTO_ESEGUITO("Pagamento eseguito"),
  
  
  PAGAMENTO_NON_ESEGUITO("Pagamento non eseguito"),
  
  
  PAGAMENTO_PARZIALMENTE_ESEGUITO("Pagamento parzialmente eseguito"),
  
  
  PAGAMENTO_IN_ERRORE("Pagamento in errore");
  
  
  

  private String value;

  StatoPagamento(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  public static StatoPagamento fromValue(String text) {
    for (StatoPagamento b : StatoPagamento.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



