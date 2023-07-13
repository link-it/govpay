package it.govpay.pagamento.v1.beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Stato del pagamento  * IN_CORSO: Pagamento in corso  * ANNULLATO: Pagamento annullato dall'utente o scaduto per timeout  * FALLITO: Pagamento non perfezionato per errori della piattaforma  * ESEGUITO: Pagamento concluso con successo  * NON_ESEGUITO: Processo concluso senza il perfezionamento del pagamento  * ESEGUITO_PARZIALE: Pagamento parzialmente eseguito
 */
public enum StatoPagamento {




  IN_CORSO("IN_CORSO"),


  ESEGUITO("ESEGUITO"),


  NON_ESEGUITO("NON_ESEGUITO"),


  ESEGUITO_PARZIALE("ESEGUITO_PARZIALE"),


  ANNULLATO("ANNULLATO"),


  FALLITO("FALLITO");




  private String value;

  StatoPagamento(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(this.value);
  }

  @JsonCreator
  public static StatoPagamento fromValue(String text) {
    for (StatoPagamento b : StatoPagamento.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



