package it.govpay.core.rs.v2.beans.pagamenti;

import java.net.URI;
import java.util.List;

import it.govpay.core.rs.v1.beans.Lista;

public class ListaPagamentiIndex extends Lista<it.govpay.core.rs.v1.beans.pagamenti.PagamentoIndex> {
	
	public ListaPagamentiIndex(List<it.govpay.core.rs.v1.beans.pagamenti.PagamentoIndex> pagamentiPortale, URI requestUri, long count, long pagina, long limit) {
		super(pagamentiPortale, requestUri, count, pagina, limit);
	}
}
