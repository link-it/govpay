package it.govpay.pagopa.v2.enumeration;

import org.apache.commons.lang3.ArrayUtils;

public enum ModelloPagamento {
	IMMEDIATO(0), 
	IMMEDIATO_MULTIBENEFICIARIO(1), 
	DIFFERITO(2), 
	ATTIVATO_PRESSO_PSP(4);
	
	private int codifica;

	ModelloPagamento(int codifica) {
		this.codifica = codifica;
	}
	
	public int getCodifica() {
		return this.codifica;
	}
	
	public static ModelloPagamento toEnum(int codifica) throws IllegalArgumentException {
		for(ModelloPagamento p : ModelloPagamento.values()){
			if(p.getCodifica() == codifica)
				return p;
		}
		throw new IllegalArgumentException("Codifica inesistente per ModelloPagamento. Valore fornito [" + codifica + "] valori possibili " + ArrayUtils.toString(ModelloPagamento.values()));
	}
}
