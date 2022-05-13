package it.govpay.backoffice.v1.beans;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;

public class ListaTipiPendenza extends Lista<TipoPendenza> {
	
	public ListaTipiPendenza(List<TipoPendenza> risultati, URI requestUri, Long count, Integer pagina, Integer limit, BigDecimal maxRisultati) {
		super(risultati, requestUri, count, pagina, limit, maxRisultati);
	}
	
}
