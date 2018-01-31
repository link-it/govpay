package it.govpay.rs.v1.beans;

import java.net.URI;
import java.util.List;

public class ListaPagamentiPortale extends Lista<PagamentoPortale> {
	
	public ListaPagamentiPortale(List<PagamentoPortale> pagamentiPortale, URI requestUri, long count, long offset, long limit) {
		super(pagamentiPortale, requestUri, count, offset, limit);
	}
	
}
