package it.govpay.core.rs.v1.beans.base;

import java.net.URI;
import java.util.List;

import it.govpay.core.rs.v1.beans.Lista;

public class ListaPagamentiPortale extends Lista<it.govpay.core.rs.v1.beans.base.PagamentoIndex> {
	
	public ListaPagamentiPortale(List<it.govpay.core.rs.v1.beans.base.PagamentoIndex> pagamentiPortale, URI requestUri, long count, long pagina, long limit) {
		super(pagamentiPortale, requestUri, count, pagina, limit);
	}
	
}
