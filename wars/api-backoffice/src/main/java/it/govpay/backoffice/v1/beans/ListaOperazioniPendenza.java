package it.govpay.backoffice.v1.beans;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;

public class ListaOperazioniPendenza extends Lista<OperazionePendenza> {
	
	public ListaOperazioniPendenza() {
		super();
	}
	
	public ListaOperazioniPendenza(List<OperazionePendenza> risultati, URI requestUri, Long count, Integer pagina, Integer limit, BigDecimal maxRisultati) {
		super(risultati, requestUri, count, pagina, limit, maxRisultati);
	}
	
}
