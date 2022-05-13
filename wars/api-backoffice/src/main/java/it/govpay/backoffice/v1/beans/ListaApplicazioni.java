package it.govpay.backoffice.v1.beans;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;

public class ListaApplicazioni extends Lista<ApplicazioneIndex> {
	
	public ListaApplicazioni() {
		super();
	}
	
	public ListaApplicazioni(List<ApplicazioneIndex> risultati, URI requestUri, Long count, Integer pagina, Integer limit, BigDecimal maxRisultati) {
		super(risultati, requestUri, count, pagina, limit, maxRisultati);
	}
	
}
