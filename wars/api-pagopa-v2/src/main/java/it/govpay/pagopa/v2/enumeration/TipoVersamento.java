package it.govpay.pagopa.v2.enumeration;

import org.apache.commons.lang3.ArrayUtils;

public enum TipoVersamento {
	BONIFICO_BANCARIO_TESORERIA("BBT"), 
	BOLLETTINO_POSTALE("BP"), 
	ADDEBITO_DIRETTO("AD"), 
	CARTA_PAGAMENTO("CP"), 
	MYBANK("OBEP"), 
	ATTIVATO_PRESSO_PSP("PO"),
	OTHER("OTH");
	
	private String codifica;

	TipoVersamento(String codifica) {
		this.codifica = codifica;
	}
	public String getCodifica() {
		return this.codifica;
	}
	
	public static TipoVersamento toEnum(String codifica) throws IllegalArgumentException {
		for(TipoVersamento p : TipoVersamento.values()){
			if(p.getCodifica().equals(codifica))
				return p;
		}
		throw new IllegalArgumentException("Codifica inesistente per TipoVersamento. Valore fornito [" + codifica + "] valori possibili " + ArrayUtils.toString(TipoVersamento.values()));
	}
}
