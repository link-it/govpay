package it.govpay.core.utils.tracciati.operazioni;

public class AnnullamentoResponse extends AbstractOperazioneResponse {
	
	public AnnullamentoResponse() {	}
	
	public static final String ESITO_DEL_OK = "DEL_OK";
	public static final String ESITO_DEL_KO = "DEL_KO";
	
	@Override
	public Object getDati() {
		switch(this.getStato()) {
		case ESEGUITO_KO:
			return this.getFaultBean(); 
		case ESEGUITO_OK:
		case NON_VALIDO:
		default:
			break;
		}
		return null;
	}
}
