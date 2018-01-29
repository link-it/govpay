package it.govpay.rs.v1.beans;

import java.net.URI;
import java.util.List;

public class ListaPagamentiPortale extends Lista<PagamentoPortale> {
	
	public ListaPagamentiPortale(List<PagamentoPortale> domini, URI requestUri, long count, long pagina, long risultatiPerPagina) {
		super(domini, requestUri, count, pagina, risultatiPerPagina);
	}
	
}
