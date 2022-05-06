package it.govpay.pendenze.v2.beans;

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
		return String.valueOf(this.value);
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

}


