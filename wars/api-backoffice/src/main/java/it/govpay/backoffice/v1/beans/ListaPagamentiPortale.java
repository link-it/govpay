package it.govpay.backoffice.v1.beans;

import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;

public class ListaPagamentiPortale extends Lista<it.govpay.backoffice.v1.beans.PagamentoIndex> {
	
	public ListaPagamentiPortale(List<it.govpay.backoffice.v1.beans.PagamentoIndex> pagamentiPortale, URI requestUri, long count, long pagina, long limit) {
		super(pagamentiPortale, requestUri, count, pagina, limit);
	}
	
}
