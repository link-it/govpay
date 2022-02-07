package it.govpay.backoffice.v1.beans;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;

public class ListaPagamentiPortale extends Lista<it.govpay.backoffice.v1.beans.PagamentoIndex> {
	
	public ListaPagamentiPortale(List<it.govpay.backoffice.v1.beans.PagamentoIndex> risultati, URI requestUri, Long count, Integer pagina, Integer limit, BigDecimal maxRisultati) {
		super(risultati, requestUri, count, pagina, limit, maxRisultati);
	}
	
}
