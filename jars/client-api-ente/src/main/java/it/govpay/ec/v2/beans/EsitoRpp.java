package it.govpay.ec.v2.beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Esito della richiesta di pagamento:  * IN_CORSO: Pagamento in corso  * RIFIUTATO: Pagamento rifiutato  * ESEGUITO: Pagamento eseguito  * NON_ESEGUITO: Pagamento non eseguito  * ESEGUITO_PARZIALE: Pagamento parzialmente eseguito  * DECORRENZA: Decorrenza termini  * DECORRENZA_PARZIALE: Decorrenza termini parziale 
 */
public enum EsitoRpp {
  IN_CORSO("IN_CORSO"),
  
  RIFIUTATO("RIFIUTATO"),
  
  ESEGUITO("ESEGUITO"),
  
  NON_ESEGUITO("NON_ESEGUITO"),
  
  ESEGUITO_PARZIALE("ESEGUITO_PARZIALE"),
  
  DECORRENZA("DECORRENZA"),
  
  DECORRENZA_PARZIALE("DECORRENZA_PARZIALE");

  private String value;

  EsitoRpp(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static EsitoRpp fromValue(String text) {
    for (EsitoRpp b : EsitoRpp.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
  
	public static EsitoRpp fromRptEsitoPagamento(String esitoRpt) {
		switch (esitoRpt) {
		case "DECORRENZA_TERMINI":
			return DECORRENZA;
		case "DECORRENZA_TERMINI_PARZIALE":
			return DECORRENZA_PARZIALE;
		case "IN_CORSO":
			return IN_CORSO;
		case "PAGAMENTO_ESEGUITO":
			return ESEGUITO;
		case "PAGAMENTO_NON_ESEGUITO":
			return NON_ESEGUITO;
		case "PAGAMENTO_PARZIALMENTE_ESEGUITO":
			return ESEGUITO_PARZIALE;
		case "RIFIUTATO":
			return RIFIUTATO;
		}
		return null;
	}
  
}
