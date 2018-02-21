package it.govpay.rs.v1.beans;

import java.net.URI;
import java.util.List;

public class ListaPagamentiPortale extends Lista<PagamentoPortale> {
	
	public ListaPagamentiPortale(List<PagamentoPortale> pagamentiPortale, URI requestUri, long count, long pagina, long limit) {
		super(pagamentiPortale, requestUri, count, pagina, limit);
	}
	
}
